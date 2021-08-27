package com.example.laboratoriofit.ui.dieta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboratoriofit.MainActivity
import com.example.laboratoriofit.R
import com.example.laboratoriofit.adapter.RefeicaoListAdapter
import com.example.laboratoriofit.data.dieta.RefeicaoRepository
//import com.example.laboratoriofit.applications.DietaApplication
import com.example.laboratoriofit.databinding.FragmentDietaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DietaFragment : Fragment() {

    private val database : CollectionReference = FirebaseFirestore.getInstance().collection("User").document(
        Firebase.auth.currentUser?.uid.toString()).collection("Dieta")
    private var refeicaoRepository: RefeicaoRepository = RefeicaoRepository(database)
    private var viewModel : DietaViewModel = DietaViewModel(refeicaoRepository)

    private var _binding: FragmentDietaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDietaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.mealsList.layoutManager = LinearLayoutManager(this.context)
        binding.addMealButton.setOnClickListener{
            val action = DietaFragmentDirections.actionNavigationDietaToAddRefeicaoFragment("", "")
            findNavController().navigate(action)
        }

        val adapter = RefeicaoListAdapter({ ref ->
                val docData = hashMapOf(
                    "checked" to !(ref.checked)
                )
            viewModel.updateRefeicao(ref.id, docData)
            val action = DietaFragmentDirections.actionNavigationDietaToAuxiliarFragment()
            findNavController().navigate(action)
        }){ val action = DietaFragmentDirections.actionNavigationDietaToAddRefeicaoFragment(getString(R.string.edit_fragment_title), it.id)
            findNavController().navigate(action)
        }
        viewModel.allRefeicoes.observe(this.viewLifecycleOwner){refeicoes ->
            adapter.submitList(refeicoes)
        }
        binding.mealsList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}