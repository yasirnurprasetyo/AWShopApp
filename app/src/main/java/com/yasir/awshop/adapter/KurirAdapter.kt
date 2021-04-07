package com.yasir.awshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yasir.awshop.R
import com.yasir.awshop.helper.Helper
import com.yasir.awshop.model.rajaongkir.Costs

class KurirAdapter(var data: ArrayList<Costs>, var kurir: String, var listener: Listeners) : RecyclerView.Adapter<KurirAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvLamaPengiriman = view.findViewById<TextView>(R.id.tv_lamaPengiriman)
        val tvBerat = view.findViewById<TextView>(R.id.tv_berat)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val rd = view.findViewById<RadioButton>(R.id.rd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

        holder.rd.isChecked = a.isActive

        holder.tvNama.text = kurir + " " + a.service
        val cost = a.cost[0]
        holder.tvLamaPengiriman.text = cost.etd + " hari kerja"
        holder.tvHarga.text = Helper().formatRupiah(cost.value)
        holder.tvBerat.text = "1 kg x " + Helper().formatRupiah(cost.value)

        holder.rd.setOnClickListener {
            a.isActive = true
            listener.onClicked(a, holder.adapterPosition)
        }
    }

    interface Listeners {
        fun onClicked(data: Costs, index: Int)
    }

}