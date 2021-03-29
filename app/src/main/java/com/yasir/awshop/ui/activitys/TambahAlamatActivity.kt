package com.yasir.awshop.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.yasir.awshop.R
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.service.ApiConfigAlamat
import com.yasir.awshop.util.ApiKey
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TambahAlamatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)

        //set nama pada toolbar
        Helper().setToolbar(this,toolbar, "Tambah Alamat")
        getProvinsi()
    }

    private fun getProvinsi() {
        ApiConfigAlamat.instanceRetrofit.getProvinsi().enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrString = ArrayList<String>()
                    arrString.add("Pilih Provinsi")
                    val listArray = res.provinsi
                    for(prov in listArray){
                        arrString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter

                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                val idProv = listArray[position-1].id
                                Log.d("respon","Provinsi id:"+idProv+" "+" name:"+listArray[position-1].nama)
                                getKota(idProv)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }else{
                    Log.d("Error","Gagal memuat data : "+response.message())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }
        })
    }

    private fun getKota(id:Int) {
//        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(id).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.kota_kabupaten
                    val arrString = ArrayList<String>()
                    arrString.add("Pilih Kabupaten/Kota")
                    for(prov in listArray){
                        arrString.add(prov.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter

                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                val idKota = listArray[position-1].id
                                Log.d("respon","Provinsi id:"+idKota+" "+" name:"+listArray[position-1].nama)
                                getKecamatan(idKota)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                }else{
                    Log.d("Error","Gagal memuat data : "+response.message())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }
        })
    }

    private fun getKecamatan(id:Int) {
//        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrString = ArrayList<String>()
                    arrString.add("Pilih Kecamatan")
                    for(kecamatan in res.kecamatan){
                        arrString.add(kecamatan.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                }else{
                    Log.d("Error","Gagal memuat data : "+response.message())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}