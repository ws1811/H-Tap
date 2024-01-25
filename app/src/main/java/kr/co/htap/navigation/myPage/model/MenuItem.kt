package kr.co.htap.navigation.myPage.model

/**
 * @author 호연
 */
data class MenuItem(val id: String, val content: String, val onClick: () -> Unit = {})