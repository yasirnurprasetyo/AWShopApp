package com.yasir.awshop.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.yasir.awshop.R
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.model.Alamat
import com.yasir.awshop.model.ModelAlamat
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.room.MyDatabase
import com.yasir.awshop.service.ApiConfigAlamat
import com.yasir.awshop.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.edt_nama
import kotlinx.android.synthetic.main.activity_tambah_alamat.edt_phone
import kotlinx.android.synthetic.main.activity_tambah_alamat.pb
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TambahAlamatActivity : AppCompatActivity() {
//    api farizdot
//    var provinsi = ModelAlamat()
//    var kota = ModelAlamat()
//    var kecamatan = ModelAlamat()

    //api rajaongkitr
    var provinsi = ModelAlamat.ProvinsiKota()
    var kota = ModelAlamat.ProvinsiKota()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)

        //set nama pada toolbar
        Helper().setToolbar(this,toolbar, "Tambah Alamat")
        mainButton()
        getProvinsi()
    }

    private fun mainButton(){
        btn_simpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan(){
        when {
            edt_nama.text.isEmpty() -> {
                error(edt_nama)
                return
            }
            edt_type.text.isEmpty() -> {
                error(edt_type)
                return
            }
            edt_phone.text.isEmpty() -> {
                error(edt_phone)
                return
            }
            edt_alamat.text.isEmpty() -> {
                error(edt_alamat)
                return
            }
            edt_kodePos.text.isEmpty() -> {
                error(edt_kodePos)
                return
            }
        }

//        if (provinsi.id == 0) {
//            toast("Silahkan pilih provinsi")
//            return
//        }

//        if (kota.id == 0) {
//            toast("Silahkan pilih Kota")
//            return
//        }

        if (provinsi.province_id == "0") {
            toast("Silahkan pilih provinsi")
            return
        }

        if (kota.city_id == "0") {
            toast("Silahkan pilih Kota")
            return
        }

//        if (kecamatan.id == 0) {
//            toast("Silahkan pilih Kecamatan")
//            return
//        }

        val alamat = Alamat()
        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

//        alamat.id_provinsi = Integer.valueOf(provinsi.id)
//        alamat.provinsi = provinsi.nama
//        alamat.id_kota = Integer.valueOf(kota.id)
//        alamat.kota = kota.nama

        alamat.id_provinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.city_name

        alamat.id_kota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name

//        alamat.id_kecamatan = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        insert(alamat)
    }

    private fun getProvinsi() {
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrString = ArrayList<String>()
                    arrString.add("Pilih Provinsi")

                    val listArray = res.rajaongkir.results
                    for(prov in listArray){
                        arrString.add(prov.province)
                    }
                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter
                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                provinsi = listArray[position - 1]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }else{
                    Log.d("Error","Gagal memuat data : "+response.message())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d("Error","Failure : "+t.message)
            }
        })
    }

    private fun getKota(id:String) {
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results

                    val arrString = ArrayList<String>()
                    arrString.add("Pilih Kabupaten/Kota")
                    for(kota in listArray){
                        arrString.add(kota.type+" "+kota.city_name)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter
                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                kota = listArray[position-1]
                                val kodePos = kota.postal_code
                                edt_kodePos.setText(kodePos)
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
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrString = ArrayList<String>()
                    val listArray = res.kecamatan
                    arrString.add("Pilih Kecamatan")
                    for(kecamatan in listArray){
                        arrString.add(kecamatan.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter

                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                kecamatan = listArray[position-1]
                                val idKecamatan = listArray[position-1].id
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

    private fun insert(data: Alamat) {
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamat().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb.daoAlamat().insert(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toast("Insert data success")
                    onBackPressed()
                })
    }

    fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun error(editText: EditText) {
        editText.error = "Kolom tidak boleh kosong"
        editText.requestFocus()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /*
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
                                provinsi = listArray[position - 1]
                                val idProv = listArray[position - 1].id
                                getKota(idProv)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }else{
                    Log.d("Error","Gagal memuat data : "+response.message())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d("Error","Failure : "+t.message)
            }
        })
    }

    private fun getKota(id:Int) {
        pb.visibility = View.VISIBLE
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
                                kota = listArray[position-1]
                                val idKota = listArray[position-1].id
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
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arrString = ArrayList<String>()
                    val listArray = res.kecamatan
                    arrString.add("Pilih Kecamatan")
                    for(kecamatan in listArray){
                        arrString.add(kecamatan.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arrString.toTypedArray())

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter

                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if(position != 0){
                                kecamatan = listArray[position-1]
                                val idKecamatan = listArray[position-1].id
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
    */
}