import java.io.File

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {
    // prepare output file
    File("C3_${data}_out.txt").printWriter().use { out ->
        var index = 0
        // prepare input file
        File("C3_${data}_in.txt").forEachLine { line ->
            // convert each
            if (index != 0) out.println("Case #$index: " + convert(line))
            index++
        }
    }
}

/**
 * Convert each line
 */
private fun convert(line: String): String {
    // split first by '|' (with a bit of cleanup for unused chars)
    val (words, points_raw) = line.remove(" ").remove("'").split('|')

    val (ignore, element_sep, data_sep) = when {
        // if dictionary: ignore '{...}', split by "," then by ":"
        points_raw.startsWith('{') -> Triple(1, ",", ":")
        // if tuple: ignore '[(...)]', split by "," then by ":"
        points_raw.startsWith('[') -> Triple(2, "),(", ",")
        // if assignments: ignore nothing, split by "," then by "="
        else -> Triple(0, ",", "=")
    }

    val points = points_raw
        // ignore
        .substring(ignore, points_raw.length - ignore)
        // separation of elements
        .split(element_sep)
        // separation of char and value
        .map { it.split(data_sep) }
        // convert to map
        .associate { (c, p) ->
            // associating to each char its score
            c[0] to
                    // fractions are just a division
                    if ('/' in p) p.split('/').map { it.toDouble() }.let { (l, r) -> l / r }
                    // normal numbers are better
                    else p.toDouble()
        }

    // calculate the score of the two words
    val (pair1, pair2) = words.split("-").map {
        // by adding all its points
        it to it.toCharArray().sumOf { c -> points[c] ?: 0.0 }
    }

    // and return max
    return when {
        pair1.second > pair2.second -> pair1.first
        pair1.second < pair2.second -> pair2.first
        // unless they are equal
        else -> "-"
    }

}

/**
 * Just a shorthand of replacing something with nothing
 */
private fun String.remove(s: String) = replace(s, "")

