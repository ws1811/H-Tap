package kr.co.htap.navigation.location

/**
 *
 * @author eunku
 */
data class BranchEntity (
    val name : String,
    val latitude : Double,
    val longitude : Double,
    var distance : Double
)