package com.neppplus.gudocin_android.view.presenter.fragment.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.databinding.FragmentCategoryBinding
import com.neppplus.gudocin_android.model.BasicResponse
import com.neppplus.gudocin_android.model.category.SmallCategoryData
import com.neppplus.gudocin_android.view.presenter.activity.main.MainActivity
import com.neppplus.gudocin_android.view.presenter.fragment.BaseFragment
import com.neppplus.gudocin_android.view.presenter.fragment.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : BaseFragment() {

  lateinit var binding: FragmentCategoryBinding

  var mLargeCategoryId = 1

  var mClickedSmallCategoryNum = 1

  var mSmallCategoriesList = ArrayList<SmallCategoryData>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    setupEvents()
    setValues()
  }

  override fun setupEvents() {
    binding.imgFoodCategory.setOnClickListener {
      mLargeCategoryId = 1
      getCategoryFromServer()
    }
    binding.imgClothesCategory.setOnClickListener {
      mLargeCategoryId = 2
      getCategoryFromServer()
    }
    binding.imgLifeCategory.setOnClickListener {
      mLargeCategoryId = 3
      getCategoryFromServer()
    }
  }

  override fun setValues() {
    getCategoryFromServer()
  }

  private fun getCategoryFromServer() {
    if (isInitialized) {
      apiService.getRequestSmallCategoryDependOnLarge(mLargeCategoryId).enqueue(object : Callback<BasicResponse> {
        override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
          if (response.isSuccessful) {
            val br = response.body()!!
            mSmallCategoriesList.clear()
            mSmallCategoriesList.addAll(br.data.small_categories)

            // 추가한 카테고리 하나하나에 대한 view 생성
            binding.llCategory.removeAllViews()
            for (sc in mSmallCategoriesList) {
              val view = LayoutInflater.from(mContext).inflate(R.layout.adapter_category, null)
              val txtSmallCategoryList = view.findViewById<TextView>(R.id.txtCategory)
              txtSmallCategoryList.text = sc.name

              view.setOnClickListener {
                mClickedSmallCategoryNum = sc.id
                // MainActivity -> HomeFragment
                val homeFragment =
                  ((requireContext() as MainActivity).binding.viewPager.adapter as MainActivity.ViewPagerAdapter).getFragment(0) as HomeFragment
                homeFragment.getReviewFromServer(mClickedSmallCategoryNum)
              }
              binding.llCategory.addView(view)
            }
          }
        }

        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
      })
    }
  }

}