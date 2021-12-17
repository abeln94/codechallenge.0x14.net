import java.io.BufferedReader
import java.io.File
import java.math.BigInteger

private val Int.isTwenty: Boolean
    get() = when {
        (this % 100) in 20..29 -> true
        this >= 20000 -> (this / 1000).isTwenty
        else -> false
    }

val MOD = BigInteger("100000007")

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            val numbers = inp.readLines(inp.readLine().toInt()).map { it.toIntOrNull() ?: 0 }
            val results = mutableMapOf<Int, String>()

            var result = BigInteger.ONE
            for (i in 1..(numbers.maxOrNull() ?: return)) {
                if (!i.isTwenty) {
                    result = result * (BigInteger(i.toString()) % MOD) % MOD
                    if (result == BigInteger.ZERO) break
                }
                if (i in numbers) results[i] = result.toString()
            }

            numbers.forEachIndexed { case, p ->
                // report output
                out.println("Case #${case + 1}: ${results.getOrDefault(p, "0")}")
            }
        }
    }
}


/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
