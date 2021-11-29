package com.neppplus.gudocin_android.api

import com.neppplus.gudocin_android.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.GET

interface ServerAPIInterface {

//    필요 API들을 만들어주세요.
//    이 파일은 여러명이 같이 편집하게 될겁니다.


//    상품목록 받아오기
    @GET("/product")
    fun getRequestProductList() : Call<BasicResponse>


    //    전체 리뷰 목록 가져오기
    @GET("/review")
    fun getRequestReviewList(): Call<BasicResponse>


    //    카테고리 목록 가져오기
    @GET("/category")
    fun getRequestCategory(): Call<BasicResponse>

    //    전체 작은 카테고리 목록 가져오기
    @GET("/category/small")
    fun getRequestSmallCategory(): Call<BasicResponse>


}