package com.example.laboratoriofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laboratoriofit.data.ficha.Treino
import com.example.laboratoriofit.databinding.RefeicaoListBinding

class TreinoListAdapter(private val onTreinoClick: (Treino) -> Unit, private val onTreinoLongClick: (Treino) -> Unit) :
    ListAdapter<Treino, TreinoListAdapter.TreinoViewHolder>(TreinoListAdapter.DiffCallback)  {
    class TreinoViewHolder(private var binding: RefeicaoListBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(treino: Treino){
            binding.apply{
                binding.titleHorario.text = treino.nome.trim()

                val textDesc = TextView(root.context)
                textDesc.text = "${treino.serieAtual}/${treino.serie} séries"
                binding.listRef.addView(textDesc)

                val textDesc2 = TextView(root.context)
                textDesc2.text = "${treino.repeticao} repetições"
                binding.listRef.addView(textDesc2)
            }
        }
    }

    companion object{
        private val DiffCallback = object: DiffUtil.ItemCallback<Treino>(){
            override fun areItemsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Treino, newItem: Treino): Boolean {
                return oldItem.nome == newItem.nome
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreinoViewHolder {
        val lp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        val bind = RefeicaoListBinding.inflate(LayoutInflater.from(parent.context))
        bind.root.layoutParams = lp
        return TreinoViewHolder(bind)
    }

    override fun onBindViewHolder(holder: TreinoViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onTreinoClick(current)
        }
        holder.itemView.setOnLongClickListener {
            onTreinoLongClick(current)
            true
        }
        holder.bind(current)
    }
}