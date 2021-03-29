package com.yasir.awshop

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yasir.awshop.helper.SharedPref
import com.yasir.awshop.ui.activitys.LoginActivity
import com.yasir.awshop.ui.activitys.MasukActivity
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

    private var statusLogin = false

    private lateinit var s : SharedPref

    private var dariDetail : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        setupNavButtomNavigation()

        LocalBroadcastManager.getInstance(this).registerReceiver(message, IntentFilter("event:keranjang"))
    }

    val message : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }

    fun setupNavButtomNavigation(){
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()

        buttomNavigationView = findViewById(R.id.nav_view)
        menu = buttomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        buttomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                }
                R.id.navigation_keranjang -> {
                    callFragment(1, fragmentKeranjang)
                }
                R.id.navigation_akun -> {
                    if(s.getStatusLogin()){
                        callFragment(2, fragmentAkun)
                    } else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }
                }
            }

            false
        }
    }

    fun callFragment(int : Int, fragment: Fragment){
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {
        if(dariDetail) {
            dariDetail = false
            callFragment(1, fragmentKeranjang)
        }
        super.onResume()
    }
}