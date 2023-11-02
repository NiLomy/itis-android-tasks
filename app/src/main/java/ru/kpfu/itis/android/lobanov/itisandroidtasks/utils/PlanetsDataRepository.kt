package ru.kpfu.itis.android.lobanov.itisandroidtasks.utils

import ru.kpfu.itis.android.lobanov.itisandroidtasks.R
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.ButtonModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DateModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.PlanetModel
import ru.kpfu.itis.android.lobanov.itisandroidtasks.model.DataModel

object PlanetsDataRepository {

    private val planetsList = mutableListOf<DataModel>()
    private var dataSet = listOf<PlanetModel>()
    private const val BLOCK_SIZE = 8

    init {
        dataSet = listOf(
            PlanetModel(
                planetId = 1,
                planetName = "Mercury",
                planetDetails = "The planet closest to the Sun. There is no water or air on Mercury. " +
                        "Due to the fact that Mercury is so close to the sun, the daytime " +
                        "temperature on this planet is almost +450 °C.",
                planetImage = R.drawable.mercury,
            ),
            PlanetModel(
                planetId = 2,
                planetName = "Venus",
                planetDetails = "A planet that is often called the morning or evening star. These names " +
                        "are not accidental: Venus can be seen in the evening, in the rays of" +
                        " the setting Sun, or in the morning, just before sunrise. " +
                        "The surface of Venus is a plain strewn with rocks and fragments of rocks." +
                        " There is no water or life on Venus.",
                planetImage = R.drawable.venus,
            ),
            PlanetModel(
                planetId = 3,
                planetName = "Earth",
                planetDetails = "It is almost 110 times smaller than this luminary in diameter." +
                        " Try to imagine that the Sun has turned into a melon. " +
                        "Then the earth with its huge cities, villages, mountains and seas " +
                        "would become the size of a small fruit pit.",
                planetImage = R.drawable.earth,
            ),
            PlanetModel(
                planetId = 4,
                planetName = "Mars",
                planetDetails = "It is called the red planet because of the rust-red color of its surface." +
                        " The temperature on Mars is very low both during the day and at night.",
                planetImage = R.drawable.mars,
            ),
            PlanetModel(
                planetId = 5,
                planetName = "Jupiter",
                planetDetails = "The largest planet in the Solar system. It is 1000 times larger" +
                        " than the Earth. Jupiter is located at a huge distance from the Sun," +
                        " which is why the temperature on this gas giant is -140 °C.",
                planetImage = R.drawable.jupiter,
            ),
            PlanetModel(
                planetId = 6,
                planetName = "Saturn",
                planetDetails = "Externally, Saturn differs from the rest of the planets in that it is" +
                        " surrounded by many luminous rings. Each Saturn ring consists of" +
                        " even thinner rings. This \"decoration\" consists of billions of stone" +
                        " fragments covered with ice.",
                planetImage = R.drawable.saturn,
            ),
            PlanetModel(
                planetId = 7,
                planetName = "Uranus",
                planetDetails = "It is 19 times farther away from the Sun than the Earth," +
                        " so it receives very little heat.",
                planetImage = R.drawable.uranus,
            ),
            PlanetModel(
                planetId = 8,
                planetName = "Neptune",
                planetDetails = "It is similar in appearance and size to Uranium." +
                        " It is highly compressed and rotates rapidly." +
                        "у Neptune is 2.8 billion kilometers away from the Sun.",
                planetImage = R.drawable.neptune,
            ),
        )
    }

    fun getSinglePlanet(position: Int): PlanetModel {
        return planetsList[position] as PlanetModel
    }

    fun getPlanets(amount: Int): MutableList<DataModel> {
        if (planetsList.isEmpty()) {
            buildList(amount)
        }

        return planetsList
    }

    fun getAllPlanets(): List<DataModel> = planetsList

    fun addPlanet(position: Int, planetModel: PlanetModel) {
        planetsList.add(position, planetModel)
    }

    fun addRandomPlanets(amount: Int) {
        repeat(amount) {
            val insertPosition = when (planetsList.size) {
                1 -> 1
                2 -> 2
                else -> (2 until planetsList.size).random()
            }
            val positionOfPlanet = (1 until dataSet.size).random()
            planetsList.add(
                insertPosition, PlanetModel(
                    planetsList.size + 1,
                    dataSet[positionOfPlanet].planetName,
                    dataSet[positionOfPlanet].planetDetails,
                    dataSet[positionOfPlanet].planetImage,
                    false
                )
            )
        }
    }

    fun clearAll() {
        if (planetsList.isNotEmpty()) {
            planetsList.clear()
        }
    }

    fun removeAt(position: Int) {
        (planetsList[position] as PlanetModel).isFavoured = false
        (planetsList[position] as PlanetModel).isSelected = false
        planetsList.removeAt(position)
    }

    private fun buildList(amount: Int) {
        planetsList.clear()
        planetsList.add(ButtonModel())
        repeat(amount) {
            val position = (dataSet.indices).random()
            planetsList.add(
                PlanetModel(
                    planetsList.size + 1,
                    dataSet[position].planetName,
                    dataSet[position].planetDetails,
                    dataSet[position].planetImage,
                    false
                )
            )
        }
        addDate()
    }

    private fun addDate() {
        for (i in 1 until planetsList.size step BLOCK_SIZE + 1) {
            planetsList.add(i, DateModel())
        }
    }

    fun markFavourite(planetModel: PlanetModel) {
        val planet =
            getAllPlanets().find { (it as? PlanetModel)?.planetId == planetModel.planetId } as PlanetModel
        planet.isFavoured = !planet.isFavoured
    }

    fun selectPlanet(planetModel: PlanetModel) {
        val planet =
            getAllPlanets().find { (it as? PlanetModel)?.planetId == planetModel.planetId } as PlanetModel
        planet.isSelected = !planet.isSelected
    }
}
