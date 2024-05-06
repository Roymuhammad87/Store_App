package com.adrammedia.storenet.frontend.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.adrammedia.storenet.R
import com.adrammedia.storenet.databinding.ActivityRegisterBinding
import com.adrammedia.storenet.frontend.viewmodels.RegisterLoginViewModel
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerLoginViewModel: RegisterLoginViewModel by viewModels<RegisterLoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegi.setOnClickListener {
                    val fullName = binding.etRegiName.text.toString()
                    val email = binding.etRegiEmail.text.trim().toString()
                    val password = binding.etRegiPassword.text.toString()
                    if (!validation(fullName, email, password)){
                        binding.progressBar.visibility =View.GONE
                    } else{
                        registerLoginViewModel.register(fullName = fullName, email = email, password = password)
                        lifecycleScope.launch {
                            registerLoginViewModel.registerStateFlow.collect {
                                when(it.status){
                                    DataStatus.Status.LOADING -> {
                                        binding.progressBar.visibility = View.VISIBLE
                                    }
                                    DataStatus.Status.SUCCESS -> {
                                        binding.progressBar.visibility = View.GONE
                                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                        Toast.makeText(this@RegisterActivity, it.data?.message, Toast.LENGTH_SHORT).show()
                                    }
                                    DataStatus.Status.ERROR -> {
                                        binding.progressBar.visibility = View.GONE
                                        Log.d("TAG", "onCreate: ${it.data?.message}")
                                    }
                                }
                            }
                        }
                    }
                }

        binding.tvRegiHaveAcc.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

    }
    private fun validation(fullName:String, email: String, password:String):Boolean {
        if(fullName.isEmpty()) {
            binding.etRegiName.apply {
                requestFocus()
                error = "The full name is required"
            }
        }
        if(fullName.length <= 4){
            binding.etRegiName.apply {
                requestFocus()
                error = "The full name must be more than 4 character"
            }
        }
        if(email.isEmpty()) {
            binding.etRegiEmail.apply {
                requestFocus()
                error = "The email is required"
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etRegiEmail.apply {
                requestFocus()
                error = "The email must be a valid email"
            }
        }
        if(password.isEmpty()) {
            binding.etRegiPassword.apply {
                requestFocus()
                error = "The password is required"
            }
        }
        if(password.length < 6){
            binding.etRegiPassword.apply {
                requestFocus()
                error = "The Password must be more than 5 characters"
            }
        }
        return true
    }
}