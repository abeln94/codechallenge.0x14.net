import java.io.BufferedReader
import java.io.File
import java.math.BigInteger

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {
    // prepare output file
    File("C19_${data}_out.txt").printWriter().use { out ->
        // prepare input file
        File("C19_${data}_in.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { index ->

                // read the numbers, and store as bigint
                val numbers = inp.readLines(inp.readLine().toInt()).map { BigInteger(it, 16) }

                // compute the gcd of all numbers, by doing gcd of 1 with 2, then result with 3, etc (until only one is left)
                var result = numbers.reduce { a, b -> gcd(a, b) }

                // the required polynomial need to end in 1 by removing leading zeros
                // with bigintegers that's just divide by 2
                while (result>BigInteger.ONE && result.mod(BigInteger.TWO) == BigInteger.ZERO)
                    result = result.divide(BigInteger.TWO)

                // report output, just write it in lowercase hex again
                out.println("Case #${index}: ${result.toString(16).lowercase()}")
            }
        }
    }
}

/**
 * The gcd (for crc) of two bigintegers
 * Just the Euclidean algorithm
 */
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

/**
 * The CRC division
 */
fun division(big: BigInteger, small: BigInteger): BigInteger {
    // prepare the divider
    val divider = small.toString(2).toCharArray()
    // and the data (with appended zeros at the end for the result)
    var data = (big.toString(2) + "0".repeat(divider.size - 1)).toCharArray()
    // now keep dividing while required
    while (data.size > divider.size - 1) {
        if (data[0] == '0') {
            // skip directly if the first byte is 0
            data = data.drop(1).toCharArray()
        } else {
            // do the xor otherwise
            divider.forEachIndexed { i, b ->
                data[i] = if (data[i] != b) '1' else '0' // this is a xor
            }
        }
    }
    // parse and return the result
    return BigInteger(data.joinToString(""), 2)
}

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
