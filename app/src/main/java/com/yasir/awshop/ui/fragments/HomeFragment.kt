package com.yasir.awshop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.yasir.awshop.R
import com.yasir.awshop.adapter.ProdukAdapter
import com.yasir.awshop.adapter.SliderAdapter
import com.yasir.awshop.model.Produk
import com.yasir.awshop.model.ResponseModel
import com.yasir.awshop.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    lateinit var vpSlider : ViewPager
    lateinit var rvProduk : RecyclerView
    lateinit var rvProdukTerlaris : RecyclerView
    lateinit var rvElektronik : RecyclerView

    private var listProduk: ArrayList<Produk> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getProduk()
        displayProduk()

        return view
    }

    fun getProduk() {
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    val arrayProduk = ArrayList<Produk>()
                    for (p in res.produks) {
                        arrayProduk.add(p)
                    }
                    listProduk = arrayProduk
                    displayProduk()
                }
            }
        })
    }

    fun init(view: View) {
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvProdukTerlaris = view.findViewById(R.id.rv_produkTerlaris)
        rvElektronik = view.findViewById(R.id.rv_elektronik)
    }

    fun displayProduk(){
        val arraySlider = ArrayList<Int>()
        arraySlider.add(R.drawable.slider1)
        arraySlider.add(R.drawable.slider2)
        arraySlider.add(R.drawable.slider3)

        val sliderAdapter = SliderAdapter(arraySlider, activity)
        vpSlider.adapter = sliderAdapter

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager1 = LinearLayoutManager(activity)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        rvProduk.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager

        rvProdukTerlaris.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvProdukTerlaris.layoutManager = layoutManager1

        rvElektronik.adapter = ProdukAdapter(requireActivity(), listProduk)
        rvElektronik.layoutManager = layoutManager2
    }

//    val arrayProduk: ArrayList<Produk>get(){
//
//        val array = ArrayList<Produk>()
//
//        val p1 = Produk()
//        p1.nama = "HP Sungsang"
//        p1.harga = "Rp. 4.000.000"
//        p1.gambar = R.drawable.hp_14_bs749tu
//
//        val p2 = Produk()
//        p2.nama = "HP Remi"
//        p2.harga = "Rp. 4.000.000"
//        p2.gambar = R.drawable.hp_envy_13_aq0019tx
//
//        val p3 = Produk()
//        p3.nama = "HP Ono"
//        p3.harga = "Rp. 4.000.000"
//        p3.gambar = R.drawable.hp_pavilion_13_an0006na
//
//        array.add(p1)
//        array.add(p2)
//        array.add(p3)
//
//        return array
//    }
//    val arrayTerlaris: ArrayList<Produk>get(){
//
//        val array = ArrayList<Produk>()
//
//        val p1 = Produk()
//        p1.nama = "HP Sungsang"
//        p1.harga = "Rp. 4.000.000"
//        p1.gambar = R.drawable.hp_14_bs749tu
//
//        val p2 = Produk()
//        p2.nama = "HP Remi"
//        p2.harga = "Rp. 4.000.000"
//        p2.gambar = R.drawable.hp_envy_13_aq0019tx
//
//        val p3 = Produk()
//        p3.nama = "HP Ono"
//        p3.harga = "Rp. 4.000.000"
//        p3.gambar = R.drawable.hp_pavilion_13_an0006na
//
//        array.add(p1)
//        array.add(p2)
//        array.add(p3)
//
//        return array
//    }
//    val arrayElektronik: ArrayList<Produk>get(){
//
//        val array = ArrayList<Produk>()
//
//        val p1 = Produk()
//        p1.nama = "HP Sungsang"
//        p1.harga = "Rp. 4.000.000"
//        p1.gambar = R.drawable.hp_14_bs749tu
//
//        val p2 = Produk()
//        p2.nama = "HP Remi"
//        p2.harga = "Rp. 4.000.000"
//        p2.gambar = R.drawable.hp_envy_13_aq0019tx
//
//        val p3 = Produk()
//        p3.nama = "HP Ono"
//        p3.harga = "Rp. 4.000.000"
//        p3.gambar = R.drawable.hp_pavilion_13_an0006na
//
//        array.add(p1)
//        array.add(p2)
//        array.add(p3)
//
//        return array
//    }

}