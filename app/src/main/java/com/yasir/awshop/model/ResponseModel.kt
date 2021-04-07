package com.yasir.awshop.model

class ResponseModel {
    var success = 0
    lateinit var message: String
    var user = User()
    var produks : ArrayList<Produk> = ArrayList()

    var rajaongkir = ModelAlamat()

    //api farizdot
    var provinsi: ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten: ArrayList<ModelAlamat> = ArrayList()
    var kecamatan: ArrayList<ModelAlamat> = ArrayList()
}