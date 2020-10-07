package com.example.timetable.ui.nagaoBus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.R

class NagaoBusFragment : Fragment() {

  private lateinit var nagaoBusViewModel: NagaoBusViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    nagaoBusViewModel =
    ViewModelProviders.of(this).get(NagaoBusViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_nagao_bus, container, false)

    return root
  }
}