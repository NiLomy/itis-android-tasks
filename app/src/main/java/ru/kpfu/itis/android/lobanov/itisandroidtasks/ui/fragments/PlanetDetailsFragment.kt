package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.base.BaseFragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentPlanetDetailsBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.ParamsKey

class PlanetDetailsFragment : BaseFragment(R.layout.fragment_planet_details) {
    private val viewBinding: FragmentPlanetDetailsBinding by viewBinding(
        FragmentPlanetDetailsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }

        initViews()
    }

    private fun initViews() {
        val transitionName = arguments?.getString(ParamsKey.TRANSITION_NAME_KEY)

        with(viewBinding) {
            tvTitle.text = arguments?.getString(ParamsKey.PLANET_NAME_KEY)
            tvDesc.text = arguments?.getString(ParamsKey.PLANET_DETAILS_KEY)
            arguments?.getInt(ParamsKey.PLANET_IMAGE_KEY)?.let { ivIcon.setImageResource(it) }
            ivIcon.transitionName = transitionName
        }
    }

    companion object {
        const val PLANET_DETAILS_FRAGMENT_TAG = "PLANET_DETAILS_FRAGMENT_TAG"

        fun newInstance(
            planetName: String,
            planetDetails: String?,
            planetImage: Int?,
            transitionName: String
        ) =
            PlanetDetailsFragment().apply {
                arguments = bundleOf(
                    ParamsKey.PLANET_NAME_KEY to planetName,
                    ParamsKey.PLANET_DETAILS_KEY to planetDetails,
                    ParamsKey.PLANET_IMAGE_KEY to planetImage,
                    ParamsKey.TRANSITION_NAME_KEY to transitionName
                )
            }
    }
}
