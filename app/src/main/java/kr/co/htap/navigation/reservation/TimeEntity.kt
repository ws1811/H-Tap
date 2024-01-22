package kr.co.htap.navigation.reservation

data class TimeEntity(val hour: Int, val minute: Int, val isAvailable: Boolean = true) {
}
