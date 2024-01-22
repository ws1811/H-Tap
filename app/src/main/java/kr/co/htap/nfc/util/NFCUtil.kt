package kr.co.htap.nfc.util

import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.util.Log
import kr.co.htap.nfc.model.NFCEventData

/**
 *
 * @author 이호연
 */
abstract class NFCUtil {
    companion object {
        fun interface OnParseContentDone {
            fun onParseContentDone(eventSource: NFCEventData)
        }

        /***
         * @param sourceNfcIntent 시스템에서 넘어온 NFC인텐트
         * @param onParseContentDone NFC 인텐트에서 파싱된 NFCEventSource를 처리할 콜백 함수
         */
        fun parseNFCContent(sourceNfcIntent: Intent, onParseContentDone: OnParseContentDone) {
            val map = mutableMapOf<String, String>()
            var urlString: String? = null
            sourceNfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                ?.let { parcelable ->
                    parcelable.forEach {
                        (it as NdefMessage).records?.forEach { record ->
                            val s = String(record.payload).substring(1) //문장의 시작이 NULL문자와 STX문자이기 때문
                            if (s.startsWith("hnfc")) urlString = s
                            else if (s.isNotEmpty()) {
                                val splitted = s.substring(2).split(':')
                                map[splitted[0]] = if (splitted.size > 1) splitted[1] else ""
                            }
                        }
                    }

                    if (urlString == null) {
                        Log.e("NFCUtil", "잘못된 NFC입니다.")
                        return;
                    }

                    onParseContentDone.onParseContentDone(
                        NFCEventData(Uri.parse(urlString), map)
                    )
                }

        }
    }
}

/**
 * 정해진 규칙대로 작성되지 않은 NFC 태그를 태깅 한 경우 발생합니다.
 * @author 이호연
 */
class InvalidNFCException(val s: String) : Throwable() {
}
