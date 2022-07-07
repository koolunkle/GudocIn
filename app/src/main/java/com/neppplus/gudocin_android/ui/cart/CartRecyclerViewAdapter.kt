package com.neppplus.gudocin_android.ui.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.databinding.AdapterCartBinding
import com.neppplus.gudocin_android.model.BasicResponse
import com.neppplus.gudocin_android.model.cart.CartData
import com.neppplus.gudocin_android.network.Retrofit
import com.neppplus.gudocin_android.network.RetrofitService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartRecyclerViewAdapter(private val mCartList: ArrayList<CartData>)
    : RecyclerView.Adapter<CartRecyclerViewAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: AdapterCartBinding)
        : RecyclerView.ViewHolder(binding.root) {

        lateinit var retrofitService: RetrofitService

        fun bind(data: CartData) {
            binding.txtProduct.text = data.product.name
            binding.txtPrice.text = data.product.getFormattedPrice()
            Glide.with(itemView.context).load(data.product.imageUrl).into(binding.imgProduct)

            binding.imgDelete.setOnClickListener {
                retrofitService =
                    Retrofit.getRetrofit(itemView.context).create(RetrofitService::class.java)

                retrofitService.deleteRequestCart(data.product.id).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(itemView.context, itemView.context.getString(R.string.cart_list_delete),
                                Toast.LENGTH_SHORT).show()
                        } else {
                            val errorJson = JSONObject(response.errorBody()!!.string())
                            Log.d(itemView.context.getString(R.string.error_case), errorJson.toString())

                            val message = errorJson.getString("message")
                            Toast.makeText(itemView.context, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("onFailure", itemView.context.resources.getString(R.string.data_loading_failed))
                    }
                })
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = AdapterCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(mCartList[position])
    }

    override fun getItemCount() = mCartList.size

}