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
                    val (matched, prefixSize) = unmatched.takeBestOfSize(k)
                    // add its points
                    points += prefixSize
                    if (matched.reduce { l, r -> l.commonPrefixWith(r) }.length != prefixSize)
                        unmatched.takeBestOfSize(k)
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
 * returns [size] strings of this list with the bigger common prefix, and the length of that common prefix
 * All chars before (not including) [char] should be equal already
 */
tailrec fun List<String>.takeBestOfSize(size: Int, char: Int = 0): Pair<List<String>, Int> =
    // first group by next character to check (if the string is shorter it is considered to be followed by a space)
    groupBy { it.getOrElse(char) { ' ' } }.entries
        // keep only those groups containing [size] or more entries
        .filter { it.value.size >= size }
        // get the first group (although it doesn't really matter which one you get)
        .getOrElse(0) {
            // if there are no groups, it means there are no [size] strings with common prefix in this step,
            // so in that case return any [size] one of them. The common prefix length is exactly [char]
            return take(size) to char
        }
        // now check if the common char is the space
        .also {
            // if so, it means there is no need to do further checks and we can exit now
            // the common prefix is exactly [char] again
            if (it.key == ' ') return it.value.take(size) to char
        }
        // otherwise, we have a group with at least [size] strings,
        // so repeat to find the better group there from the next char
        .value.takeBestOfSize(size, char + 1)


/**
 * Splits a string into a space separated list of integers
 */
fun String.splitInts() = split(" ").map { it.toInt() }

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
