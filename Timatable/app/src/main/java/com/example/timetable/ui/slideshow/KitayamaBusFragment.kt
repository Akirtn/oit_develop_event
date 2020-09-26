package com.example.timetable.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.R

class KitayamaBusFragment : Fragment() {

  private lateinit var kitayamaBusViewModel: KitayamaBusViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    kitayamaBusViewModel =
    ViewModelProviders.of(this).get(KitayamaBusViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_kitayama_bus, container, false)

    return root
  }
}