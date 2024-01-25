package kr.co.htap.navigation.myPage.model

/**
 * @author 호연
 */
data class MenuItemContent(val content: String, val onClick: () -> Unit = {}) {
    companion object {
        fun createMenuItem(
            position: Int,
            content: MenuItemContent
        ): MenuItem {
            return MenuItem(
                position.toString(),
                content.content,
                content.onClick
            )
        }
    }
}