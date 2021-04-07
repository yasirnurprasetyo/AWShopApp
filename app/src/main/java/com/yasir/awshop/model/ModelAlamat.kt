package com.yasir.awshop.model

class ModelAlamat {
    //api alamat farizdot
    val id = 0
    val nama = ""

    //api rajaongkir
    val status = Status()
    val results = ArrayList<ProvinsiKota>()

    class Status {
        val code = 0
        val description = ""
    }

    class ProvinsiKota {
        val province_id = "0"
        val province = ""
        val city_id = "0"
        val city_name = ""
        val postal_code = ""
        val type = ""
    }
}