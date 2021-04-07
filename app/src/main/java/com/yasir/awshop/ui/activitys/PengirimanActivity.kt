package com.yasir.awshop.ui.activitys

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.yasir.awshop.R
import com.yasir.awshop.adapter.KurirAdapter
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.helper.SharedPref
import com.yasir.awshop.model.Checkout
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.model.rajaongkir.Costs
import com.yasir.awshop.model.rajaongkir.ResponOngkir
import com.yasir.awshop.room.MyDatabase
import com.yasir.awshop.service.ApiConfig
import com.yasir.awshop.service.ApiConfigAlamat
import com.yasir.awshop.util.ApiKey
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.activity_pengiriman.btn_tambahAlamat
import kotlinx.android.synthetic.main.activity_pengiriman.div_kosong
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PengirimanActivity : AppCompatActivity() {
    lateinit var myDb: MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)

        //set nama pada toolbar
        Helper().setToolbar(this, toolbar, "Pengiriman")
        myDb = MyDatabase.getInstance(this)!!

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)
        tv_totalBelanja.text = Helper().formatRupiah(totalHarga)
        mainButton()
        setSepiner()
        btn_bayar.setOnClickListener {
            bayar()
        }
    }

    private fun bayar(){
        val user = SharedPref(this).getUser()!!
        val a = myDb.daoAlamat().getByStatus(true)!!
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        val produks = ArrayList<Checkout.Item>()
        var totalItem = 0
        var totalHarga = 0

        for(p in listProduk){
            if(p.selected){
                totalItem += p.jumlah
                totalHarga += (p.jumlah*Integer.valueOf(p.harga))

                val produk = Checkout.Item()
                produk.id = ""+p.id
                produk.total_item = ""+p.jumlah
                produk.total_harga = ""+(p.jumlah * Integer.valueOf(p.harga))
                //manual
                produk.catatan = "catatat"
                produks.add(produk)
            }
        }

        val checkout = Checkout()
        checkout.user_id = ""+user.id
        checkout.user_id = "1"
        checkout.total_item = ""+totalItem
        checkout.total_harga = ""+totalHarga
        checkout.name = ""+a.name
        checkout.phone = ""+a.phone
        checkout.jasa_pengiriman = jasaKirim
        checkout.total_transfer = ""+(totalHarga+Integer.valueOf(ongkir))
        checkout.ongkir = ongkir
        checkout.kurir = kurir
        checkout.produks = produks

        val json = Gson().toJson(checkout, Checkout::class.java)
        Log.d("respon","json:"+json)
        val intent = Intent(this, PembayaranActivity::class.java)
        intent.putExtra("extra", json)
        startActivity(intent)
    }

    fun mainButton() {
        btn_tambahAlamat.setOnClickListener {
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }
    }

    private fun getOngkir(kurir: String) {
        val alamat = myDb.daoAlamat().getByStatus(true)

        val origin = "501"
        val destination = "" + alamat!!.id_kota.toString()
        val berat = 1000

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object : Callback<ResponOngkir> {
            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {
                if (response.isSuccessful) {
                    Log.d("success", "berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()) {
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }
                } else {
                    Log.d("error", "gagal memuat data (onResponse):" + response.message())
                }
            }

            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("error", "gagal memeuat data:" + t.message)
            }

        })

    }

    var ongkir =""
    var jasaKirim = ""
    var kurir = ""
    private fun displayOngkir(_kurir: String, arrayList: ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            val ongkir = arrayList[i]
            if(i == 0){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        jasaKirim = arrayOngkir[0].service
        kurir = _kurir

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter:KurirAdapter? = null
        adapter = KurirAdapter(arrayOngkir, _kurir, object : KurirAdapter.Listeners {
            override fun onClicked(data: Costs, adapterPosition: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for(ongkir in arrayOngkir){
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                jasaKirim = data.service
                kurir = _kurir
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    @SuppressLint("SetTextI18n")
    fun chekAlamat() {

        if (myDb.daoAlamat().getByStatus(true) != null) {
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat + ", " + a.kota + ", " + a.kodepos + ", (" + a.type + ")"
            btn_tambahAlamat.text = "Ubah Alamat"

            getOngkir("JNE")
        } else {
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }

    fun setSepiner() {
        val arryString = ArrayList<String>()
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter
        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    getOngkir(spn_kurir.selectedItem.toString())
                }
            }
        }
    }

    fun setTotal(ongkir: String) {
        tv_ongkir.text = Helper().formatRupiah(ongkir)
        tv_total.text = Helper().formatRupiah(Integer.valueOf(ongkir) + totalHarga)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        chekAlamat()
        super.onResume()
    }
}