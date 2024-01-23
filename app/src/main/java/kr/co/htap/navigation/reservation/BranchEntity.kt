package kr.co.htap.navigation.reservation

/**
 *
 * @author eunku
 */
class BranchEntity {
    val id : Int
    val name : String
    val latitude : Double
    val longitude : Double

    constructor(id : Int, name : String, latitude :Double, longitude : Double) {
        this.id = id
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
    }

}