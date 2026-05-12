package com.example.pasienapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapp.R
import com.example.pasienapp.model.Pasien

class PasienAdapter : RecyclerView.Adapter<PasienAdapter.PasienViewHolder>() {

    private val listPasien = mutableListOf<Pasien>()

    fun setData(data: List<Pasien>) {
        listPasien.clear()
        listPasien.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasienViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pasien, parent, false)
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasienViewHolder, position: Int) {
        holder.bind(listPasien[position])
    }

    override fun getItemCount(): Int = listPasien.size

    class PasienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvInisial: TextView = itemView.findViewById(R.id.tvInisial)
        private val tvNama: TextView    = itemView.findViewById(R.id.tvNama)
        private val tvDetail: TextView  = itemView.findViewById(R.id.tvDetail)

        fun bind(pasien: Pasien) {


            val nama = pasien.nama ?: "Pasien"
            tvNama.text = nama

            // Tampilkan inisial huruf pertama dari nama
            tvInisial.text = nama.firstOrNull()?.uppercaseChar()?.toString() ?: "P"

            // Detail: gabungkan field yang tersedia
            val detail = buildString {
                pasien.jenis_kelamin?.let { append("$it  ·  ") }
                pasien.tanggal_lahir?.let { append("TTL: $it  ·  ") }
                pasien.no_telepon?.let   { append("☎ $it") }
            }.trimEnd(' ', '·', ' ')

            tvDetail.text = detail.ifEmpty { "Tidak ada detail" }
        }
    }
}