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
    var q = 2
    val divisors = (1..N).associateWith { mutableSetOf(1) }
    val notDivisors = (1..N).associateWith { mutableSetOf<Int>() }

    while (true) {

        val i = divisors.filter { it.value.size <= 2 }.keys.random()

        val probable = divisors.filter { (it.value intersect notDivisors.getValue(i)).isEmpty() }.filter { it.value.size > 1 }
        val j = if (probable.isEmpty()) {
            (1..N).random()
        } else {
            val maxProbable = probable.maxOf { it.value.size }
            probable.filter { it.value.size == maxProbable }.keys.random()
        }

        if (i == j) continue

        val gcd = communicate("? $i $j").toInt()
        divisors.getValue(i) += gcd.divisors()
        divisors.getValue(j) += gcd.divisors()

        notDivisors.getValue(i) += divisors.getValue(j) - divisors.getValue(i)
        notDivisors.getValue(j) += divisors.getValue(i) - divisors.getValue(j)

        notDivisors.getValue(i) += notDivisors.getValue(i).flatMap { it.mutiples(N) }
        notDivisors.getValue(j) += notDivisors.getValue(j).flatMap { it.mutiples(N) }

        println(divisors)
        println(notDivisors)
        println("unknowns remaining: " + (divisors.count { it.value.size == 1 } - 1))

        if (q++ == Q) communicate("! " + divisors.filter { it.value.size <= 2 }.keys.take(primes + 1).joinToString(" ")).also { return }

        (divisors.values zip notDivisors.values).forEachIndexed { i, (d, nd) ->
            if ((d intersect nd).isNotEmpty()) throw Exception("${i + 1}")
        }

        if (divisors.values.filter { it.size == 2 }.distinct().size == primes && divisors.values.count { it.size == 1 } == 1) {
            communicate(
                "! " + divisors.filter { it.value.size == 1 }.keys
                    .joinToString(" ")
            )
            return
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

private fun Int.mutiples(max: Int): Set<Int> {
    val result = mutableSetOf(this)
    for (i in 2 until max / this) {
        result += this * i
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