package com.example.timetable.ui.license

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.R

class LicenseFragment : Fragment() {

  private lateinit var licenseViewModel: LicenseViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    licenseViewModel =
    ViewModelProviders.of(this).get(LicenseViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_license, container, false)

    return root
  }
}