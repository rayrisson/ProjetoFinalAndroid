package com.example.laboratoriofit.ui.ficha

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laboratoriofit.data.ficha.Treino
import com.example.laboratoriofit.data.ficha.TreinoRepository

class FichaViewModel(private val treinoRepository: TreinoRepository) : ViewModel() {

    var _allTreinos = MutableLiveData<List<Treino>>()
    val allTreinos: LiveData<List<Treino>>
        get(){
            treinoRepository.getTreinos{_allTreinos.value = it}
            return _allTreinos
        }

    var _Treino = MutableLiveData<Treino>()

    private fun insertTreino(treino: Treino){
        treinoRepository.insert(treino)
    }

    private fun getNewTreino(treinoNome: String, treinoSerie: String, treinoRepeticao: String): Treino {
        return Treino(
            nome = treinoNome,
            serie = treinoSerie.toInt(),
            repeticao = treinoRepeticao.toInt()
        )
    }

    fun addNewTreino(treinoNome: String, treinoSerie: String, treinoRepeticao: String){
        val newTreino = getNewTreino(treinoNome, treinoSerie, treinoRepeticao)
        insertTreino(newTreino)
    }

    fun isEntryValid(treinoNome: String, treinoSerie: String, treinoRepeticao: String, treinoSerieAtual: String = "1"): Boolean{
        return (treinoNome.isNotBlank() && treinoSerie.isNotBlank() && treinoSerie.toInt() > 0 && treinoRepeticao.isNotBlank() && treinoRepeticao.toInt() > 0 && treinoSerieAtual.isNotBlank() && treinoSerieAtual.toInt() >= 0 && treinoSerieAtual.toInt() <= treinoSerie.toInt())
    }

    fun retrieveItem(id: String): LiveData<Treino>{
        treinoRepository.getTreino(id) {_Treino.value = it}
        return _Treino
    }

    fun deleteTreino(id: String){
        treinoRepository.delete(id)
    }

    fun updateTreino(id: String, map: Map<String, Any>){
        treinoRepository.update(id, map)
    }

    fun concludeSerie(treino: Treino){
        if(treino.serieAtual + 1 <= treino.serie) {
            val docData = hashMapOf(
                "serieAtual" to treino.serieAtual + 1
            )
            updateTreino(treino.id, docData)
        }
    }
}