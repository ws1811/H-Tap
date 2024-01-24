package kr.co.htap.navigation.reservation.TimeSelect

/**
 *
 * @author 김기훈
 *
 */
data class TimeDTO(var hour: Int, var minute: Int, val isAvailable: Boolean = true)