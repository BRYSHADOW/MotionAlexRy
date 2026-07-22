package com.example.motionsimulator.ui.screens.simulation

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motionsimulator.data.model.EasingType
import com.example.motionsimulator.data.model.SimulationPhase
import com.example.motionsimulator.utils.AnimationUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * ViewModel quan ly toan bo logic mo phong:
 *  - Vi tri cac cham (red / green)
 *  - Pha mo phong (IDLE / MOVING_TO_TARGET / WAITING / RETURNING)
 *  - Vong lap hoat anh chay tren coroutine rieng
 *  - Cap nhat cai dat tu UI (speed, delay, loop, easing)
 *
 * Dung AndroidViewModel de co the goi VibrationUtils / SoundUtils
 * voi Application context neu can; hien tai callback duoc truyen vao tu UI.
 */
class SimulationViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        /** Ban kinh cham (px) – dung cho ve va cho vung cham */
        const val DOT_RADIUS = 36f
        /** Toc do co ban: 280 px/s tai speed = 1.0 */
        private const val BASE_PX_PER_SEC  = 280f
        /** Nguong den noi: khoang cach < 1px thi coi la da den */
        private const val ARRIVAL_THRESHOLD = 1f
        /** Khoang frame 8ms ~ 120 FPS (phan cung gioi han o 60 FPS) */
        private const val FRAME_MS = 8L
    }

    // ── Vi tri cac cham ───────────────────────────────────────────────────────
    private val _redPos   = MutableStateFlow(Offset.Zero)
    val redPos:   StateFlow<Offset> = _redPos.asStateFlow()

    private val _greenPos = MutableStateFlow(Offset.Zero)
    val greenPos: StateFlow<Offset> = _greenPos.asStateFlow()

    // ── Trang thai mo phong ───────────────────────────────────────────────────
    private val _phase = MutableStateFlow(SimulationPhase.IDLE)
    val phase: StateFlow<SimulationPhase> = _phase.asStateFlow()

    // ── Trang thai keo tha (cho hieu ung phong to) ───────────────────────────
    private val _draggingRed   = MutableStateFlow(false)
    val draggingRed:   StateFlow<Boolean> = _draggingRed.asStateFlow()

    private val _draggingGreen = MutableStateFlow(false)
    val draggingGreen: StateFlow<Boolean> = _draggingGreen.asStateFlow()

    // ── Cai dat hoat anh ─────────────────────────────────────────────────────
    private val _speed      = MutableStateFlow(1.0f)
    val speed: StateFlow<Float> = _speed.asStateFlow()

    private val _delayTime  = MutableStateFlow(1.0f)
    val delayTime: StateFlow<Float> = _delayTime.asStateFlow()

    private val _loop       = MutableStateFlow(false)
    val loop: StateFlow<Boolean> = _loop.asStateFlow()

    private val _easingType = MutableStateFlow(EasingType.LINEAR)
    val easingType: StateFlow<EasingType> = _easingType.asStateFlow()

    // ── Trang thai noi bo ────────────────────────────────────────────────────
    private var canvasSize         = Size.Zero
    private var positionsReady     = false
    private var redOrigin          = Offset.Zero   // vi tri goc truoc khi bat dau
    private var animJob: Job?      = null

    val isIdle: Boolean get() = _phase.value == SimulationPhase.IDLE

    // ── Su kien canvas ───────────────────────────────────────────────────────

    /**
     * Goi khi Canvas duoc ve lan dau hoac kich thuoc thay doi.
     * Lan dau: dat vi tri mac dinh (25% va 75% chieu rong).
     * Lan sau: chi clamp vi tri hien tai vao trong bounds.
     */
    fun onCanvasSizeChanged(newSize: Size) {
        if (newSize.width <= 0f || newSize.height <= 0f) return
        if (!positionsReady) {
            _redPos.value   = Offset(newSize.width * 0.25f, newSize.height * 0.5f)
            _greenPos.value = Offset(newSize.width * 0.75f, newSize.height * 0.5f)
            redOrigin = _redPos.value
            positionsReady = true
        } else {
            _redPos.value   = _redPos.value.clamp(newSize)
            _greenPos.value = _greenPos.value.clamp(newSize)
        }
        canvasSize = newSize
    }

    // ── Keo tha cham do (chi duoc khi IDLE) ──────────────────────────────────
    fun onRedDragStart() { if (isIdle) _draggingRed.value = true }
    fun onRedDrag(delta: Offset) {
        if (isIdle) _redPos.value = (_redPos.value + delta).clamp(canvasSize)
    }
    fun onRedDragEnd() { _draggingRed.value = false }

    // ── Keo tha cham xanh (luon duoc) ────────────────────────────────────────
    fun onGreenDragStart() { _draggingGreen.value = true }
    fun onGreenDrag(delta: Offset) {
        _greenPos.value = (_greenPos.value + delta).clamp(canvasSize)
    }
    fun onGreenDragEnd() { _draggingGreen.value = false }

    // ── Cap nhat cai dat ─────────────────────────────────────────────────────
    fun setSpeed(v: Float)       { _speed.value      = v }
    fun setDelayTime(v: Float)   { _delayTime.value  = v }
    fun setLoop(v: Boolean)      { _loop.value       = v }
    fun setEasingType(v: EasingType) { _easingType.value = v }

    // ── Dieu khien mo phong ──────────────────────────────────────────────────

    /**
     * Bat dau mo phong.
     * @param onVibrate  Callback bat rung (neu duoc bat trong Settings)
     * @param onSound    Callback phat am thanh (neu duoc bat trong Settings)
     */
    fun start(onVibrate: () -> Unit = {}, onSound: () -> Unit = {}) {
        if (!isIdle || !positionsReady) return
        redOrigin = _redPos.value

        animJob = viewModelScope.launch {
            do {
                // Pha 1: chuyen dong den dich
                _phase.value = SimulationPhase.MOVING_TO_TARGET
                val reached = animateTo(_greenPos.value)
                if (!reached || !isActive) break

                // Phan hoi cam giac
                onVibrate()
                onSound()

                // Pha 2: cho
                _phase.value = SimulationPhase.WAITING
                delay((_delayTime.value * 1000f).toLong().coerceAtLeast(0L))

                // Pha 3: quay ve
                _phase.value = SimulationPhase.RETURNING
                val returned = animateTo(redOrigin)
                if (!returned || !isActive) break

            } while (_loop.value && isActive)

            _phase.value = SimulationPhase.IDLE
        }
    }

    /** Dung va tra ve vi tri goc ngay lap tuc. */
    fun stop() {
        animJob?.cancel()
        _redPos.value = redOrigin
        _phase.value  = SimulationPhase.IDLE
    }

    /** Reset toan bo: dung + dat lai vi tri mac dinh. */
    fun reset() {
        animJob?.cancel()
        _phase.value = SimulationPhase.IDLE
        if (canvasSize.width > 0f) {
            _redPos.value   = Offset(canvasSize.width * 0.25f, canvasSize.height * 0.5f)
            _greenPos.value = Offset(canvasSize.width * 0.75f, canvasSize.height * 0.5f)
        }
        redOrigin = _redPos.value
    }

    // ── Vong lap hoat anh noi bo ──────────────────────────────────────────────

    /**
     * Noi suy tuyen tinh cham do tu vi tri hien tai den [target].
     * Thoi gian duoc tinh tu khoang cach / (toc do * hs_toc_do).
     * @return true neu hoan thanh, false neu bi huy (coroutine cancelled)
     */
    private suspend fun animateTo(target: Offset): Boolean {
        val from     = _redPos.value
        val distance = (target - from).getDistance()

        if (distance < ARRIVAL_THRESHOLD) {
            _redPos.value = target
            return true
        }

        val pxPerMs    = BASE_PX_PER_SEC * _speed.value / 1000f
        val durationMs = (distance / pxPerMs).toLong().coerceAtLeast(16L)
        val startMs    = System.currentTimeMillis()

        while (isActive) {
            val elapsed  = System.currentTimeMillis() - startMs
            val rawT     = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
            val easedT   = AnimationUtils.applyEasing(rawT, _easingType.value)

            _redPos.value = Offset(
                x = AnimationUtils.lerp(from.x, target.x, easedT),
                y = AnimationUtils.lerp(from.y, target.y, easedT)
            )

            if (rawT >= 1f) return true
            delay(FRAME_MS)
        }
        return false  // bi huy
    }

    /** Clamp Offset vao trong Canvas (tru di ban kinh de khong vuot bien). */
    private fun Offset.clamp(s: Size) = Offset(
        x = x.coerceIn(DOT_RADIUS, (s.width  - DOT_RADIUS).coerceAtLeast(DOT_RADIUS)),
        y = y.coerceIn(DOT_RADIUS, (s.height - DOT_RADIUS).coerceAtLeast(DOT_RADIUS))
    )
}
