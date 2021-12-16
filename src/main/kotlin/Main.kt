import java.io.BufferedReader
import java.io.File
import kotlin.math.max

/**
 * Bitcoin
 */
const val BTC = "BTC"

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { index ->

                // prepare trading dictionary of better trades (fromType -> toType:multiplier)
                // its default value is an empty map, no trades
                val tradings = mutableMapOf<String, MutableMap<String, Int>>().withDefault { mutableMapOf() }

                // for each website
                (1..inp.readLine().toInt()).forEach {

                    // and for each trade it offers
                    (1..inp.readLine().substringAfterLast(" ").toInt()).forEach {
                        // get and parse the data
                        val (fromType, s_multiplier, toType) = inp.readLine().split("-")
                        val multiplier = s_multiplier.toInt()

                        // get the trading
                        tradings.getOrPut(fromType) {
                            // if didn't exist, initialize with a new one
                            mutableMapOf<String, Int>().withDefault { 0 }
                        }.let { trade ->
                            // if the multiplier is greater than what was saved, replace it
                            // note that the default is 0, so we never save a 0 trade
                            if (multiplier > trade.getValue(toType)) trade[toType] = multiplier
                        }
                    }
                }

                // prepare a list of current testing trades
                var testing = listOf(BTC to 1)

                // and a list of already tested trades (to avoid infinite loops and unnecessary tests)
                val tested = testing.toMutableList()

                // keep the best currently found trade
                var result = 1

                // and iterate while there are trades to test, and no improved result
                while (testing.isNotEmpty() && result == 1) {
                    // for all current testing trades, get the new ones and replace them
                    testing = testing.flatMap { (fromType, value) ->
                        // get all next trades (if any)
                        tradings.getValue(fromType)
                            // and generate the new trades
                            .map { (toType, multiplier) ->
                                toType to value * multiplier
                            }
                            // keep distinct to avoid unnecessary computations
                            .distinct()
                            // remove those who were already tested (if they happened before, they were better)
                            .filter { it !in tested }
                            // and mark the remaining as tested
                            .also { tested.addAll(it) }
                    }
                    // check if there is a BTC result which is better than the current best trade
                    testing.maxByOrNull { it.first == BTC }?.second?.let {
                        // if there is, update it
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
