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
                    "2   Spacer:\n" +
                    "3 Label: Marner Sis. Recording\n" +
                    "4   Band: Auditones\n" +
                    "5     Festival: Twisted Tour\n" +
                    "6   Spacer:\n" +
                    "7 Label: Outerscope\n" +
                    "8   Band: Summon\n" +
                    "9     Festival: Twisted Tour\n" +
                    "10   Spacer:\n" +
                    "11 Label: Pacific Records\n" +
                    "12   Band: Propeller\n", makeDisplayItems(festivals).toText()
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
                    "3   Spacer:\n" +
                    "4 Label: Outerscope\n" +
                    "5   Band: Summon\n" +
                    "6     Festival: Twisted Tour\n", displayItems.toText()
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
                    "3   Spacer:\n" +
                    "4 Label: Outerscope\n" +
                    "5   Band: Summon\n" +
                    "6     Festival: Twisted Tour\n" +
                    "7   Spacer:\n" +
                    "8 Label: Pacific Records\n" +
                    "9   Band: Propeller\n" +
                    "10     Festival: Groovy Times!\n" +
                    "11     Festival: Twisted Tour\n", makeDisplayItems(festivals).toText()
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
                    "3   Spacer:\n" +
                    "4 Label: Atlantic Records\n" +
                    "5   Band: Propeller\n" +
                    "6     Festival: Twisted Tour\n" +
                    "7   Spacer:\n" +
                    "8 Label: Outerscope\n" +
                    "9   Band: Summon\n" +
                    "10     Festival: Twisted Tour\n" +
                    "11   Spacer:\n" +
                    "12 Label: Pacific Records\n" +
                    "13   Band: Propeller\n" +
                    "14     Festival: Groovy Times!\n", makeDisplayItems(festivals).toText()
        )
    }

}
