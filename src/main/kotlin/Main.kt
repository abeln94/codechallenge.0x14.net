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

                // while there are still functions unmatched
                while (unmatched.isNotEmpty()) {
                    // get the best group
                    val matched = unmatched.takeBestOfSize(k)
                    // add its points
                    points += matched.reduce { l, r -> l.commonPrefixWith(r) }.length
                    // remove from the unmatched
                    // [kotlin issue: unmatched.removeAll removes duplicates, we don't want that]
                    matched.forEach { unmatched.remove(it) }
                }

                // report output
                out.println("Case #${index}: $points")
            }
        }
    }
}

/**
 * returns [size] strings of this list with the bigger common prefix
 * All chars before (not including) [char] should be equal already
 */
tailrec fun List<String>.takeBestOfSize(size: Int, char: Int = 0): Pair<List<String>> =
    groupBy { it.getOrElse(char) { ' ' } }.values
        .filter { it.size >= size }
        .getOrElse(0) { return take(size) to char }
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
