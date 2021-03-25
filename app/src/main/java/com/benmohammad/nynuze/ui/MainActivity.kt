package com.benmohammad.nynuze.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.benmohammad.nynuze.R
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.coordinator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }



    private fun setupNavigation() {
        navController = this.findNavController(R.id.nav_host_fragment)
        bottom_navigation.itemIconTintList = null
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }
}