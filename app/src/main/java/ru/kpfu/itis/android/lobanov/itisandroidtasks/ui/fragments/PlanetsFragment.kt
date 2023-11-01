package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.PlanetAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.decorations.SimpleHorizontalMarginDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.decorations.SimpleVerticalDecorator
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentPlanetsBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemPlanetBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DataModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.PlanetModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ActionType
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.PlanetsDataRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.getValueInPx

class PlanetsFragment : BaseFragment(R.layout.fragment_planets) {

    private val viewBinding: FragmentPlanetsBinding by viewBinding(FragmentPlanetsBinding::bind)
    private var planetAdapter: PlanetAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        planetAdapter = null
    }

    private fun initRecyclerView() {
        with(viewBinding) {
            val input = arguments?.getString(ParamsKey.PLANET_COUNT_KEY)
            if (input != null) {
                val newsCount = input.toInt()
                val list = PlanetsDataRepository.getPlanets(newsCount)

                if (newsCount == 0) {
                    tvNoPlanets.visibility = View.VISIBLE
                } else {
                    tvNoPlanets.visibility = View.GONE
                }

                planetAdapter = PlanetAdapter(
                    fragmentManager = parentFragmentManager,
                    onPlanetsClicked = ::onPlanetClicked,
                    onPlanetsClickedLong = ::onPlanetClickedLong,
                    onBinClicked = ::onBinClicked,
                    onBinClickedLong = ::onBinClickedLong,
                    onLikeClicked = ::onFavouriteClicked,
                    planetsCount = newsCount
                )
                rvPlanets.adapter = planetAdapter

                if (newsCount <= LINEAR_LAYOUT_THRESHOLD) {
                    initLinearLayout(rvPlanets)
                } else {
                    initGridLayout(rvPlanets, list)
                }

                setDecorations(rvPlanets)

                planetAdapter?.setItems(list)
            }
        }
    }

    private fun initLinearLayout(rvPlanets: RecyclerView) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvPlanets.layoutManager = layoutManager

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

        touchHelper.attachToRecyclerView(rvPlanets)
    }

    private fun initGridLayout(rvPlanets: RecyclerView, list: MutableList<DataModel>) {
        val layoutManager = GridLayoutManager(requireContext(), DELIMITER_SPAN)
        rvPlanets.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                if (list[position] is PlanetModel) ELEMENT_SPAN
                else DELIMITER_SPAN
        }
    }

    private fun setDecorations(rvPlanets: RecyclerView) {
        val marginValue = MARGIN_WIDTH_DP.getValueInPx(resources.displayMetrics)
        rvPlanets.addItemDecoration(SimpleHorizontalMarginDecorator(itemOffset = marginValue))
        rvPlanets.addItemDecoration(SimpleVerticalDecorator(itemOffset = marginValue / HEIGHT_DIVIDER))
    }

    private fun onPlanetClicked(planetModel: PlanetModel) {
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        enterTransition = fade
        exitTransition = fade

        (requireActivity() as? BaseActivity)?.goToScreen(
            actionType = ActionType.REPLACE,
            destination = PlanetDetailsFragment.newInstance(
                planetModel.planetName,
                planetModel.planetDetails,
                planetModel.planetImage
            ),
            tag = PlanetDetailsFragment.PLANET_DETAILS_FRAGMENT_TAG,
            isAddToBackStack = true
        )
    }

    private fun onPlanetClickedLong(
        planetModel: PlanetModel,
        binding: ItemPlanetBinding
    ) {
        when (binding.ivBin.visibility) {
            View.VISIBLE -> {
                binding.ivBin.visibility = View.GONE
                PlanetsDataRepository.selectPlanet(planetModel)
            }

            else -> {
                binding.ivBin.visibility = View.VISIBLE
                PlanetsDataRepository.selectPlanet(planetModel)
            }
        }
    }

    private fun onBinClicked(position: Int) {
        removeElement(position)
    }

    private fun onBinClickedLong(
        planetModel: PlanetModel,
        binding: ItemPlanetBinding
    ): Boolean {
        binding.ivBin.visibility = View.GONE
        PlanetsDataRepository.selectPlanet(planetModel)
        return true
    }

    private fun onFavouriteClicked(position: Int, planetModel: PlanetModel) {
        PlanetsDataRepository.markFavourite(planetModel)
        planetAdapter?.updateItem(position, planetModel)
    }

    private fun removeElement(position: Int) {
        val news = PlanetsDataRepository.getSinglePlanet(position)
        planetAdapter?.removeAt(position)
        PlanetsDataRepository.removeAt(position)
        Snackbar.make(viewBinding.root, getString(R.string.cancel_deletion), Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.yes)) {
                PlanetsDataRepository.addNews(position, news)
                planetAdapter?.setItems(PlanetsDataRepository.getAllPlanets())
            }.show()
    }

    companion object {
        const val PLANETS_FRAGMENT_TAG = "PLANETS_FRAGMENT_TAG"
        const val LINEAR_LAYOUT_THRESHOLD = 12
        private const val SWIPE_THRESHOLD = 0.5f
        private const val ELEMENT_SPAN = 1
        private const val DELIMITER_SPAN = 2
        private const val MARGIN_WIDTH_DP = 16
        private const val HEIGHT_DIVIDER = 4

        fun newInstance(planetsCount: String) = PlanetsFragment().apply {
            arguments = bundleOf(ParamsKey.PLANET_COUNT_KEY to planetsCount)
        }
    }
}
