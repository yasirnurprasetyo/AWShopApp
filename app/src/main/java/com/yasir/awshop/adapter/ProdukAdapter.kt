package com.yasir.awshop.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.yasir.awshop.R
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.model.Produk
import com.yasir.awshop.ui.activitys.DetailProdukActivity
import com.yasir.awshop.util.Config
import kotlin.collections.ArrayList

class ProdukAdapter(var activity: Activity, var data : ArrayList<Produk>):RecyclerView.Adapter<ProdukAdapter.Holder>() {

     class Holder(view : View):RecyclerView.ViewHolder(view){
         val tvNama = view.findViewById<TextView>(R.id.tv_nama)
         val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
         val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
         val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].name
        holder.tvHarga.text = Helper().formatRupiah(data[position].harga)
//        holder.imgProduk.setImageResource(data[position].image)
        val image = Config.productUrl + data[position].image
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product)
                .error(R.drawable.product)
                .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val intent = Intent(activity, DetailProdukActivity::class.java)

            val str = Gson().toJson(data[position], Produk::class.java)
            intent.putExtra("extra", str)

            activity.startActivity(intent)
        }
    }
}
