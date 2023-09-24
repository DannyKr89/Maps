package ru.dk.maps.ui.edit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.dk.maps.databinding.FragmentEditMarkerDialogBinding

class EditMarkerDialogFragment : DialogFragment() {

    private var _binding: FragmentEditMarkerDialogBinding? = null
    private val binding get() = _binding!!

    var listener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMarkerDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val title = arguments?.getString("edit") ?: ""

        binding.apply {
            etMarkerTitleEdit.setText(title)

            btnEdit.setOnClickListener {
                applyChanges()
            }
        }

    }

    private fun applyChanges() {
        listener?.invoke(binding.etMarkerTitleEdit.text.toString())
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroy() {
        listener = null
        _binding = null
        super.onDestroy()
    }
}