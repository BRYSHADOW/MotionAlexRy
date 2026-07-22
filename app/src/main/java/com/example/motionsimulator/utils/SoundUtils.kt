package com.example.motionsimulator.utils

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

/**
 * Tiện ích âm thanh – dùng ToneGenerator có sẵn trong Android, không cần thư viện ngoài.
 */
object SoundUtils {

    private var toneGen: ToneGenerator? = null
    private val handler = Handler(Looper.getMainLooper())

    /**
     * Phát âm thanh "beep" ngắn (~150ms) trên kênh NOTIFICATION.
     * Tự động giải phóng ToneGenerator sau khi phát xong.
     */
    fun playBeep() {
        try {
            toneGen?.release()
            toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 70).also { gen ->
                gen.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
            }
            // Giải phóng sau 300ms để âm thanh có thời gian phát đủ
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                toneGen?.release()
                toneGen = null
            }, 300L)
        } catch (_: Exception) {
            // Bỏ qua nếu audio không khả dụng (thiết bị im lặng, v.v.)
        }
    }
}
