package com.example.pasienapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pasienapp.adapter.PasienAdapter
import com.example.pasienapp.network.RetrofitClient
import kotlinx.coroutines.launch

class PasienActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var rvPasien: RecyclerView
    private lateinit var adapter: PasienAdapter

    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pasien)

        tvWelcome   = findViewById(R.id.tvWelcome)
        btnLogout   = findViewById(R.id.btnLogout)
        progressBar = findViewById(R.id.progressBar)
        rvPasien    = findViewById(R.id.rvPasien)

        token = intent.getStringExtra("TOKEN") ?: ""
        val userName = intent.getStringExtra("USER_NAME") ?: ""

        tvWelcome.text = "Halo, $userName!"

        adapter = PasienAdapter()
        rvPasien.layoutManager = LinearLayoutManager(this)
        rvPasien.adapter = adapter

        btnLogout.setOnClickListener {
            finish()
        }

        loadPasien()
    }

    private fun loadPasien() {
        lifecycleScope.launch {
            showLoading(true)
            try {
                val response = RetrofitClient.apiService.getPasien("Bearer $token")

                if (response.isSuccessful) {
                    val pasienList = response.body()?.data ?: emptyList()
                    adapter.setData(pasienList)

                    if (pasienList.isEmpty()) {
                        showMessage("Tidak ada data pasien")
                    }
                } else {
                    showMessage("Gagal memuat data: kode ${response.code()}")
                }
            } catch (e: Exception) {
                showMessage("Error: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}