package com.example.motionsimulator.utils

import com.example.motionsimulator.data.model.EasingType

/**
 * Tiện ích hoạt ảnh – tập trung tất cả hàm nội suy vào một nơi.
 */
object AnimationUtils {

    /**
     * Áp dụng hàm easing lên giá trị progress [0.0, 1.0].
     *
     * @param t       Tiến trình thô (linear, 0.0 → 1.0)
     * @param easing  Kiểu nội suy muốn dùng
     * @return        Tiến trình đã qua nội suy (vẫn trong [0.0, 1.0])
     */
    fun applyEasing(t: Float, easing: EasingType): Float = when (easing) {
        EasingType.LINEAR      -> t
        EasingType.EASE_IN_OUT -> easeInOutCubic(t)
    }

    /**
     * Cubic ease-in-out: tăng tốc ở đầu, giảm tốc ở cuối.
     * Công thức: f(t) = 4t³       nếu t < 0.5
     *            f(t) = 1 - (-2t+2)³/2  nếu t ≥ 0.5
     */
    private fun easeInOutCubic(t: Float): Float {
        return if (t < 0.5f) {
            4f * t * t * t
        } else {
            val p = -2f * t + 2f
            1f - (p * p * p) / 2f
        }
    }

    /**
     * Nội suy tuyến tính giữa [a] và [b] theo tỷ lệ [t].
     */
    fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
}
