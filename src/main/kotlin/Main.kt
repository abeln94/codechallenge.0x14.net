import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

//----------------- socket -----------------//

// prepare
val pingSocket = Socket("codechallenge-daemons.0x14.net", 7162)
val out = PrintWriter(pingSocket.getOutputStream(), true)
val input = BufferedReader(InputStreamReader(pingSocket.getInputStream()))

/**
 * Close socket
 */
fun closeAll() {
    input.close()
    out.close()
    pingSocket.close()
}

/**
 * sends a command, returns the result
 */
fun communicate(data: String): String {
    // log too
    println(data)
    out.println(data)
    return input.readLine().also { println(it) }
}

/**
 * Read all available lines
 */
fun readReady() = buildString {
    Thread.sleep(100) // give a little rest
    while (input.ready()) append(input.readLine())
}

//----------------- primes -----------------//

const val N = 100
const val Q = 1500

const val primes = 25 // there are 25 primes between 1 and 100

fun whereAreThePrimesAt() {
    val divisors = (0..N).map { mutableSetOf(1) }
    divisors[0].addAll(setOf(0, -1))

    while (true) {

        var i = 0
        i@ while (i < N) {
            i++

            val notDivisors = mutableSetOf<Int>()

            var j = 0
            j@ while (j < N) {
                j++
                if (j == i) continue@j

                if (divisors[i].size >= 2) continue@i

                if (((notDivisors - 1) intersect divisors[j]).isNotEmpty()) continue@j
//                if ((notDivisors - 1).containsAll(divisors[j])) continue@j

                val gcd = communicate("? $i $j").toInt()
                divisors[i] += gcd.divisors()
                divisors[j] += gcd.divisors()

                notDivisors += divisors[j] - gcd.divisors()

                println(divisors)
                println(notDivisors)
                println("unknowns remaining: " + (divisors.count { it.size == 1 } - 1))

                if (divisors.filter { it.size == 2 }.distinct().size == primes && divisors.count { it.size == 1 } == 1) {
                    communicate("! " + divisors.indexesOf { it.size == 1 }
                        .joinToString(" "))
                    return
                }

            }
        }

    }
}

private fun Int.divisors(): Set<Int> {
    val result = mutableSetOf(1, this)
    for (i in 2 until this) {
        if (this % i == 0) result += i
    }
    return result
}

fun Int.isPrime(): Boolean {
    var flag = false
    for (i in 2..this / 2) {
        // condition for nonprime number
        if (this % i == 0) {
            flag = true
            break
        }
    }
    return flag
}

//----------------- testing -----------------//

/**
 * The most basic interactive prompt
 */
fun prompt() {
    while (true) {
        println(readReady())
        readLine().let {
            when (it) {
                "_primes" -> whereAreThePrimesAt()
                // exit
                "_exit" -> return
                // send
                else -> out.println(it)
            }
        }
    }
}

//----------------- main -----------------//

fun main() {
    println(readReady())
    whereAreThePrimesAt()
    prompt()
    closeAll()
}

fun <T> List<T>.indexesOf(filter: (T) -> Boolean) = withIndex().filter { filter(it.value) }.map { it.index }