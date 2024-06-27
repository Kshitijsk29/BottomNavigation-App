package com.nextin.bottomnavigationapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nextin.bottomnavigationapp.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private val binding : ActivitySignInBinding by lazy{
        ActivitySignInBinding.inflate(layoutInflater)
    }
    lateinit var  auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.registerHere.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Please Enter the All Credentials",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this,
                            "Login is successful",
                            Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this , MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,
                            "Login is failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this,
                            it.message,
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}