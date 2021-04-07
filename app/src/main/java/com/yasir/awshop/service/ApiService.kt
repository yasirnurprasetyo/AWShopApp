package com.yasir.awshop.service

import com.yasir.awshop.model.Checkout
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.model.rajaongkir.ResponOngkir
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

    @POST("checkout")
    fun checkout(
        @Body data: Checkout
    ): Call<ResponseModel>

    @GET("province")
    fun getProvinsi(
            @Header("key") key:String
    ): Call<ResponseModel>

    @GET("city")
    fun getKota(
            @Header("key") key:String,
            @Query("province") id: String
    ): Call<ResponseModel>

    @GET("kecamatan")
    fun getKecamatan(
            @Query("id_kota") id: Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
            @Header("key") key:String,
            @Field("origin") origin : String,
            @Field("destination") destination : String,
            @Field("weight") weight : Int,
            @Field("courier") courier : String
    ):Call<ResponOngkir>

//    @GET("provinsi")
//    fun getProvinsi(): Call<ResponseModel>

//    @GET("kota")
//    fun getKota(
//            @Query("id_provinsi") id: Int
//    ): Call<ResponseModel>
}