package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentStartBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.AirplaneModeHandler
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.Notification
import ru.kpfu.itis.android.lobanov.itisandroidtasks.utils.NotificationProvider

class StartFragment : Fragment(R.layout.fragment_start) {
    private var _viewBinding: FragmentStartBinding? = null
    private val viewBinding: FragmentStartBinding
        get() = _viewBinding!!
    private var notificationProvider: NotificationProvider? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentStartBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationProvider = context?.let { NotificationProvider(it) }
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            btnShowNotification.setOnClickListener {
                val title: String = etNotificationTitle.text.toString()
                val message: String = etNotificationBody.text.toString()

                Notification.title = title
                Notification.content = message

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Notification.importance?.let { notificationProvider?.setUpBuilder(it) }
                    notificationProvider?.buildNotification()
                    notificationProvider?.showNotification()
                }
            }

            val airplaneModeHandler = AirplaneModeHandler(requireContext())
            airplaneModeHandler.handle {
                btnShowNotification.isEnabled = !it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        notificationProvider = null
    }

    companion object {
        const val START_FRAGMENT_TAG = "START_FRAGMENT_TAG"
    }
}
