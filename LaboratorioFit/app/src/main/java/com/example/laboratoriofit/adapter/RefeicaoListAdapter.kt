package com.example.laboratoriofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laboratoriofit.data.dieta.Refeicao
import com.example.laboratoriofit.databinding.RefeicaoListBinding

class RefeicaoListAdapter(private val onRefeicaoClick: (Refeicao) -> Unit, private val onRefeicaoLongClick: (Refeicao) -> Unit) :
    ListAdapter<Refeicao, RefeicaoListAdapter.RefeicaoViewHolder>(DiffCallback) {
        class RefeicaoViewHolder(private var binding: RefeicaoListBinding)
            : RecyclerView.ViewHolder(binding.root){
                fun bind(refeicao: Refeicao){
                    binding.apply{
                        val refeicoes = refeicao.descricao.split(",").toTypedArray()
                        val m = refeicao.horario % 100
                        val minuto = m.toString().padStart(2, '0')
                        val h = refeicao.horario / 100
                        val hora = h.toString().padStart(2, '0')
                        binding.titleHorario.text = "${hora}:${minuto}"
                        for(ref in refeicoes){
                            val textDesc = TextView(root.context)
                            textDesc.text = ref.trim()
                            binding.listRef.addView(textDesc)
                        }
                    }
                }
            }

        companion object{
            private val DiffCallback = object: DiffUtil.ItemCallback<Refeicao>(){
                override fun areItemsTheSame(oldItem: Refeicao, newItem: Refeicao): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: Refeicao, newItem: Refeicao): Boolean {
                    return oldItem.descricao == newItem.descricao
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefeicaoViewHolder {
        val lp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        val bind = RefeicaoListBinding.inflate(LayoutInflater.from(parent.context))
        bind.root.layoutParams = lp
        return RefeicaoViewHolder(bind)
        /*return RefeicaoViewHolder(
            RefeicaoListBinding.inflate(LayoutInflater.from(parent.context))
        )*/
    }

    override fun onBindViewHolder(holder: RefeicaoViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{onRefeicaoClick(current)}
        holder.itemView.setOnLongClickListener {
            onRefeicaoLongClick(current)
            true
        }
        holder.bind(current)
    }
}