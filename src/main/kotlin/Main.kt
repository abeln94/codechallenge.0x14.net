import java.io.File
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

fun main() {
    // prepare output
    File("output.txt").printWriter().use { out ->
        var index = 0
        // parse input
        File("input.txt").forEachLine { line ->
            if (index != 0) out.println("Case #$index: " + convert(line))
            index++
        }
    }
}

// from https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html
val VALID_LANGUAGES = mapOf(
    "CA" to "ca",
    "CZ" to "cs",
    "DE" to "de",
    "DK" to "da",
    "EN" to "en",
    "ES" to "es",
    "FI" to "fi",
    "FR" to "fr",
    "IS" to "is",
    "GR" to "el",
    "HU" to "hu",
    "IT" to "it",
    "NL" to "nl",
    "VI" to "vi",
    "PL" to "pl",
    "RO" to "ro",
    "RU" to "ru",
    "SE" to "sv",
    "SI" to "sl",
    "SK" to "sk",
)
// CA: catalan 	CZ: czech 	DE: german 	DK: danish 	EN: english
//ES: spanish 	FI: finnish 	FR: french 	IS: icelandic 	GR: greek
//HU: hungarian 	IT: italian 	NL: dutch 	VI: vietnamese 	PL: polish
//RO: romanian 	RU: russian 	SE: swedish 	SI: slovenian 	SK: slovak

/**
 * Convert each case
 */
fun convert(line: String): String {
    // get parts
    val (date, lang) = line.split(":")

    return runCatching {
        // get the date
        LocalDate.parse(
            // reversed if necessary
            if (date[2] == '-') date.split("-").reversed().joinToString("-")
            else date
        )
    }.getOrElse { return "INVALID_DATE" }
        // get its day of week
        .dayOfWeek
        // translated by the locale
        .getDisplayName(
            TextStyle.FULL_STANDALONE,
            Locale.Builder()
                .setLanguage(VALID_LANGUAGES[lang] ?: return "INVALID_LANGUAGE")
                .build()
        )
        // return as lowercase
        .lowercase()
        // is java wrong? seems so, strange...
        .replace("ț", "ţ")

}
