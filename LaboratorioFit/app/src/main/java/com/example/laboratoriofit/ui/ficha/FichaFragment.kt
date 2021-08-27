package com.example.laboratoriofit.ui.ficha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laboratoriofit.R
import com.example.laboratoriofit.adapter.TreinoListAdapter
import com.example.laboratoriofit.data.ficha.TreinoRepository
import com.example.laboratoriofit.databinding.FragmentFichaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback as SimpleCallback1
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class FichaFragment : Fragment() {

    private val database : CollectionReference = FirebaseFirestore.getInstance().collection("User").document(
        Firebase.auth.currentUser?.uid.toString()).collection("Ficha")
    private var treinoRepository: TreinoRepository = TreinoRepository(database)
    private var viewModel : FichaViewModel = FichaViewModel(treinoRepository)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.addExerciseButton.setOnClickListener{
            val action = FichaFragmentDirections.actionNavigationFichaToAddTreinoFragment()
            findNavController().navigate(action)
        }

        binding.listaTreino.layoutManager = LinearLayoutManager(this.context)
        val adapter = TreinoListAdapter({ ref ->
            viewModel.concludeSerie(ref)
            val action = FichaFragmentDirections.actionNavigationFichaToAuxiliarFragment()
            findNavController().navigate(action)
        }){ val action = FichaFragmentDirections.actionNavigationFichaToAddTreinoFragment(getString(R.string.editar_treino), it.id)
            findNavController().navigate(action)
        }

        binding.listaTreino.adapter = adapter

        viewModel.allTreinos.observe(this.viewLifecycleOwner){ treinos ->
            adapter.submitList(treinos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            if (getFragmentManager() != null) {
                getFragmentManager()
                    ?.beginTransaction()
                    ?.detach(this)
                    ?.attach(this)
                    ?.commit();
            }
        }
    }

}