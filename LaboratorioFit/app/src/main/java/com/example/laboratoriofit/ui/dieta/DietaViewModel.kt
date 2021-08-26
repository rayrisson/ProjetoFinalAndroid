package com.example.laboratoriofit.ui.dieta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laboratoriofit.data.dieta.Dieta
import com.example.laboratoriofit.data.dieta.RefeicaoRepository

class DietaViewModel(private val refeicaoRepository: RefeicaoRepository) : ViewModel() {

    var _allRefeicoes = MutableLiveData<List<Dieta>>()
    val allRefeicoes: LiveData<List<Dieta>>
    get(){
        refeicaoRepository.getRefeicoes{_allRefeicoes.value = it}
        return _allRefeicoes
    }

    var _Ref = MutableLiveData<Dieta>()

    private fun insertRefeicao(refeicao: Dieta){
        refeicaoRepository.insert(refeicao)
    }

    private fun getNewRefeicao(refeicaoDesc: String, refeicaoHora: String): Dieta{
        var formattertime = refeicaoHora.replace(":", "")
        return Dieta(
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

    fun retrieveItem(id: String): LiveData<Dieta>{
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