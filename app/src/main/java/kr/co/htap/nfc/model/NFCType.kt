package kr.co.htap.nfc.model

/**
 * NFC 태그에 저장된 태그의 유형
 * @author 이호연
 */
enum class NFCType {
    IDENTIFICATION,
    COUPON,
    ERROR,
    POINT
}