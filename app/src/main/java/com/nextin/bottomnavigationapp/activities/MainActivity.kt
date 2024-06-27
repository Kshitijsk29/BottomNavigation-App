package com.nextin.bottomnavigationapp.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nextin.bottomnavigationapp.R
import com.nextin.bottomnavigationapp.fragments.FeedFragment
import com.nextin.bottomnavigationapp.fragments.HomeFragment
import com.nextin.bottomnavigationapp.fragments.ProfileFragment
import com.nextin.bottomnavigationapp.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideSystemUI()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.navBottom)

        replaceFrameWithFragment(HomeFragment())

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemHome -> replaceFrameWithFragment(HomeFragment())
                R.id.itemSearch -> replaceFrameWithFragment(SearchFragment())
                R.id.itemFeed -> replaceFrameWithFragment(FeedFragment())
                R.id.itemProfile -> replaceFrameWithFragment(ProfileFragment())
                else ->{

                }
            }
            true
        }
    }

    private fun replaceFrameWithFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}