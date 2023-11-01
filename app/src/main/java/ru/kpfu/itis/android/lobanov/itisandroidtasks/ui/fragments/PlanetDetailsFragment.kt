package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
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

        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        enterTransition = fade
        exitTransition = fade

        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            tvTitle.text = arguments?.getString(ParamsKey.PLANET_NAME_KEY)
            tvDesc.text = arguments?.getString(ParamsKey.PLANET_DETAILS_KEY)
            arguments?.getInt(ParamsKey.PLANET_IMAGE_KEY)?.let { ivIcon.setImageResource(it) }
        }
    }

    companion object {
        const val PLANET_DETAILS_FRAGMENT_TAG = "PLANET_DETAILS_FRAGMENT_TAG"

        fun newInstance(planetName: String, planetDetails: String?, planetImage: Int?) =
            PlanetDetailsFragment().apply {
                arguments = bundleOf(
                    ParamsKey.PLANET_NAME_KEY to planetName,
                    ParamsKey.PLANET_DETAILS_KEY to planetDetails,
                    ParamsKey.PLANET_IMAGE_KEY to planetImage
                )
            }
    }
}
