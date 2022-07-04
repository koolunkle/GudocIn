package com.neppplus.gudocin_android.ui.cart

import androidx.lifecycle.MutableLiveData
import com.neppplus.gudocin_android.model.BasicResponse
import com.neppplus.gudocin_android.model.cart.CartData
import com.neppplus.gudocin_android.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CartRepository @Inject constructor(private val retrofitService: RetrofitService) {
  fun getRequestCart(liveDataList: MutableLiveData<BasicResponse>) {
    val call: Call<BasicResponse> = retrofitService.getRequestCart()
    call.enqueue(object : Callback<BasicResponse> {
      override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
        liveDataList.postValue(response.body())
      }

      override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
        liveDataList.postValue(null)
      }
    })
  }


  fun deleteRequestCart(liveDataList: MutableLiveData<BasicResponse>, mCartData: CartData) {
    val call: Call<BasicResponse> = retrofitService.deleteRequestCart(mCartData.product.id)
    call.enqueue(object : Callback<BasicResponse> {
      override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
        liveDataList.postValue(response.body())
      }

      override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
        liveDataList.postValue(null)
      }
    })
  }
}