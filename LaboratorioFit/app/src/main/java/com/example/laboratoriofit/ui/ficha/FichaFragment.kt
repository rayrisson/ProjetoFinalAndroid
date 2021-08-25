package com.example.laboratoriofit.ui.ficha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.laboratoriofit.databinding.FragmentFichaBinding
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback as SimpleCallback1

class FichaFragment : Fragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentFichaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFichaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}