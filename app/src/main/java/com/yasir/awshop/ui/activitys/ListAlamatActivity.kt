package com.yasir.awshop.ui.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yasir.awshop.R
import com.yasir.awshop.helper.Helper
import kotlinx.android.synthetic.main.toolbar.*

class ListAlamatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_alamat)

        //set nama pada toolbar
        Helper().setToolbar(this,toolbar, "Pilih Alamat")

        mainButton()

    }

    fun mainButton(){
        startActivity(Intent(this, TambahAlamatActivity::class.java))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}