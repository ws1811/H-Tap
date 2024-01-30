package kr.co.htap.navigation.reservation


/**
 *
 * @author 김기훈
 *
 */
data class StoreEntity(
    val name: String,
    val category: String,
    val description: String,
    val image: String,
    val telephone: String,
    val address: String,
    val belong: String,
    val operationTime: ArrayList<String>,
    val documentId: String
) {
}