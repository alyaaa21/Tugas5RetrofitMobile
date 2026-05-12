package com.example.pasienapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pasienapp.model.LoginRequest
import com.example.pasienapp.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail     = findViewById(R.id.etEmail)
        etPassword  = findViewById(R.id.etPassword)
        btnLogin    = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)

        btnLogin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        val email    = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Email tidak boleh kosong"
            etEmail.requestFocus()
            return
        }
        if (password.isEmpty()) {
            etPassword.error = "Password tidak boleh kosong"
            etPassword.requestFocus()
            return
        }

        lifecycleScope.launch {
            showLoading(true)
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        val token    = body.data?.token ?: ""
                        val userName = body.data?.user?.name ?: ""

                        val intent = Intent(this@MainActivity, PasienActivity::class.java).apply {
                            putExtra("TOKEN", token)
                            putExtra("USER_NAME", userName)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        showMessage(body?.message ?: "Login gagal")
                    }
                } else {
                    showMessage("Login gagal: kode ${response.code()}")
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
        btnLogin.isEnabled = !isLoading
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}