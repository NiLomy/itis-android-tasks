package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.DialogBottomSheetFragmentDetailedFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator

class DetailedFilmBottomSheetDialogFragment : BottomSheetDialogFragment(R.layout.dialog_bottom_sheet_fragment_detailed_film) {
    private val viewBinding: DialogBottomSheetFragmentDetailedFilmBinding by viewBinding(
        DialogBottomSheetFragmentDetailedFilmBinding::bind
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_bottom_sheet_fragment_detailed_film, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateViewDialogHeight()
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            markFavouriteBtn.setOnClickListener {
                val currentUserEmail: String? = ServiceLocator.getSharedPreferences().getString("userEmail", "")
                if (!currentUserEmail.isNullOrEmpty()) {
//                    UserRepository.saveUserFilmCrossRef(currentUserEmail, )
                }
            }
        }
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