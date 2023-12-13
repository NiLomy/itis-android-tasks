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
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.FIlmRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.RatingRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRatingModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.DialogBottomSheetFragmentDetailedFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.toFilmModel
import java.math.RoundingMode
import java.sql.Date
import java.text.DecimalFormat

class DetailedFilmBottomSheetDialogFragment(
    private val filmModel: FilmRVModel,
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
        return inflater.inflate(R.layout.dialog_bottom_sheet_fragment_detailed_film, container, false)
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
            if (filmModel.isFavoured) {
                favouriteIv.setImageResource(R.drawable.ic_favorite)
            } else {
                favouriteIv.setImageResource(R.drawable.ic_unfavorite)
            }
            val currentUserEmail: String? = ServiceLocator.getSharedPreferences().getString("userEmail", "")

            if (!currentUserEmail.isNullOrEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val isFilmFavourite = UserRepository.isFilmFavourite(currentUserEmail, filmModel.name, filmModel.date)
                    if (isFilmFavourite) {
                        markFavouriteBtn.text = "Unfavourite"
                    } else {
                        markFavouriteBtn.text = "Favourite"
                    }
                }

                markFavouriteBtn.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val isFilmFavourite = UserRepository.isFilmFavourite(currentUserEmail, filmModel.name, filmModel.date)
                        if (isFilmFavourite) {
                            UserRepository.deleteUserFilmCrossRef(currentUserEmail, filmModel.toFilmModel())
                            activity?.runOnUiThread {
                                filmAdapter.removeAt(filmModel.id - 1)
                                markFavouriteBtn.text = "Favourite"
                            }
                        } else {
                            UserRepository.saveUserFilmCrossRef(currentUserEmail, filmModel.toFilmModel())
                            activity?.runOnUiThread {
                                filmModel.isFavoured = true
                                filmAdapter.addItem(filmModel)
                                markFavouriteBtn.text = "Unfavourite"
                            }
                        }
                    }
                }
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val rating: Double? = RatingRepository.getAllFilmRating(filmModel.toFilmModel())
                if (rating != null) {
                    activity?.runOnUiThread {
                        if (rating.isNaN()) {
                            ratingTv.text = "There are no ratings"
                        } else {
                            val s = rating.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                            ratingTv.text = "Rating: " + rating.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                        }
                    }
                }
                if (currentUserEmail != null) {
                    val markedRating = RatingRepository.getFilmRating(filmModel.toFilmModel(), currentUserEmail)
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
                        RatingRepository.save(FilmRatingModel(filmModel.name, filmModel.date, currentUserEmail, rating.toDouble()))
                        val resultRating: Double? = RatingRepository.getAllFilmRating(filmModel.toFilmModel())
                        if (resultRating != null) {
                            activity?.runOnUiThread {
                                ratingTv.text = "Rating: " + resultRating.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun calculateViewDialogHeight() {
        val displayMetrics = requireContext().resources.displayMetrics
        val dialogHeight = displayMetrics.heightPixels / 3

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
