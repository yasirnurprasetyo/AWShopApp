package com.yasir.awshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yasir.awshop.R
import com.yasir.awshop.adapter.KeranjangAdapter
import com.yasir.awshop.adapter.ProdukAdapter
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.model.Produk
import com.yasir.awshop.room.MyDatabase
import com.yasir.awshop.ui.activitys.PengirimanActivity

class KeranjangFragment : Fragment() {

    lateinit var btnDelete: ImageView
    lateinit var rvProduk: RecyclerView
    lateinit var tvTotal: TextView
    lateinit var btnBayar: TextView
    lateinit var cbAll: CheckBox

    lateinit var adapter: KeranjangAdapter
    lateinit var myDb: MyDatabase
    var listProduk = ArrayList<Produk>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!

        mainButton()
        displayProduk()

        return view
    }

    private fun displayProduk(){
        listProduk = myDb!!.daoKeranjang().getAll() as ArrayList

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = KeranjangAdapter(requireActivity(), listProduk, object : KeranjangAdapter.Listeners{
            override fun onUpdate() {
                hitungTotal()
            }

            override fun onDelete(position:Int) {
                listProduk.removeAt(position)
                adapter.notifyDataSetChanged()
                hitungTotal()
            }

        })
        rvProduk.adapter = adapter
        rvProduk.layoutManager = layoutManager
    }

    private fun mainButton(){
        btnDelete.setOnClickListener {

        }

        btnBayar.setOnClickListener {
            val intent = Intent(requireActivity(), PengirimanActivity::class.java)
//            intent.putExtra("extra", "" + totalHarga)
            startActivity(intent)
        }

        cbAll.setOnClickListener {
            for (i in listProduk.indices) {
                val produk = listProduk[i]
                produk.selected = cbAll.isChecked

                listProduk[i] = produk
            }
        }
    }

    var totalHarga = 0
    fun hitungTotal() {
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        totalHarga = 0
        var isSelectedAll = true
        for (produk in listProduk) {
            if (produk.selected) {
                val harga = Integer.valueOf(produk.harga)
                totalHarga += (harga * produk.jumlah)
            } else {
                isSelectedAll = false
            }
        }

        cbAll.isChecked = isSelectedAll
        tvTotal.text = Helper().formatRupiah(totalHarga)
    }

    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduk = view.findViewById(R.id.rv_produk)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBayar = view.findViewById(R.id.btn_bayar)
        cbAll = view.findViewById(R.id.cb_all)
    }

    override fun onResume() {
        displayProduk()
        hitungTotal()
        super.onResume()
    }
}