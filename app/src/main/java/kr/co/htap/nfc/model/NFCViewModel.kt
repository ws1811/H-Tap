package kr.co.htap.nfc.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * @author 이호연
 */
class NFCViewModel : ViewModel() {
    private val _couponEventData = MutableLiveData<NFCEventData>()
    private val _identificationEventData = MutableLiveData<NFCEventData>()
    fun setCouponEventData(newData: NFCEventData) {
        _couponEventData.value = newData
    }

    fun setIdentificationData(newData: NFCEventData) {
        _identificationEventData.value = newData
    }
}