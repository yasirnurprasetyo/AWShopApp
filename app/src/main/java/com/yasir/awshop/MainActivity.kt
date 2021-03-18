package com.yasir.awshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.yasir.awshop.ui.fragments.AkunFragment
import com.yasir.awshop.ui.fragments.HomeFragment
import com.yasir.awshop.ui.fragments.KeranjangFragment

class MainActivity : AppCompatActivity() {

    val fragmentHome : Fragment = HomeFragment()
    val fragmentKeranjang : Fragment = KeranjangFragment()
    val fragmentAkun : Fragment = AkunFragment()
    val fm : FragmentManager = supportFragmentManager
    var active : Fragment = fragmentHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()

    }
}