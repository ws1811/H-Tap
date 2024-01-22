package kr.co.htap.navigation.reservation


/**
 *
 * @author 김기훈
 *
 */
class StoreEntity {

    val category: String
    val name: String
    val description: String
    val imageURL: String
    val id: Int

    constructor(category: String, name: String, description: String, imageURL: String, id: Int) {
        this.category = category
        this.name = name
        this.description = description
        this.imageURL = imageURL
        this.id = id
    }
}