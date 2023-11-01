package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.PlanetAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.DialogBottomSheetFragmentBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PlanetsDataRepository

class BottomSheetDialogFragment(private val adapter: PlanetAdapter?) :
    BottomSheetDialogFragment(R.layout.dialog_bottom_sheet_fragment) {

    private val viewBinding: DialogBottomSheetFragmentBinding by viewBinding(
        DialogBottomSheetFragmentBinding::bind
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateViewDialogHeight()
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            dialogEtPagesToAdd.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = dialogEtPagesToAdd.text.toString()
                    if (input.isNotEmpty()) {
                        val newsCount = input.toInt()
                        if (newsCount > 5) {
                            dialogEtPagesToAdd.removeTextChangedListener(this)
                            dialogEtPagesToAdd.setText(input.substring(0, input.length - 1))
                            dialogEtPagesToAdd.text?.let { dialogEtPagesToAdd.setSelection(it.length) }
                            dialogEtPagesToAdd.addTextChangedListener(this)

                            Toast.makeText(
                                context,
                                getString(R.string.you_should_enter_a_number_between_0_and_5),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            dialogBtn.setOnClickListener {
                val planetCount = dialogEtPagesToAdd.text.toString()
                if (isValidInput(planetCount)) {
                    PlanetsDataRepository.addRandomPlanets(planetCount.toInt())
                    adapter?.setItems(PlanetsDataRepository.getAllPlanets())
                    dismiss()
                } else Toast.makeText(
                    context,
                    getString(R.string.you_should_enter_a_number_between_0_and_5),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidInput(input: String): Boolean {
        if (input.isEmpty()) return false
        val planetCount = input.toInt()
        if (planetCount > 5) return false
        return true
    }

    private fun calculateViewDialogHeight() {
        val displayMetrics = requireContext().resources.displayMetrics
        val dialogHeight = displayMetrics.heightPixels / 10

        val layoutParams =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
                .apply {
                    height = dialogHeight
                }

        this.viewBinding.root.layoutParams = layoutParams
    }

    companion object {
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }
}
