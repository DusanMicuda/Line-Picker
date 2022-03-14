package com.micudasoftware.linepicker.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.ui.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        activity?.window?.statusBarColor = Color.parseColor("#FFF5A0")

        val viewModel : SplashScreenViewModel  by viewModels()
        viewModel.startTimer()
        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it) {
                if (viewModel.isFirstStart)
                    view?.findNavController()?.navigate(
                        SplashScreenFragmentDirections
                            .actionSplashScreenFragmentToPDFFragment("LicenseAgreement.pdf", true)
                    )
                else
                    view?.findNavController()?.navigate(
                        SplashScreenFragmentDirections.actionSplashScreenFragmentToImportFragment()
                    )
            }
        }

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }
}