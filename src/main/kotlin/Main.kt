import java.io.BufferedReader
import java.io.File
import java.math.BigInteger

/**
 * Algorithm:
 * After reading and understanding the problem, the solution is as follows:
 * We are required to find the bigger polynomial that allows us to not modify the crc when applying some noise
 * This is the equivalent as saying we need to find the bigger polynomial that zeros (when applying crc) the noise
 * (because CRC(message^noise^0..0)=CRC(message)^CRC(noise)^CRC(0..0), and since CRC(noise)=0 then CRC(message^noise)=CRC(message)
 *
 * We need to find the bigger polynomial that 'zeroes' all the noise.
 * The CRC operation is just as a 'simple' division, where the result is the CRC output,
 * and so we 'simply' need to find the gcd (great common divisor) of all the polynomial noises,
 * which can be calculated sequentially gcd(1,2,3) = gcd(gcd(1,2),3) etc
 * And to find the gcd, we can use the Euclidean algorithm which just so happens to only require the modulo operation
 * Exactly what we know how to do! (CRC)
 *
 * So, to recap:
 * 1) Get all the noise
 * 2) Apply gcd to each pair of noise until only one is left
 *    a) to apply gcd use the euclidean algorithm using crc itself as the modulo operation
 * 3) The result is the bigger polynomial that solves the problem
 *
 * Except there is one extra requirement: the polynomial must end in '1'
 * But that's easy, we just need to remove the extra leading zeros,
 * because a polynomial still works when adding or subtracting zeros (if the length fits)
 *
 * so
 * 4) Remove all leading zeros of the output
 */

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

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
