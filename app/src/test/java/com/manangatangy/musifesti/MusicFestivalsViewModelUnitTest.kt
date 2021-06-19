package com.manangatangy.musifesti

import com.manangatangy.musifesti.model.Band
import com.manangatangy.musifesti.model.MusicFestival
import com.manangatangy.musifesti.viewmodel.makeDisplayItems
import com.manangatangy.musifesti.viewmodel.toText
import org.junit.Test

import org.junit.Assert.*

class MusicFestivalsViewModelUnitTest {
    @Test
    fun bands_without_festival_appear() {
        val festivals: List<MusicFestival> = listOf(
            MusicFestival(name = null,
                bands = listOf(
                    Band(name = "Critter Girls", recordLabel = "ACR",),
                    Band(name = "Propeller", recordLabel = "Pacific Records",),
                )
            ),
            MusicFestival(name = "Twisted Tour",
                bands = listOf(
                    Band(name = "Summon", recordLabel = "Outerscope",),
                    Band(name = "Auditones", recordLabel = "Marner Sis. Recording",),
                )
            )
        )
        assertEquals(
            "0 Label: ACR\n" +
                    "1   Band: Critter Girls\n" +
                    "2 Label: Marner Sis. Recording\n" +
                    "3   Band: Auditones\n" +
                    "4     Festival: Twisted Tour\n" +
                    "5 Label: Outerscope\n" +
                    "6   Band: Summon\n" +
                    "7     Festival: Twisted Tour\n" +
                    "8 Label: Pacific Records\n" +
                    "9   Band: Propeller\n", makeDisplayItems(festivals).toText()
        )
    }

    @Test
    fun bands_without_label_dont_appear() {
        val festivals: List<MusicFestival> = listOf(
            MusicFestival(name = "Twisted Tour",
                bands = listOf(
                    Band(name = "Summon", recordLabel = "Outerscope",),
                    Band(name = "Squint-281", recordLabel = "",),
                    Band(name = "Auditones", recordLabel = "Marner Sis. Recording",),
                )
            )
        )
        val displayItems = makeDisplayItems(festivals)
        assertEquals(
            "0 Label: Marner Sis. Recording\n" +
                    "1   Band: Auditones\n" +
                    "2     Festival: Twisted Tour\n" +
                    "3 Label: Outerscope\n" +
                    "4   Band: Summon\n" +
                    "5     Festival: Twisted Tour\n", displayItems.toText()
        )
    }

    @Test
    fun one_band_at_multiple_festivals_same_label() {
        val festivals: List<MusicFestival> = listOf(
            MusicFestival(name = "Groovy Times!",
                bands = listOf(
                    Band(name = "Critter Girls", recordLabel = "ACR",),
                    Band(name = "Propeller", recordLabel = "Pacific Records",),
                )
            ),
            MusicFestival(name = "Twisted Tour",
                bands = listOf(
                    Band(name = "Summon", recordLabel = "Outerscope",),
                    Band(name = "Propeller", recordLabel = "Pacific Records",),
                )
            )
        )
        assertEquals(
            "0 Label: ACR\n" +
                    "1   Band: Critter Girls\n" +
                    "2     Festival: Groovy Times!\n" +
                    "3 Label: Outerscope\n" +
                    "4   Band: Summon\n" +
                    "5     Festival: Twisted Tour\n" +
                    "6 Label: Pacific Records\n" +
                    "7   Band: Propeller\n" +
                    "8     Festival: Groovy Times!\n" +
                    "9     Festival: Twisted Tour\n", makeDisplayItems(festivals).toText()
        )
    }

    @Test
    fun one_band_at_multiple_festivals_different_label() {
        val festivals: List<MusicFestival> = listOf(
            MusicFestival(name = "Groovy Times!",
                bands = listOf(
                    Band(name = "Critter Girls", recordLabel = "ACR",),
                    Band(name = "Propeller", recordLabel = "Pacific Records",),
                )
            ),
            MusicFestival(name = "Twisted Tour",
                bands = listOf(
                    Band(name = "Summon", recordLabel = "Outerscope",),
                    Band(name = "Propeller", recordLabel = "Atlantic Records",),
                )
            )
        )
        assertEquals(
            "0 Label: ACR\n" +
                    "1   Band: Critter Girls\n" +
                    "2     Festival: Groovy Times!\n" +
                    "3 Label: Atlantic Records\n" +
                    "4   Band: Propeller\n" +
                    "5     Festival: Twisted Tour\n" +
                    "6 Label: Outerscope\n" +
                    "7   Band: Summon\n" +
                    "8     Festival: Twisted Tour\n" +
                    "9 Label: Pacific Records\n" +
                    "10   Band: Propeller\n" +
                    "11     Festival: Groovy Times!\n", makeDisplayItems(festivals).toText()
        )
    }

}
