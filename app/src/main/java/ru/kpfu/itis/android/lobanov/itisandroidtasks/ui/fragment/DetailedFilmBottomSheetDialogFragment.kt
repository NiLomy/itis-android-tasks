package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.FilmAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.RatingRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRatingModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.DialogBottomSheetFragmentDetailedFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmModel
import java.math.RoundingMode

class DetailedFilmBottomSheetDialogFragment(
    private val filmModel: FilmCatalog.FilmRVModel,
    private val filmAdapter: FilmAdapter
) : BottomSheetDialogFragment(R.layout.dialog_bottom_sheet_fragment_detailed_film) {
    private val viewBinding: DialogBottomSheetFragmentDetailedFilmBinding by viewBinding(
        DialogBottomSheetFragmentDetailedFilmBinding::bind
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_bottom_sheet_fragment_detailed_film,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateViewDialogHeight()
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            filmNameTv.text = filmModel.name
            filmDateTv.text = filmModel.date.toString()
            filmDescriptionTv.text = filmModel.description
            val currentUserEmail: String? =
                ServiceLocator.getSharedPreferences().getString(ParamsConstants.EMAIL_SP_TAG, "")

            if (!currentUserEmail.isNullOrEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val isFilmFavourite = UserRepository.isFilmFavourite(
                        currentUserEmail,
                        filmModel.name,
                        filmModel.date
                    )
                    if (isFilmFavourite) {
                        markFavouriteBtn.text = getString(R.string.unfavorite)
                    } else {
                        markFavouriteBtn.text = getString(R.string.favorite)
                    }
                }

                markFavouriteBtn.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val isFilmFavourite = UserRepository.isFilmFavourite(
                            currentUserEmail,
                            filmModel.name,
                            filmModel.date
                        )
                        if (isFilmFavourite) {
                            UserRepository.deleteUserFilmCrossRef(
                                currentUserEmail,
                                filmModel.toFilmModel()
                            )
                            activity?.runOnUiThread {
                                filmAdapter.remove(filmModel)
                                markFavouriteBtn.text = getString(R.string.favorite)
                            }
                        } else {
                            UserRepository.saveUserFilmCrossRef(
                                currentUserEmail,
                                filmModel.toFilmModel()
                            )
                            activity?.runOnUiThread {
                                filmModel.isFavoured = true
                                filmAdapter.addItem(filmAdapter.itemCount, filmModel)
                                markFavouriteBtn.text = getString(R.string.unfavorite)
                            }
                        }
                    }
                }
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val rating: Double? = RatingRepository.getAllFilmRating(filmModel.toFilmModel())
                if (rating != null && !rating.isNaN()) {
                    activity?.runOnUiThread {
                        ratingTv.text = getString(
                            R.string.rating_display,
                            rating.toBigDecimal().setScale(1, RoundingMode.UP)
                                .toDouble().toString()
                        )
                    }
                } else {
                    ratingTv.text = getString(R.string.no_ratings)
                }
                if (currentUserEmail != null) {
                    val markedRating =
                        RatingRepository.getFilmRating(filmModel.toFilmModel(), currentUserEmail)
                    if (markedRating != null && !markedRating.isNaN()) {
                        activity?.runOnUiThread {
                            filmRb.rating = markedRating.toFloat()
                        }
                    }
                }
            }

            filmRb.setOnRatingBarChangeListener { _, rating, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    if (!currentUserEmail.isNullOrEmpty()) {
                        RatingRepository.save(
                            FilmRatingModel(
                                filmModel.name,
                                filmModel.date,
                                currentUserEmail,
                                rating.toDouble()
                            )
                        )
                        val resultRating: Double? =
                            RatingRepository.getAllFilmRating(filmModel.toFilmModel())
                        if (resultRating != null) {
                            activity?.runOnUiThread {
                                ratingTv.text = getString(
                                    R.string.rating_display, resultRating.toBigDecimal()
                                        .setScale(1, RoundingMode.UP).toDouble().toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun calculateViewDialogHeight() {
        val displayMetrics = requireContext().resources.displayMetrics
        val dialogHeight = displayMetrics.heightPixels

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
