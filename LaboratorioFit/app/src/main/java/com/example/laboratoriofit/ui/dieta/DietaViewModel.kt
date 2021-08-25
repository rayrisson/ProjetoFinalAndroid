package com.example.laboratoriofit.ui.dieta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.laboratoriofit.data.dieta.Refeicao
import com.example.laboratoriofit.data.dieta.RefeicaoRepository
import kotlinx.coroutines.launch

class DietaViewModel(private val refeicaoRepository: RefeicaoRepository) : ViewModel() {

    var _allRefeicoes = MutableLiveData<List<Refeicao>>()
    val allRefeicoes: LiveData<List<Refeicao>>
    get(){
        refeicaoRepository.getRefeicoes{_allRefeicoes.value = it}
        return _allRefeicoes
    }

    var _Ref = MutableLiveData<Refeicao>()

    private fun insertRefeicao(refeicao: Refeicao){
        refeicaoRepository.insert(refeicao)
    }

    private fun getNewRefeicao(refeicaoDesc: String, refeicaoHora: String): Refeicao{
        var formattertime = refeicaoHora.replace(":", "")
        return Refeicao(
            descricao = refeicaoDesc,
            horario = formattertime.toInt()
            )
    }

    fun addNewRefeicao(refeicaoDesc: String, refeicaoHora: String){
        val newRefeicao = getNewRefeicao(refeicaoDesc, refeicaoHora)
        insertRefeicao(newRefeicao)
    }

    fun isEntryValid(refeicaoDesc: String, refeicaoHora: String): Boolean{
        return (refeicaoDesc.isNotBlank() && refeicaoHora.isNotBlank())
    }

    fun retrieveItem(id: String): LiveData<Refeicao>{
        refeicaoRepository.getRefeicao(id) {_Ref.value = it}
        return _Ref
    }

    fun deleteRefeicao(id: String){
        refeicaoRepository.delete(id)
    }

    fun updateRefeicao(id: String, map: Map<String, Any>){
        refeicaoRepository.update(id, map)
    }
}