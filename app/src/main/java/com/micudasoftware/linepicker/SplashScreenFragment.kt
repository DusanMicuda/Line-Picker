package com.micudasoftware.linepicker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController

class SplashScreenFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.window?.statusBarColor = Color.parseColor("#FFF5A0")

        val viewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]
        viewModel.startTimer()
        viewModel.canContinue.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.isFirstStart)
                    view?.findNavController()?.navigate(
                        SplashScreenFragmentDirections
                            .actionSplashScreenFragmentToPDFFragment("LicenseAgreement.pdf", true))
                else
                    view?.findNavController()?.navigate(
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToImportFragment())
            }
        })

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}