import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

//----------------- socket -----------------//

// prepare
private val pingSocket = Socket("codechallenge-daemons.0x14.net", 7162)
private val out = PrintWriter(pingSocket.getOutputStream(), true)
private val input = BufferedReader(InputStreamReader(pingSocket.getInputStream()))

/**
 * Close socket
 */
private fun closeAll() {
    input.close()
    out.close()
    pingSocket.close()
}

/**
 * sends a command, returns the result
 */
private fun communicate(data: String): String {
    // log too
    println(data)
    out.println(data)
    return input.readLine().also { println(it) }
}

/**
 * Read all available lines
 */
private fun readReady() = buildString {
    Thread.sleep(100) // give a little rest
    while (input.ready()) append(input.readLine())
}

//----------------- primes -----------------//

// constant values
private const val N = 100
private const val Q = 1500
private var q = 0 // number of tests

// the gcd of each pair of numbers, for caching purpose
private val gcds = mutableMapOf<Pair<Int, Int>, Int>()

// the multiples of each number
private val multiples = (1..N).associateWith { sortedSetOf(1) }

/**
 * Asks the server for the gcd of two numbers.
 * The query is cached and validated so a real petition is made only when strictly necessary
 * Returns null if the petition is not possible (no more questions to ask)
 */
private fun ask(a: Int, b: Int): Int? {
    // same number, invalid, return 1 because we know nothing
    if (a == b) return 1
    // already asked, return it
    if (a to b in gcds) return gcds[a to b]

    // no more questions left to ask
    if (q == Q - 1)
        return null
    q++

    // ask and save
    val gcd = communicate("? $a $b").toInt()
    gcds[a to b] = gcd
    gcds[b to a] = gcd

    // if a number has 6 as gcd, it is multiple of 1, 2, 3 and 6
    multiples.getValue(a) += gcd.divisors()
    multiples.getValue(b) += gcd.divisors()

    return gcd
}

/**
 * Main function
 */
private fun whereAreThePrimesAt() {

    // Prime numbers that can be found more than one as divisors of numbers 1-100
    val factors = listOf(2, 3, 5, 7).associateWith { null as Int? }.toMutableMap()

    // find at least one factor for each
    while (factors.any { it.value == null }) {

        // ask randomly
        val i = (1..N).random()
        val j = (1..N).random()
        val gcd = ask(i, j) ?: continue

        factors.filter { it.value == null }.keys.forEach { f ->
            // found, keep
            if (f in gcd.divisors()) factors[f] = i
        }
    }

    // now ask those with the rest
    factors.values.filterNotNull().forEach { v ->
        (1..N).forEach { n -> ask(v, n) }
    }

    // we now know all the primes, except those factors

    // find 7, which is one of the one who only returned 1 or 7 but those may be 7, 49, 77 or 91
    // first ask with those who are multiple of 14, to find 49
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 7).toString() }.keys) {
        for (p in multiples.filter { 2 in it.value && 7 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 7) continue@k
        }
    }
    // now ask with those who are not multiple of 2, 3, 5 nor 7, to find 77 and 91
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 7).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 7) continue@k
        }
    }
    // we should have now found the 7

    // find 5, which is one of the one who only returned 1 or 5 but those may be 5, 25, 55, 65 or 85
    // first ask with 77, to find 55
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 7 in it.value && 11 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    // now ask with 91, to find 65
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 7 in it.value && 13 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    // now ask with 15 or multiple, to find 25 (because of 75)
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 5 in it.value && 3 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    // now ask with those who are not multiple of 2, 3, 5 nor 7, to find 85
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    // we should have now found the 5

    // find 3, which is one of the one who only returned 1 or 3 but those may be multiples of 9 * 11, 13, 17, 19, 23, 29 and 31
    // ask with multiples of 11, 13, 17, 19, 23, 29, 31 to find those
    for (i in listOf(11, 13, 17, 19, 23, 29, 31)) {
        k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
            for (p in multiples.filter { i in it.value }.keys) {
                val gcd = ask(k, p) ?: break@k
                if (gcd != 1 && gcd != 3) continue@k
            }
        }
    }
    // now ask with those who are not multiple of 2, 3, 5 nor 7, to find the others except some powers of 3
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 3) continue@k
        }
    }
    // and finally ask the remaining with themselves to find the powers of 3
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
        for (p in multiples.filter {
            it.value.toString() == setOf(1, 3).toString()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 3) continue@k
        }
    }
    // we should have now found the 3


    // find 2, which is one of the one who only returned 1 or 2 but those may be any even number
    // ask with multiples of 11, 13, 17, 19, 23, 29, 31 to find those powers
    for (i in listOf(11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47)) {
        k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
            for (p in multiples.filter { i in it.value }.keys) {
                val gcd = ask(k, p) ?: break@k
                if (gcd != 1 && gcd != 2) continue@k
            }
        }
    }
    // now ask with those who are not multiple of 2, 3, 5 nor 7, to find most of the others
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 2) continue@k
        }
    }
    // and finally ask the remaining with themselves to find the powers of 2
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
        for (p in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 2) continue@k
        }
    }

    // the numbers which only returned 1 or 1 and another are the solution (probably)
    val probable = multiples.filter { it.value.size <= 2 }.keys.take(26)
    println("Q=$q")

    // lets hope for the best
    communicate("! " + probable.sorted().joinToString(" "))

}

/**
 * GCD of two numbers
 */
private fun gcd(a: Int, b: Int): Int {
    var n1 = a
    var n2 = b

    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }

    return n1
}

/**
 * All the divisors of a number
 */
private fun Int.divisors(): Set<Int> {
    val result = mutableSetOf(1, this)
    for (i in 2 until this) {
        if (this % i == 0) result += i
    }
    return result
}

//----------------- testing -----------------//

/**
 * The most basic interactive prompt
 */
private fun prompt() {
    while (true) {
        println(readReady())
        readLine().let {
            println("> $it")
            when (it) {
                "" -> Unit
                "_primes" -> whereAreThePrimesAt()
                // exit
                "_exit" -> return
                // send
                else -> out.println("$it")
            }
        }
    }
}

//----------------- main -----------------//


fun main() {

    readReady()
    whereAreThePrimesAt()

//    prompt()
    closeAll()
}