package com.example.laboratoriofit.data.ficha

import com.google.firebase.firestore.CollectionReference

class TreinoRepository(private val db: CollectionReference) {
    fun getTreinos(callback: (List<Treino>) -> Unit){
        db.get().addOnSuccessListener { documents ->
            val treinos = mutableListOf<Treino>()
            for(document in documents){
                val treino = Treino(
                    id = document.getString("id")!!,
                    nome = document.getString("nome")!!,
                    repeticao = document.getDouble("repeticao")!!.toInt(),
                    serie = document.getDouble("serie")!!.toInt(),
                    serieAtual = document.getDouble("serieAtual")!!.toInt()
                )
                treinos.add(treino)
            }
            callback(treinos)
        }
    }

    fun getTreino(id: String, callback: (Treino) -> Unit){
        db.document(id).get().addOnSuccessListener { document ->
            val treino = Treino(
                id = document.getString("id")!!,
                nome = document.getString("nome")!!,
                repeticao = document.getDouble("repeticao")!!.toInt(),
                serie = document.getDouble("serie")!!.toInt(),
                serieAtual = document.getDouble("serieAtual")!!.toInt()
            )
            callback(treino)
        }
    }

    fun insert(treino: Treino){
        db.add(treino).addOnSuccessListener { document ->
            val id: String = document.id
            db.document(id).update("id", id)
        }
    }

    fun delete(id: String){
        db.document(id).delete()
    }

    fun update(id: String, map: Map<String, Any>){
        db.document(id).update(map)
    }
}