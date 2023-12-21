package com.utad.ideas.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityCreateBinding
import com.utad.ideas.databinding.FragmentIdeaDetailBinding
import com.utad.ideas.databinding.FragmentIdeasListBinding
import com.utad.ideas.room.model.Ideas
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

    private var historialIdeas: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtainDetailRelation(args.itemId)
        checkTime()
        checkPriority()
        binding.button.setOnClickListener { updateValue();navigateToBack() }
        binding.btnAddDetail.setOnClickListener { addDescription() }
    }

    private fun addDescription() {
        val description = binding.etDetailDescription.text.toString()
        if (!description.isNullOrEmpty()) {
            historialIdeas += "\n" + description + "\n"
            binding.tvAddDescription.text = historialIdeas
            binding.etDetailDescription.text.clear()
        } else {
            Toast.makeText(requireContext(), "Campo vacio", Toast.LENGTH_SHORT).show()
        }
    }


    private fun navigateToBack() {
        val action = IdeaDetailFragmentDirections.actionIdeaDetailFragmentToIdeasListFragment()
        findNavController().navigate(action)
    }

    private fun obtainDetailRelation(itemId: Int) {
        /** Accedo a la aplicacion a través de la activity y le hago un cast para que sea de tipo MyApplication*/
        val application: MyApplication = requireActivity().application as MyApplication

        lifecycleScope.launch(Dispatchers.IO) {

            val obj: Ideas = application.dataBase.ideasDao().getIdeaListDetail(itemId)
            if (obj != null) {
                withContext(Dispatchers.Main) {
                    binding.ivPhotoDetail.setImageBitmap(obj.image)
                    binding.tvDetailTitle.text = obj.ideaName
                    binding.tvDetailDescription.text = obj.description
                    if (!obj.detail.isNullOrEmpty()) {
                        binding.tvAddDescription.text = obj.detail
                        historialIdeas = obj.detail
                    }
                    selectDefect(obj.priority, obj.time)

                }
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
            "En progreso" -> binding.cbDetailProgreso.isChecked = true
            "Terminado" -> binding.cbDetailTerminado.isChecked = true
        }
    }

    private fun updateValue() {

        val application: MyApplication = requireActivity().application as MyApplication
        lifecycleScope.launch(Dispatchers.IO) {
            application.dataBase.ideasDao()
                .updateIdeaTimeAndPriority(args.itemId, timeValue, priorityValue)

        }

        lifecycleScope.launch(Dispatchers.IO) {
            application.dataBase.ideasDao()
                .updateDetail(args.itemId, historialIdeas)

        }
    }


    private fun checkTime() {
        binding.cbDetailPendiente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailProgreso.isChecked = false
                binding.cbDetailTerminado.isChecked = false
                timeValue = binding.cbDetailPendiente.text.toString()

            }
        }

        binding.cbDetailProgreso.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbDetailPendiente.isChecked = false
                binding.cbDetailTerminado.isChecked = false
                timeValue = binding.cbDetailProgreso.text.toString()

            }

            binding.cbDetailTerminado.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.cbDetailPendiente.isChecked = false
                    binding.cbDetailProgreso.isChecked = false
                    timeValue = binding.cbDetailTerminado.text.toString()

                }


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
