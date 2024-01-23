package kr.co.htap.navigation.reservation.TimeSelect

data class TimeDTO(var hour: Int, var minute: Int, val isAvailable: Boolean = true)