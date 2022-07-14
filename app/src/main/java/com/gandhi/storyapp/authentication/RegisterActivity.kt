package com.gandhi.storyapp.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gandhi.storyapp.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerView: RegisterViewModel

    companion object {
        const val TAG = "RegistActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setEnabledButton()
        setupButton()
        playAnimation()
        supportActionBar?.hide()

    }

    private fun setupViewModel() {
        registerView = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    private fun setupButton() {

        binding.btnRegist.setOnClickListener {

            registerUser()


        }

        binding.nameEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEnabledButton()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.emailEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEnabledButton()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.passEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEnabledButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.passEdit.toString().length < 6) {
                    binding.passEdit.error = "Password minimal 6 karakter"
                }
            }
        })
    }

    private fun setEnabledButton() {
        val result = binding.emailEdt.text
        val result2 = binding.passEdit.text
        val result3 = binding.nameEdt.text
        binding.btnRegist.isEnabled = result != null && result.toString()
            .isNotEmpty() && result2 != null && result2.toString().isNotEmpty()
                && result3 != null && result3.toString().isNotEmpty()
    }

    private fun registerUser() {
        val password = binding.passEdit.text.toString()
        val name = binding.nameEdt.text.toString()
        val email = binding.emailEdt.text.toString()

        registerView.register(name, email, password)

        registerView.isSuccess.observe(this) { status ->

            status.getContentIfNotHandled()?.let {

                if (it == "Registration Succes") {
                    Toast.makeText(this@RegisterActivity , it, Toast.LENGTH_SHORT).show()
                    moveActivity()
                } else {
                    Toast.makeText(this@RegisterActivity , it, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }


    private fun moveActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()


        val textRegister =
            ObjectAnimator.ofFloat(binding.txtRegister, View.ALPHA, 1f).setDuration(500)
        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameEdt, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEdt, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passEdit, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnRegist, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(textRegister, image, name, email, password, button)
            start()
        }
    }


}