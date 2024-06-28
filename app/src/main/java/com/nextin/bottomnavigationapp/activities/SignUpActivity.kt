package com.nextin.bottomnavigationapp.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.nextin.bottomnavigationapp.R
import com.nextin.bottomnavigationapp.data.Users
import com.nextin.bottomnavigationapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding : ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private val CHANNEL_ID = "channelId"
    private val CHANNEL_NAME = "channelName"
    private val NOTIFY_ID = 0

    lateinit var auth : FirebaseAuth
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Registration Successful")
            .setContentText("Congratulations!  You have Successfully register into this application ")
            .setSmallIcon(R.drawable.baseline_gpp_good_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

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
                        notificationManager.notify(NOTIFY_ID,notification)
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
    
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    description = "This the Notification App"
                }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}