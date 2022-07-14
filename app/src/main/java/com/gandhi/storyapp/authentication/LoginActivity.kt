package com.gandhi.storyapp.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.gandhi.storyapp.ViewModelFactory
import com.gandhi.storyapp.databinding.ActivityLoginBinding
import com.gandhi.storyapp.mainactivity.MainActivity
import com.gandhi.storyapp.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        playAnimation()
        setupButton()
        setEnabledButton()
        checkToken()
        supportActionBar?.hide()


    }

    private fun checkToken() {
        loginViewModel.getUser().observe(this) {
            if (it != "") {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }


    private fun setupButton() {
        binding.registerHere.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Mohon Menunggu", Toast.LENGTH_SHORT).show()
            loginUser()
        }


        binding.emailEdt.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        binding.passEdit.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    setEnabledButton()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
    }


    private fun setEnabledButton() {
        val result = binding.emailEdt.text
        val result2 = binding.passEdit.text
        binding.loginBtn.isEnabled = result != null && result.toString()
            .isNotEmpty() && result2 != null && result2.toString().isNotEmpty()
    }

    private fun loginUser() {
        val email = binding.emailEdt.text.toString()
        val password = binding.passEdit.text.toString()

        loginViewModel.getLogin(email, password)
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[LoginViewModel::class.java]

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val textLogin = ObjectAnimator.ofFloat(binding.txtLogin, View.ALPHA, 1f).setDuration(500)
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEdt, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passEdit, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(500)
        val txtBelum = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val txtDaftar =
            ObjectAnimator.ofFloat(binding.registerHere, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(button, txtBelum, txtDaftar)
        }

        AnimatorSet().apply {
            playSequentially(textLogin, image, email, password, together)
            start()
        }


    }
}



