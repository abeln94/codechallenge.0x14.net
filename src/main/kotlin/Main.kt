import java.io.File
import java.lang.Exception

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        var index = 1
        // prepare input file
        File("input.txt").readLines().drop(1).windowed(2, 2) { (f, s) ->
            // convert each
            out.println("Case #$index: " + convert(f, s))
            index++
        }
    }
}

/**
 * list of notes
 */
val notes = listOf(
    listOf("A"),
    listOf("A#", "Bb"),
    listOf("B", "Cb"),
    listOf("C", "B#"),
    listOf("C#", "Db"),
    listOf("D"),
    listOf("D#", "Eb"),
    listOf("E", "Fb"),
    listOf("F", "E#"),
    listOf("F#", "Gb"),
    listOf("G"),
    listOf("G#", "Ab"),
)

/**
 * Convert each step
 */
fun convert(root: String, scale: String): String {
    // take each scale numerically
    val notes = scale.map { if (it == 'T') 2 else 1 }
        // and convert to the list of steps (numerical)
        .fold(listOf(notes.indexOfFirst { root in it })) { result, step ->
            // traversing the array
            result + ((result.last() + step) % notes.size)
        }
        // then to the list of possible notes
        .map { notes[it] }
    // to find a valid solution, we use a recursive approach
    return foldUnique(emptyList(), notes) ?: throw Exception("no solution")
}

/**
 * Takes an already built result, and tries to append one of each in notes.
 * If there is a solution, it is returned, if not null is returned
 */
fun foldUnique(result: List<String>, notes: List<List<String>>): String? {
    // solution! return it
    if (notes.isEmpty()) return result.joinToString("")

    // for each in the next possible notes
    notes.first()
        // remove those who can't satisfy the current solution
        .filter { possible -> result.none { it != possible && it[0] == possible[0] } }
        // and try all the others
        .forEach { possible ->
            // try it
            foldUnique(result + possible, notes.drop(1))
                // if valid, return
                ?.let { return it }
            // else continue
        }
    // no solution
    return null
}

