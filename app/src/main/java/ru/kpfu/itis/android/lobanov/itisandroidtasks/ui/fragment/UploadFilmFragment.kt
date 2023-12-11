package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.repository.FIlmRepository
import ru.kpfu.itis.android.lobanov.itisandroidtasks.data.model.FilmModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.FragmentUploadFilmBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.MainActivity
import java.sql.Date

class UploadFilmFragment: Fragment(R.layout.fragment_upload_film) {
    private var _viewBinding: FragmentUploadFilmBinding? = null
    private val viewBinding: FragmentUploadFilmBinding
        get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentUploadFilmBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            uploadBtn.setOnClickListener {
                val name: String = nameEt.text.toString()
                val date: String = yearEt.text.toString()
                val description: String = descriptionEt.text.toString()

                if (isValid(name, date)) {
                    (requireActivity() as MainActivity).uploadFilm(name, date, description)
                }
            }
        }
    }

    private fun isValid(name: String, date: String): Boolean {
        if (name.isEmpty() || date.isEmpty()) {
            return false
        }
        return true
    }

    private fun makeToast(text: String) {
        activity?.runOnUiThread {
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val UPLOAD_FILM_FRAGMENT_TAG = "UPLOAD_FILM_FRAGMENT_TAG"
    }
}
