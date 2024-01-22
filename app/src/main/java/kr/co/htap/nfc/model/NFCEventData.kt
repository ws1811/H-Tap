package kr.co.htap.nfc.model

import android.net.Uri

/**
 *
 * @author 이호연
 */
data class NFCEventData(val uri: Uri, val addtionalData: Map<String, String>) {
    var type: NFCType

    init {
        type = NFCType.ERROR
        addtionalData["type"]?.let {
            type = when (it) {
                "IDENTIFICATION" -> NFCType.IDENTIFICATION
                "COUPON" -> NFCType.COUPON
                "POINT" -> NFCType.POINT
                else -> NFCType.ERROR
            }
        }
    }

}
