package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentNotificationSettingsBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.Notification
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.NotificationParams

class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {
    private val viewBinding: FragmentNotificationSettingsBinding by viewBinding(
        FragmentNotificationSettingsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            initSpinners()

            detailedMsgCheckbox.setOnCheckedChangeListener { _, isChecked ->
                Notification.isBigText = isChecked
            }

            showBtnCheckbox.setOnCheckedChangeListener { _, isChecked ->
                Notification.isButtonsShown = isChecked
            }
        }
    }

    private fun initSpinners() {
        val importanceSpinner = viewBinding.notificationImportanceSpinner

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.notification_importance_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                importanceSpinner.adapter = adapter
            }
        }

        val notificationImportance: Array<String> =
            resources.getStringArray(R.array.notification_importance_array)
        importanceSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Notification.importance =
                        NotificationParams.priority[notificationImportance[position].lowercase().trim()]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val visibilitySpinner = viewBinding.notificationVisibilitySpinner

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.notification_visibility_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                visibilitySpinner.adapter = adapter
            }
        }

        val notificationVisibility: Array<String> =
            resources.getStringArray(R.array.notification_visibility_array)
        visibilitySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val visibilityNumber: Int =
                    NotificationParams.visibility[notificationVisibility[position].lowercase().trim()]
                        ?: throw RuntimeException(getString(R.string.unsupported_visibility_type_text))
                Notification.visibility = visibilityNumber
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    companion object {
        const val NOTIFICATION_SETTINGS_FRAGMENT_TAG = "NOTIFICATION_SETTINGS_FRAGMENT_TAG"
    }
}
