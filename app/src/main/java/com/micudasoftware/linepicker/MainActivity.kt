package com.micudasoftware.linepicker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Line Picker"
            val description = "Line Picker Created PDF"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("1", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findViewById<View>(R.id.navHostFragment).findNavController()
        when (item.itemId) {
            R.id.about_us -> startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.activateyes.net/about")))
            R.id.user_manual -> navController.navigate(
                NavigationDirections.actionGlobalPDFFragment("UserManual.pdf"))
            R.id.license_agreement -> navController.navigate(
                NavigationDirections.actionGlobalPDFFragment("LicenseAgreement.pdf"))
            R.id.version -> navController.navigate(
                NavigationDirections.actionGlobalPDFFragment("SoftwareVersion.pdf"))
        }
        return super.onOptionsItemSelected(item)
    }
}