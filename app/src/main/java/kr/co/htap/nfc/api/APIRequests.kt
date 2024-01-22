package kr.co.htap.nfc.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * @author 이호연
 */
interface IdentificationAPI {
    @GET("nfcSignIn")
    fun nfcSignIn(@Query("uid") uid: String): Call<IdentificationResponse>

}

/**
 *
 * @author 이호연
 */
interface CouponAPI {
    @GET("getCoupon")
    fun getCoupon(@Query("productId") productId: String): Call<CouponResponse>
}