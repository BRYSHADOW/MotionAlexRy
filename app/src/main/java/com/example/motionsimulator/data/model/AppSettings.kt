package com.example.motionsimulator.data.model

/**
 * Các pha trong vòng đời của một lần mô phỏng.
 * IDLE        → Đang chờ (kéo thả được phép)
 * MOVING_TO_TARGET → Chấm đỏ đang di chuyển đến chấm xanh
 * WAITING     → Đã đến, đang chờ theo delay
 * RETURNING   → Chấm đỏ đang quay về vị trí ban đầu
 */
enum class SimulationPhase {
    IDLE,
    MOVING_TO_TARGET,
    WAITING,
    RETURNING
}

/**
 * Kiểu nội suy hoạt ảnh.
 * LINEAR      → Tốc độ không đổi
 * EASE_IN_OUT → Tăng tốc ở đầu, giảm tốc ở cuối
 */
enum class EasingType(val label: String) {
    LINEAR("Tuyến tính"),
    EASE_IN_OUT("Ease In/Out")
}

/**
 * Toàn bộ cài đặt bền vững của ứng dụng.
 * Được lưu qua DataStore và đọc lại khi mở app.
 *
 * @param isDarkMode      Bật/tắt chế độ tối
 * @param themeIndex      Chỉ số màu chủ đề (0-4) trong danh sách ThemeColorOptions
 * @param animationSpeed  Hệ số tốc độ hoạt ảnh (0.1 – 5.0)
 * @param delayTime       Thời gian chờ sau khi đến đích (giây, 0 – 5)
 * @param enableLoop      Lặp vô hạn hay không
 * @param vibration       Rung khi chấm đỏ đến đích
 * @param soundEffect     Phát âm thanh khi chấm đỏ đến đích
 * @param easingType      Kiểu nội suy hoạt ảnh
 */
data class AppSettings(
    val isDarkMode: Boolean    = false,
    val themeIndex: Int        = 0,
    val animationSpeed: Float  = 1.0f,
    val delayTime: Float       = 1.0f,
    val enableLoop: Boolean    = false,
    val vibration: Boolean     = true,
    val soundEffect: Boolean   = false,
    val easingType: EasingType = EasingType.LINEAR
)
