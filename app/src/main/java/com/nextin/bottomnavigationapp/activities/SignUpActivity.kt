package com.nextin.bottomnavigationapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.nextin.bottomnavigationapp.data.Users
import com.nextin.bottomnavigationapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding : ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()
        val database = Firebase.database

        binding.btnSignUp.setOnClickListener {

            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,
                    "Please Enter the All Credentials",
                    Toast.LENGTH_SHORT).show()
            }
            else if(password.length < 8){
                Toast.makeText(this,
                    "Password Length should be At least 8 digit",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val user = Users(name, email,password)
                        val id = it.result.user?.uid
                        if (id!= null){
                            database.reference.child("Users").child(id).setValue(user)
                        }

                        Toast.makeText(this, "Registration Successful",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,SignInActivity::class.java))
                        finish()
                    }
                        else{
                        Toast.makeText(this, "Registration Failed",Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this,
                            it.message.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
            }

        }
        
    }
}