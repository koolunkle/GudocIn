package com.neppplus.gudocin_android.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.adapters.categories.SmallCategoryListDetailRecyclerViewAdapter
import com.neppplus.gudocin_android.adapters.categories.SmallCategoryListRecyclerViewAdapter
import com.neppplus.gudocin_android.databinding.FragmentFoodCategoryListBinding
import com.neppplus.gudocin_android.datas.BasicResponse
import com.neppplus.gudocin_android.datas.ProductData
import com.neppplus.gudocin_android.datas.SmallCategoryData
import com.neppplus.gudocin_android.fragments.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodCategoryListFragment : BaseFragment() {

    lateinit var binding: FragmentFoodCategoryListBinding

    val mSmallCategoryList = ArrayList<SmallCategoryData>()
    lateinit var mSmallCategoryListRecyclerViewAdapter: SmallCategoryListRecyclerViewAdapter

    var mLargeCategoryId = 1
    var mClickedSmallCategoryNum = 1

    val mProductList = ArrayList<ProductData>()
    lateinit var mSmallCategoryListDetailRecyclerAdapter: SmallCategoryListDetailRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_food_category_list,
                container,
                false
            )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        getSmallCategoryListFromServer()
        mSmallCategoryListRecyclerViewAdapter =
            SmallCategoryListRecyclerViewAdapter(mContext, mSmallCategoryList)

        getProductListInSmallCategoryFromServer()
        mSmallCategoryListDetailRecyclerAdapter =
            SmallCategoryListDetailRecyclerViewAdapter(mContext, mProductList)
        binding.productListRecyclerView.adapter = mSmallCategoryListDetailRecyclerAdapter
        binding.productListRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getProductListInSmallCategoryFromServer() {
        apiService.getRequestSmallCategoriesItemList(mClickedSmallCategoryNum)
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        val br = response.body()!!
                        mProductList.clear()
                        mProductList.addAll(br.data.products)
                        mSmallCategoryListDetailRecyclerAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
    }

    fun getSmallCategoryListFromServer() {
        apiService.getRequestSmallCategoryDependOnLarge(mLargeCategoryId)
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        binding.layoutSmallCategoryList.removeAllViews()
                        val br = response.body()!!
                        mSmallCategoryList.clear()
                        mSmallCategoryList.addAll(br.data.small_categories)

//                        추가한 카테고리 하나하나에 대한 view 생성
                        for (sc in mSmallCategoryList) {
                            val view = LayoutInflater.from(mContext)
                                .inflate(R.layout.small_category_list_item, null)
                            val txtSmallCategoryList =
                                view.findViewById<TextView>(R.id.txtSmallCategoryList)
                            txtSmallCategoryList.text = sc.name

                            view.setOnClickListener {
                                mClickedSmallCategoryNum = sc.id
                                getProductListInSmallCategoryFromServer()
                            }
                            binding.layoutSmallCategoryList.addView(view)
                        }
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
    }

}