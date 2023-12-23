package com.utad.ideas.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityCreateBinding
import com.utad.ideas.databinding.FragmentIdeaDetailBinding
import com.utad.ideas.databinding.FragmentIdeasListBinding
import com.utad.ideas.room.DetalleDao
import com.utad.ideas.room.model.Detail
import com.utad.ideas.room.model.Ideas
import com.utad.ideas.ui.adapters.IdeasAdapter
import com.utad.ideas.ui.application.MyApplication
import com.utad.ideas.ui.fragment.IdeasListFragmentDirections.ActionIdeasListFragmentToIdeaDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class IdeaDetailFragment : Fragment() {

    private lateinit var _binding: FragmentIdeaDetailBinding
    private val binding: FragmentIdeaDetailBinding get() = _binding

    private val args: IdeaDetailFragmentArgs by navArgs()

    private lateinit var timeValue: String
    private lateinit var priorityValue: String
    private lateinit var detailDao: DetalleDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailDao = (requireActivity().application as MyApplication).dataBase.detailDao()
        obtainDetailRelation(args.itemId)
        checkTime()
        checkPriority()
        binding.button.setOnClickListener { updateValue();navigateToBack() }
        binding.btnAddDetail.setOnClickListener { addDescription() }
    }

    private fun addDescription() {
        val description = binding.etDetailDescription.text.toString()

        if (!description.isNullOrEmpty()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val detail = Detail(0, args.itemId, description)
                detailDao.insertDetail(detail)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Añadido", Toast.LENGTH_SHORT).show()
                }
            }
            obtainDetailRelation(args.itemId)
            binding.etDetailDescription.text.clear()
        } else {
            Toast.makeText(requireContext(), "Campo vacío", Toast.LENGTH_SHORT).show()
        }
    }


    private fun navigateToBack() {
        val action = IdeaDetailFragmentDirections.actionIdeaDetailFragmentToIdeasListFragment()
        findNavController().navigate(action)
    }

    private fun obtainDetailRelation(itemId: Int) {
        val application: MyApplication = requireActivity().application as MyApplication

        lifecycleScope.launch(Dispatchers.IO) {
            val obj: Ideas = application.dataBase.ideasDao().getIdeaListDetail(itemId)
            val det: List<Detail> = application.dataBase.detailDao().getDetailsByIdeaId(itemId)

            if (obj != null) {
                withContext(Dispatchers.Main) {
                    binding.ivPhotoDetail.setImageBitmap(obj.image)
                    binding.tvDetailTitle.text = obj.ideaName
                    binding.tvDetailDescription.text = obj.description
                    selectDefect(obj.priority, obj.time)

                    val detailsText = det.joinToString("\n\n") { it.detailText!! }
                    binding.tvAddDescription.text = detailsText


                    // Crear una variable para almacenar el detalle antes de la lambda
                    var detailToDelete: Detail? = null

                    binding.tvAddDescription.setOnLongClickListener {
                        // Asignar el detalle a la variable antes de la lambda
                        detailToDelete = det.firstOrNull()

                        lifecycleScope.launch(Dispatchers.IO) {
                            // Verificar si hay un detalle para eliminar
                            detailToDelete?.let { detail ->
                                detailDao.deleteDetail(detail)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Ha sido eliminado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        // Actualizar la vista después de eliminar el detalle
                        obtainDetailRelation(args.itemId)
                        true
                    }
                }
            }
        }
    }


    private fun updateValue() {

        val application: MyApplication = requireActivity().application as MyApplication
        lifecycleScope.launch(Dispatchers.IO) {
            application.dataBase.ideasDao()
                .updateIdeaTimeAndPriority(args.itemId, timeValue, priorityValue)

        }

        lifecycleScope.launch(Dispatchers.IO) {
            // Actualiza los detalles en la tabla Detail
            val details = detailDao.getDetailsByIdeaId(args.itemId)
            for (detail in details) {
                detailDao.updateDetail(detail)
            }
        }
    }

    private fun selectDefect(priority: String, time: String) {
        when (priority) {
            "Baja" -> binding.cbDetailBaja.isChecked = true
            "Media" -> binding.cbDetailMedia.isChecked = true
            "Alta" -> binding.cbDetailAlta.isChecked = true
        }

        when (time) {
            "Pendiente" -> binding.cbDetailPendiente.isChecked = true
            "En proceso" -> binding.cbDetailProceso.isChecked = true
            "Terminado" -> binding.cbDetailTerminado.isChecked = true
        }
    }

    private fun checkTime() {
        binding.cbDetailPendiente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailProceso.isChecked = false
                binding.cbDetailTerminado.isChecked = false
                timeValue = binding.cbDetailPendiente.text.toString()
            }
        }

        binding.cbDetailProceso.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailPendiente.isChecked = false
                binding.cbDetailTerminado.isChecked = false
                timeValue = binding.cbDetailProceso.text.toString()
            }
        }

        binding.cbDetailTerminado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailPendiente.isChecked = false
                binding.cbDetailProceso.isChecked = false
                timeValue = binding.cbDetailTerminado.text.toString()

            }

        }
    }

    private fun checkPriority() {
        binding.cbDetailBaja.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailMedia.isChecked = false
                binding.cbDetailAlta.isChecked = false
                priorityValue = binding.cbDetailBaja.text.toString()
            }
        }

        binding.cbDetailMedia.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailBaja.isChecked = false
                binding.cbDetailAlta.isChecked = false
                priorityValue = binding.cbDetailMedia.text.toString()
            }
        }

        binding.cbDetailAlta.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailBaja.isChecked = false
                binding.cbDetailMedia.isChecked = false
                priorityValue = binding.cbDetailAlta.text.toString()
            }

        }
    }
}