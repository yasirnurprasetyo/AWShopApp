package com.yasir.awshop.service

import com.yasir.awshop.model.ResponseModel
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("phone") phone : String,
        @Field("password") password : String
    ):Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ):Call<ResponseModel>

    @GET("produk")
    fun getProduk(): Call<ResponseModel>

    @GET("provinsi")
    fun getProvinsi(
    ): Call<ResponseModel>

    @GET("kota")
    fun getKota(
        @Query("id_provinsi") id: Int
    ): Call<ResponseModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ): Call<ResponseModel>
}