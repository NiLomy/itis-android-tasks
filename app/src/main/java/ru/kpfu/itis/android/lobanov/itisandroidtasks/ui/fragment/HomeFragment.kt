package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.FilmAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.decorations.HorizontalMarginDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.adapter.decorations.VerticalMarginDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.FIlmRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmRVModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentHomeBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.getValueInPx

class HomeFragment: Fragment(R.layout.fragment_home) {
    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!
    private var filmAdapter: FilmAdapter? = null

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
        activity?.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)?.visibility = View.VISIBLE
        initRecyclerView()
        initViews()
    }

    private fun initRecyclerView() {
        filmAdapter = FilmAdapter(
            onFilmClicked = ::onFilmClicked,
            onLikeClicked = ::onFavouriteClicked
        )

        with(viewBinding) {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            filmsRv.layoutManager = layoutManager
            filmsRv.adapter = filmAdapter

            setDecorations(filmsRv)
//            (activity as MainActivity).showAllFilms(filmAdapter, noFilmsTv)
            showAllFilms(filmAdapter, noFilmsTv)
//            filmAdapter?.setItems(NewsDataRepository.getNewsList())
        }
    }

    private fun initViews() {
        with(viewBinding) {

        }
    }

    private fun showAllFilms(filmAdapter: FilmAdapter?, noFilmsTv: TextView) {
        lifecycleScope.async(Dispatchers.IO) {
            val result: MutableList<FilmRVModel> = ArrayList()
            val films: List<FilmModel>? = FIlmRepository.getAll()
            if (films != null) {
                for (i in films.indices) {
                    val f = films[i]
                    result.add(FilmRVModel(i, f.name, f.date, f.description, false))
                }
                filmAdapter?.setItems(result)
                activity?.runOnUiThread {
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

    private fun onFilmClicked(view: View, filmModel: FilmRVModel) {
        addFadeTransition()

        val transitionName = view.transitionName
//        val fragment = PlanetDetailsFragment.newInstance(
//            filmModel.planetName,
//            filmModel.planetDetails,
//            filmModel.planetImage,
//            "planet-${filmModel.planetId}"
//        )
//        fragment.sharedElementEnterTransition = getTransition()
//        fragment.sharedElementReturnTransition = getTransition()
//
//        requireActivity().supportFragmentManager
//            .beginTransaction()
//            .addSharedElement(view, transitionName)
//            .replace(R.id.main_activity_container, fragment)
//            .addToBackStack(null)
//            .commit()
    }

    private fun getTransition(): Transition? {
        val set = TransitionSet()
        set.ordering = TransitionSet.ORDERING_TOGETHER
        set.addTransition(ChangeBounds())
        set.addTransition(ChangeTransform())
        return set
    }

    private fun addFadeTransition() {
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        enterTransition = fade
        exitTransition = fade
    }


    private fun onFavouriteClicked(position: Int, filmModel: FilmRVModel) {
//        PlanetsDataRepository.markFavourite(planetModel)
        filmAdapter?.updateItem(position, filmModel)
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

    }
}
