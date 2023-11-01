package ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.adapter.diffutil.PlanetDiffUtil
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemButtonBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemDateBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.databinding.ItemPlanetBinding
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.ButtonModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DateModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.PlanetModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DataModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.ButtonViewHolder
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.DateViewHolder
import ru.kpfu.itis.android.lobanov.itisandroidtasks.ui.holder.PlanetViewHolder
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlanetAdapter(
    private val fragmentManager: FragmentManager,
    private val onPlanetsClicked: ((PlanetModel) -> Unit),
    private val onPlanetsClickedLong: ((PlanetModel, ItemPlanetBinding) -> Unit),
    private val onBinClicked: ((Int) -> Unit),
    private val onBinClickedLong: ((PlanetModel, ItemPlanetBinding) -> Unit),
    private val onLikeClicked: ((Int, PlanetModel) -> Unit),
    private val planetsCount: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var planetsList = mutableListOf<DataModel>()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {
            PLANET_TYPE -> return PlanetViewHolder(
                viewBinding = ItemPlanetBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ),
                onNewsClicked = onPlanetsClicked,
                onNewsClickedLong = onPlanetsClickedLong,
                onBinClicked = onBinClicked,
                onBinClickedLong = onBinClickedLong,
                onLikeClicked = onLikeClicked,
                newsCount = planetsCount
            )

            BUTTON_TYPE -> return ButtonViewHolder(
                viewBinding = ItemButtonBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                ),
                fragmentManager = fragmentManager,
                this
            )

            DATE_TYPE -> return DateViewHolder(
                viewBinding = ItemDateBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )

            else -> throw RuntimeException(parent.context.getString(R.string.there_is_no_such_type))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PlanetViewHolder -> holder.bindItem(planetsList[position] as PlanetModel)
            is ButtonViewHolder -> holder.bindItem()
            is DateViewHolder -> holder.bindItem(
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? PlanetViewHolder)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = planetsList.size

    override fun getItemViewType(position: Int): Int {
        return when (planetsList[position]) {
            is PlanetModel -> PLANET_TYPE
            is ButtonModel -> BUTTON_TYPE
            is DateModel -> DATE_TYPE
            else -> throw RuntimeException(context?.getString(R.string.there_is_no_such_type))
        }
    }

    fun setItems(list: List<DataModel>) {
        val diff = PlanetDiffUtil(oldItemsList = planetsList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        planetsList.clear()
        planetsList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: PlanetModel) {
        this.planetsList[position] = item
        notifyItemChanged(position, item.isFavoured)
    }

    fun removeAt(position: Int) {
        planetsList.removeAt(position)
        notifyItemRemoved(position)
    }

    companion object {
        private const val PLANET_TYPE = 0
        private const val BUTTON_TYPE = 1
        private const val DATE_TYPE = 2
    }
}
