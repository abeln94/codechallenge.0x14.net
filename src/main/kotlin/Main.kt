import java.io.BufferedReader
import java.io.File
import java.math.BigInteger

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { index ->

                val numbers = inp.readLines(inp.readLine().toInt()).map { BigInteger(it, 16) }

                var result = numbers.reduce { a, b -> gcd(a, b) }

                while (result.mod(BigInteger.TWO) == BigInteger.ZERO) result = result.divide(BigInteger.TWO)

                // report output
                out.println("Case #${index}: ${result.toString(16).lowercase()}")
            }
        }
    }
}

fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    var (small, big) =
        if (a < b) a to b
        else b to a

    while (true) {
        val mod = division(big, small)
        if (mod == BigInteger.ZERO) break
        big = small
        small = mod
    }
    return small
}

fun division(big: BigInteger, small: BigInteger): BigInteger {
    val divider = small.toString(2).toCharArray()
    var data = (big.toString(2) + "0".repeat(divider.size - 1)).toCharArray()
    while (data.size > divider.size - 1) {
        if (data[0] == '0') data = data.drop(1).toCharArray()
        else {
            divider.forEachIndexed { i, b ->
                data[i] = if (data[i] != b) '1' else '0'
            }
        }
    }
    return BigInteger(data.joinToString(""), 2)
}

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
