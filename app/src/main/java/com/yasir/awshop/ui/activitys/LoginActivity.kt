package com.yasir.awshop.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yasir.awshop.R
import com.yasir.awshop.helper.SharedPref
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var s : SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = SharedPref(this)

        btn_login.setOnClickListener {
            s.setStatusLogin(true)
        }
    }
}