package com.yasir.awshop.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.yasir.awshop.R
import com.yasir.awshop.adapter.BankAdapter
import com.yasir.awshop.model.Bank
import com.yasir.awshop.model.Checkout
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.service.ApiConfig
import kotlinx.android.synthetic.main.activity_pembayaran.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        displayBank()
    }

    fun displayBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("Bank BCA", "11111111111111","Waluyo", R.drawable.bca))
        arrBank.add(Bank("Bank Mandiri", "22222222222222","Waluyo", R.drawable.mandiri))
        arrBank.add(Bank("Bank BNI", "33333333333333","Waluyo", R.drawable.bni))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_pembayaranbank.layoutManager = layoutManager
        rv_pembayaranbank.adapter = BankAdapter(arrBank, object : BankAdapter.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data.nama)
            }

        })
    }

    fun bayar(bank: String){

        val json = intent.getStringExtra("extra")!!.toString()
        val checkout = Gson().fromJson(json, Checkout::class.java)

        checkout.bank = bank

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PembayaranActivity, "Berhasil terkirim ke server", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PembayaranActivity, "Gagal terkirim ke server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                Toast.makeText(this, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}