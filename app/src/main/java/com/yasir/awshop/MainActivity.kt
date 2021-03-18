package com.yasir.awshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yasir.awshop.ui.fragments.AkunFragment
import com.yasir.awshop.ui.fragments.HomeFragment
import com.yasir.awshop.ui.fragments.KeranjangFragment

class MainActivity : AppCompatActivity() {

    val fragmentHome : Fragment = HomeFragment()
    val fragmentKeranjang : Fragment = KeranjangFragment()
    val fragmentAkun : Fragment = AkunFragment()
    val fm : FragmentManager = supportFragmentManager
    var active : Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var buttomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()

        buttomNavigationView = findViewById(R.id.nav_view)
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        buttomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    Log.d("Response","Home")
                    menuItem = menu.getItem(0)
                    menuItem.isChecked = true
                    fm.beginTransaction().hide(active).show(fragmentHome).commit()
                    active = fragmentHome
                }
                R.id.navigation_keranjang -> {
                    Log.d("Response","Keranjang")
                    menuItem = menu.getItem(1)
                    menuItem.isChecked = true
                    fm.beginTransaction().hide(active).show(fragmentKeranjang).commit()
                    active = fragmentKeranjang
                }
                R.id.navigation_akun -> {
                    Log.d("Response","Akun")
                    menuItem = menu.getItem(2)
                    menuItem.isChecked = true
                    fm.beginTransaction().hide(active).show(fragmentAkun).commit()
                    active = fragmentAkun
                }
            }

            false
        }

    }
}