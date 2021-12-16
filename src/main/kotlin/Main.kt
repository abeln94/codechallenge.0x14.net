import java.io.BufferedReader
import java.io.File

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { index ->

                // get data
                val (n, k) = inp.readLine().splitInts()
                val unmatched = inp.readLines(n)
                var points = 0

                while (unmatched.isNotEmpty()) {
                    val matched = unmatched.takeBestOfSize(k)
                    points += matched.reduce { l, r -> l.commonPrefixWith(r) }.length
                    matched.forEach { unmatched.remove(it) }
                    if (index == 35) print(matched.size)
                }


                // report output
                out.println("Case #${index}: $points")
            }
        }
    }
}

fun List<String>.takeBestOfSize(size: Int, char: Int = 0): List<String> =
    groupBy { it.getOrElse(char) { ' ' } }.values
        .filter { it.size >= size }
        .getOrElse(0) { return take(size) }
        .also { if (it[0].length < char) return it.take(size) }
        .takeBestOfSize(size, char + 1)


/**
 * Splits a string into a space separated list of integers
 */
fun String.splitInts() = split(" ").map { it.toInt() }

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
