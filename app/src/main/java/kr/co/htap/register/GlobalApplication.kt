package kr.co.htap.register

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

/**
 *
 * @author 송원선
 *
 */
class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "ac5456a18f99663b24f10709db7fae94")
    }
}