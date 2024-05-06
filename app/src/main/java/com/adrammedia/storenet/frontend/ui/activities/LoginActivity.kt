package com.adrammedia.storenet.frontend.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import com.adrammedia.storenet.R
import com.adrammedia.storenet.backend.data.users.passwords.ForgetPasswordRequest
import com.adrammedia.storenet.databinding.ActivityLoginBinding
import com.adrammedia.storenet.frontend.viewmodels.ForgetPasswordViewModel

import com.adrammedia.storenet.frontend.viewmodels.RegisterLoginViewModel
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val forgetPasswordViewModel by viewModels<ForgetPasswordViewModel>()
    private lateinit var sharedPreferences: SharedPreferences
    private val registerLoginViewModel: RegisterLoginViewModel by viewModels()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.loginBtnSignin.setOnClickListener {
            login(editor)
        }

        binding.loginTvSignin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.loginTvForgetPassword.setOnClickListener {
            val email = binding.emailLogin.text.toString().trim()
            if (email.isEmpty()){
                binding.emailLogin.requestFocus()
                binding.emailLogin.error= "Email is required to restore your password"
            } else {
                val request = ForgetPasswordRequest(email)
                forgetPassword(request)
            }
        }
    }

    private fun login(editor: SharedPreferences.Editor) {
        binding.loginPb.visibility = View.VISIBLE
        val email = binding.emailLogin.text.toString().trim()
        val password = binding.passwordLogin.text.toString()
        if (validation(email, password)){
               registerLoginViewModel.login(email, password)
                lifecycleScope.launch {
                    registerLoginViewModel.loginStateFlow.collect{
                        when(it.status){
                            DataStatus.Status.LOADING->{
                                binding.loginPb.visibility = View.VISIBLE
                            }
                            DataStatus.Status.SUCCESS->{
                                binding.loginPb.visibility = View.GONE
                                editor.putString("userToken", it.data?.data?.token)
                                editor.putString("id", it.data?.data?.id.toString())
                                editor.apply()
                                Toast.makeText(this@LoginActivity, it.data?.message, Toast.LENGTH_SHORT).show()
                                Intent(this@LoginActivity, HomeActivity::class.java).also { intent ->
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                            }
                            DataStatus.Status.ERROR->{
                                withContext(Dispatchers.Main){
                                    binding.loginPb.visibility = View.GONE
                                    Toast.makeText(this@LoginActivity, "${it.data?.message}", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                }
        }
    }

    private fun validation( email: String, password:String):Boolean {

        if(email.isEmpty()) {
            binding.emailLogin.apply {
                requestFocus()
                error = "The email is required"
            }
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailLogin.apply {
                requestFocus()
                error = "The email must be a valid email"
            }
        }
        if(password.isEmpty()) {
            binding.passwordLogin.apply {
                requestFocus()
                error = "The password is required"
            }
        }
        if(password.length < 6){
            binding.passwordLogin.apply {
                requestFocus()
                error = "The Password must be more than 5 characters"
            }
        }
        return true
    }

    private fun forgetPassword(email: ForgetPasswordRequest){
        forgetPasswordViewModel.forgetPassword(email)
        lifecycleScope.launch {
            forgetPasswordViewModel.forgetPasswordStateFlow.collect{
                when(it.status){
                    DataStatus.Status.LOADING->{
                        binding.loginPb.visibility = View.VISIBLE
                    }
                    DataStatus.Status.SUCCESS->{
                        binding.loginPb.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, it.data?.message, Toast.LENGTH_SHORT).show()
                    }
                    DataStatus.Status.ERROR->{
                        binding.loginPb.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, it.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

