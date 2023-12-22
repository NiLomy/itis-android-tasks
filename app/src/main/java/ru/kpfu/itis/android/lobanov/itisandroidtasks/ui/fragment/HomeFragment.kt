package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.FilmAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.decorations.HorizontalMarginDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.decorations.VerticalMarginDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.FIlmRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.UserRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmCatalog
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentHomeBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.di.ServiceLocator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsConstants
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.getValueInPx

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!
    private var filmAdapter: FilmAdapter? = null
    private var favouritesAdapter: FilmAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)?.visibility =
            View.VISIBLE
        initRecyclerView()
        initViews()
    }

    private fun initRecyclerView() {
        filmAdapter = FilmAdapter(
            onFilmClicked = ::onFilmClicked,
            favoritesRV = null
        )
        favouritesAdapter = FilmAdapter(
            onFilmClicked = ::onFavouriteFilmClicked,
            favoritesRV = viewBinding.favouritesRv
        )

        with(viewBinding) {
            val favouritesLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val filmLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            favouritesRv.layoutManager = favouritesLayoutManager
            favouritesRv.adapter = favouritesAdapter

            filmsRv.layoutManager = filmLayoutManager
            filmsRv.adapter = filmAdapter

            val touchHelper = ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        val position = viewHolder.adapterPosition
                        removeElement(position)
                    }
                }

                override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                    return SWIPE_THRESHOLD
                }
            })

            touchHelper.attachToRecyclerView(filmsRv)

            setDecorations(filmsRv)
            showAllFavourites(favouritesAdapter)
            showAllFilmsDateDesc(filmAdapter, noFilmsTv)
        }
    }

    private fun initViews() {
        with(viewBinding) {
            val orderTypes: Array<String> = resources.getStringArray(R.array.ordering_types)
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter(requireContext(), R.layout.item_dropdown, orderTypes)
            dropdownTv.setAdapter(arrayAdapter)

            dropdownTv.setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        showAllFilmsDateDesc(filmAdapter, noFilmsTv)
                    }

                    1 -> {
                        showAllFilmsDateAsc(filmAdapter, noFilmsTv)
                    }

                    2 -> {
                        showAllFilmsRatingDesc(filmAdapter, noFilmsTv)
                    }

                    3 -> {
                        showAllFilmsRatingAsc(filmAdapter, noFilmsTv)
                    }
                }
            }
        }
    }

    private fun showAllFavourites(filmAdapter: FilmAdapter?) {
        val currentUserEmail: String? =
            ServiceLocator.getSharedPreferences().getString(ParamsConstants.EMAIL_SP_TAG, "")
        if (!currentUserEmail.isNullOrEmpty()) {
            lifecycleScope.async(Dispatchers.IO) {
                val result: MutableList<FilmCatalog.FilmRVModel> = ArrayList()
                val films: List<FilmModel> = UserRepository.getAllFavourites(currentUserEmail)
                for (i: Int in films.indices) {
                    val f = films[i]
                    result.add(FilmCatalog.FilmRVModel(i, f.name, f.date, f.description, true))
                }
                activity?.runOnUiThread {
                    // I don't know why but if I use here viewBinding I face NPE
                    if (result.isEmpty()) _viewBinding?.favouritesRv?.visibility = View.GONE
                    filmAdapter?.setItems(result)
                }
            }
        }
    }

    private fun showAllFilmsDateDesc(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmCatalog.FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAllByDateDesc()
            if (films.isNullOrEmpty()) {
                activity?.runOnUiThread {
                    noFilmsTv.visibility = View.VISIBLE
                }
            } else {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmCatalog.FilmRVModel(i, f.name, f.date, f.description, false))
                }
                activity?.runOnUiThread {
                    filmAdapter?.setItems(result)
                    noFilmsTv.visibility = View.GONE
                }
            }
        }
    }

    private fun showAllFilmsRatingDesc(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmCatalog.FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAllByRatingDesc()
            if (!films.isNullOrEmpty()) {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmCatalog.FilmRVModel(i, f.name, f.date, f.description, false))
                }
                activity?.runOnUiThread {
                    filmAdapter?.setItems(result)
                    noFilmsTv.visibility = View.GONE
                }
            } else {
                activity?.runOnUiThread {
                    noFilmsTv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showAllFilmsRatingAsc(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmCatalog.FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAllByRatingAsc()
            if (!films.isNullOrEmpty()) {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmCatalog.FilmRVModel(i, f.name, f.date, f.description, false))
                }
                activity?.runOnUiThread {
                    filmAdapter?.setItems(result)
                    noFilmsTv.visibility = View.GONE
                }
            } else {
                activity?.runOnUiThread {
                    noFilmsTv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showAllFilmsDateAsc(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmCatalog.FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAllByDateAsc()
            if (!films.isNullOrEmpty()) {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmCatalog.FilmRVModel(i, f.name, f.date, f.description, false))
                }
                activity?.runOnUiThread {
                    filmAdapter?.setItems(result)
                    noFilmsTv.visibility = View.GONE
                }
            } else {
                activity?.runOnUiThread {
                    noFilmsTv.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setDecorations(rvFilms: RecyclerView) {
        val marginValue = MARGIN_WIDTH_DP.getValueInPx(resources.displayMetrics)
        rvFilms.addItemDecoration(HorizontalMarginDecorator(itemOffset = marginValue))
        rvFilms.addItemDecoration(VerticalMarginDecorator(itemOffset = marginValue / HEIGHT_DIVIDER))
    }

    private fun onFilmClicked(filmModel: FilmCatalog.FilmRVModel) {
        addFadeTransition()

        val bottomSheet = favouritesAdapter?.let {
            DetailedFilmBottomSheetDialogFragment(filmModel, it)
        }
        bottomSheet?.show(
            requireActivity().supportFragmentManager,
            DetailedFilmBottomSheetDialogFragment.DIALOG_FRAGMENT_TAG
        )
    }

    private fun onFavouriteFilmClicked(filmModel: FilmCatalog.FilmRVModel) {
        addFadeTransition()

        val bottomSheet = favouritesAdapter?.let {
            DetailedFilmBottomSheetDialogFragment(filmModel, it)
        }
        bottomSheet?.show(
            requireActivity().supportFragmentManager,
            DetailedFilmBottomSheetDialogFragment.DIALOG_FRAGMENT_TAG
        )
    }

    private fun addFadeTransition() {
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        enterTransition = fade
        exitTransition = fade
    }

    private fun removeElement(position: Int) {
        val film = filmAdapter?.get(position)
        if (film != null) {
            filmAdapter?.removeAt(position)
            val pos: Int? = favouritesAdapter?.itemPosition(film)
            if (favouritesAdapter?.itemPosition(film) != -1) {
                film.isFavoured = true
                if (pos != null) {
                    favouritesAdapter?.removeAt(pos)
                }
            }
            lifecycleScope.launch(Dispatchers.IO) {
                FIlmRepository.delete(FilmModel(film.name, film.date, film.description))
            }
            Snackbar.make(
                viewBinding.root,
                getString(R.string.cancel_deletion),
                Snackbar.LENGTH_SHORT
            )
                .setAction(getString(R.string.yes)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val filmModel = FilmModel(film.name, film.date, film.description)
                        val currentUserEmail: String? =
                            ServiceLocator.getSharedPreferences().getString(ParamsConstants.EMAIL_SP_TAG, "")
                        FIlmRepository.save(filmModel)
                        if (currentUserEmail != null) {
                            UserRepository.saveUserFilmCrossRef(currentUserEmail, filmModel)
                        }
                    }
                    if (film.isFavoured) {
                        favouritesAdapter?.itemCount?.let { it1 ->
                            favouritesAdapter?.addItem(
                                it1,
                                film
                            )
                        }
                    }
                    filmAdapter?.addItem(position, film)
                }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        filmAdapter = null
    }

    companion object {
        const val HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG"

        private const val MARGIN_WIDTH_DP = 16
        private const val HEIGHT_DIVIDER = 4
        private const val SWIPE_THRESHOLD = 0.5f
    }
}
