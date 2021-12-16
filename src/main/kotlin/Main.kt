import java.io.BufferedReader
import java.io.File
import kotlin.math.max

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { index ->

                val tradings = mutableMapOf<String, MutableMap<String, Int>>()

                // for each website
                (1..inp.readLine().toInt()).forEach {
                    val trades = inp.readLine().substringAfterLast(" ").toInt()

                    (1..trades).forEach {
                        val (fromType, s_multiplier, toType) = inp.readLine().split("-")
                        val multiplier = s_multiplier.toInt()
                        tradings.getOrPut(fromType) {
                            mutableMapOf<String, Int>().withDefault { 0 }
                        }.let {
                            if (multiplier > it.getValue(toType)) it[toType] = multiplier
                        }
                    }
                }


                var testing = listOf("BTC" to 1)

                val tested = testing.toMutableList()

                var result = 1

                while (testing.isNotEmpty() && result == 1) {
                    testing = testing.flatMap { (fromType, value) ->
                        tradings[fromType]?.let {

                            it.map { (toType, multiplier) ->
                                toType to value * multiplier
                            }.filter { it !in tested }
                                .also { tested.addAll(it) }
                        } ?: emptyList()
                    }
                    testing.maxByOrNull { it.first == "BTC" }?.second?.let {
                        result = max(it, result)
                    }
                }


                // report output
                out.println("Case #${index}: $result")
            }
        }
    }
}

/**
 * Splits a string into a space separated list of integers
 */
fun String.splitInts() = split(" ").map { it.toInt() }

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
