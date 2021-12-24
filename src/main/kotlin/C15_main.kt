import java.io.BufferedReader
import java.io.File
import java.math.BigInteger
import kotlin.math.min

/**
 * Checks if a number contains the word twenty when written in english
 */
private val Int.isTwenty: Boolean
    get() = when {
        // 20 .. 29 is
        (this % 100) in 20..29 -> true
        // 20.000 .. 29.000, and 20.000.000 .. 29.000.000 etc
        this >= 20000 -> (this / 1000).isTwenty
        // else not
        else -> false
    }

/**
 * The modulus (a strange number...)
 */
const val MOD = 100000007
val BI_MOD = BigInteger(MOD.toString())

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {
    // prepare output file
    File("C15_${data}_out.txt").printWriter().use { out ->
        // prepare input file
        File("C15_${data}_in.txt").bufferedReader().use { inp ->

            // read all numbers, no need to repeat each time, compute all directly
            val numbers = inp.readLines(inp.readLine().toInt()).map { it.toIntOrNull() ?: MOD }
            // a number bigger than MOD will always return 0
            // because a>MOD => a! % MOD = a*(a-1)*...*(MOD+1)*MOD*(MOD-1)*...*1 % MOD => MOD*(...) % MOD which is 0
            // this means that we don't need to calculate all of them, only until MOD-1 at most
            // this number fits into an int, so if a number doesn't, it means it's bigger
            // so the result is 0, and we can ignore it
            val results = mutableMapOf<Int, String>().withDefault { "0" }

            // calculate all numbers up to MOD-1 (or the bigger in the input if it's smaller)
            var result = BigInteger.ONE
            for (i in 1..min(numbers.maxOrNull() ?: return, MOD - 1)) {
                if (!i.isTwenty) {
                    // skip 'twenty' numbers
                    result = result * BigInteger(i.toString()) % BI_MOD
                }
                // save if one of the required ones
                if (i in numbers) results[i] = result.toString()
            }

            // report each output
            numbers.forEachIndexed { case, p ->
                out.println("Case #${case + 1}: ${results.getValue(p)}")
            }
        }
    }
}


/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
