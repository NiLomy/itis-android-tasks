package ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.PlanetAdapter
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemButtonBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.fragments.BottomSheetDialogFragment

class ButtonViewHolder (
    private val viewBinding: ItemButtonBinding,
    private val fragmentManager: FragmentManager,
    private val adapter: PlanetAdapter
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bindItem() {
        viewBinding.btnShowSheet.setOnClickListener {
            val bottomSheet = BottomSheetDialogFragment(adapter)
            bottomSheet.show(fragmentManager, BottomSheetDialogFragment.DIALOG_FRAGMENT_TAG)
        }
    }
}
