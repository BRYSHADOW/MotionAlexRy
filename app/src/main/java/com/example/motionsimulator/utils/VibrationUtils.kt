package com.example.motionsimulator.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Tiện ích rung – tương thích từ API 26 đến API mới nhất.
 *
 * Android 12+ (API 31): dùng VibratorManager (Vibrator cũ bị deprecated).
 * Android 8–11 (API 26–30): dùng Vibrator trực tiếp.
 */
object VibrationUtils {

    /**
     * Rung một lần ngắn để xác nhận chấm đỏ đã đến đích.
     * @param durationMs  Thời gian rung tính bằng ms (mặc định 80ms)
     */
    fun vibrate(context: Context, durationMs: Long = 80L) {
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator.hasVibrator()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    }
}
