package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding

import ru.kpfu.itis.android.lobanov.itisandroidtasks.MainActivity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentCoroutineSettingsBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.AirplaneModeHandler
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.CoroutineSettings

class CoroutineSettingsFragment : Fragment(R.layout.fragment_coroutine_settings) {
    private val viewBinding: FragmentCoroutineSettingsBinding by viewBinding(
        FragmentCoroutineSettingsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    CoroutineSettings.coroutinesCount = seekbar.progress
                }

            })

            asyncCheckbox.setOnCheckedChangeListener { _, isChecked ->
                CoroutineSettings.isAsync = isChecked
            }

            stopOnBackCheckbox.setOnCheckedChangeListener { _, isChecked ->
                CoroutineSettings.isStopOnBackground = isChecked
            }

            coroutineLaunchBtn.setOnClickListener {
                (activity as MainActivity).startCoroutines()
            }

            val airplaneModeHandler = AirplaneModeHandler(requireContext())
            airplaneModeHandler.handle {
                coroutineLaunchBtn.isEnabled = !it
            }
        }
    }


    companion object {
        const val COROUTINE_SETTINGS_FRAGMENT_TAG = "COROUTINE_SETTINGS_FRAGMENT_TAG"
    }
}
