package com.utad.ideas.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utad.ideas.databinding.ItemIdeaListBinding
import com.utad.ideas.room.model.Ideas
import com.utad.ideas.ui.activities.IdeasActivity
import com.utad.ideas.ui.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdeasAdapter(
    val deleteItem: (item: Ideas) -> Unit,
    val goToDetail: (itemId: Int) -> Unit,
    val getIdeasFromDataBase: () -> Unit
) : ListAdapter<Ideas, IdeasAdapter.IdeaListViewHolder>(IdeasListItemCallBack) {

    // Implemento funcion del adaptador tengo que inflar la vista (visible) va crear y actualizar la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIdeaListBinding.inflate(inflater, parent, false)
        return IdeaListViewHolder(binding)

    }

    // Implemento funcion del adaptador asigna a cada item a un dato en concreto
    override fun onBindViewHolder(holder: IdeaListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvName.text = item.ideaName
        holder.binding.tvPlayedPosition.text = item.description
        holder.binding.tvListTime.text = item.time
        holder.binding.tvListPriority.text = item.priority
        setColors(item, holder)
        holder.binding.btnDeleteIdea.setOnClickListener { deleteItem(item);getIdeasFromDataBase() }
        holder.binding.root.setOnClickListener { goToDetail(item.id) }

    }


    private fun setColors(item: Ideas, holder: IdeaListViewHolder) {
        if (item.priority == "Baja") {
            holder.binding.tvListPriority.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#BDECB6"))
        } else if (item.priority == "Media") {
            holder.binding.tvListPriority.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FDFD96"))
        } else if (item.priority == "Alta") {
            holder.binding.tvListPriority.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FF6961"))
        }

        if (item.time == "Pendiente") {
            holder.binding.tvListTime.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FF6961"))
        } else if (item.time == "En progreso") {
            holder.binding.tvListTime.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FDFD96"))
        } else if (item.time == "Terminado") {
            holder.binding.tvListTime.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#BDECB6"))
        }
    }

    // Creo la clase interna que va a tener el viewHolder que va  recibir el binding del item.xml (layout)
    // Devuelve el recyclerView binding (la raiz de la vista que se esta viendo)
    inner class IdeaListViewHolder(val binding: ItemIdeaListBinding) :
        RecyclerView.ViewHolder(binding.root)


    // Creo un objeto que extiende de diffutil , va a comparar la informacion de dentro de la lista, si son iguales o cambia el contenido
    // le paso el objeto a comparar <AQUI>, tengo que implementar los miembros
    object IdeasListItemCallBack : DiffUtil.ItemCallback<Ideas>() {

        // Funcion que comprueba si los elementos son iguales
        override fun areItemsTheSame(oldItem: Ideas, newItem: Ideas): Boolean {
            return oldItem.id == newItem.id
        }

        // Funcion que comprueba si el contenido de los elementos cambio
        override fun areContentsTheSame(oldItem: Ideas, newItem: Ideas): Boolean {
            return oldItem.ideaName == newItem.ideaName
        }
    }


}