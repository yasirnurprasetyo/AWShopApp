package com.yasir.awshop.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.yasir.awshop.R
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.service.ApiConfig
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            register()
        }

        btn_google.setOnClickListener{
            dataDummy()
        }

    }

    fun dataDummy(){
        edt_nama.setText("ceo")
        edt_email.setText("ceo@mail.com")
        edt_phone.setText("081229844969")
        edt_password.setText("12345678")
    }


    fun register() {
        if (edt_nama.text.isEmpty()) {
            edt_nama.error = "Kolom Nama tidak boleh kosong"
            edt_nama.requestFocus()
            return
        } else if (edt_email.text.isEmpty()) {
            edt_email.error = "Kolom Email tidak boleh kosong"
            edt_email.requestFocus()
            return
        } else if (edt_password.text.isEmpty()) {
            edt_password.error = "Kolom Password tidak boleh kosong"
            edt_password.requestFocus()
            return
        }

        ApiConfig.instanceRetrofit.register(edt_nama.text.toString(), edt_email.text.toString(), edt_password.text.toString()).enqueue(object : Callback<ResponseModel>{
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error : "+t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val respon = response.body()!!

                if(respon.success == 1){
                    Toast.makeText(this@RegisterActivity, "Selamat datang:"+respon.user.name, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@RegisterActivity, "Error: "+respon.message, Toast.LENGTH_LONG).show()
                }
            }

        })
    }
}