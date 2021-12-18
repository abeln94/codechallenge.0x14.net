/*
 * This problem was the hardest BY FAR
 * Lots of tests, lots of failures, sorry for all the petitions
 * in the end it was an algorithm somehow like:
 * 1) trying to find a 2,3,5,7 multiple
 * 2) do those with all the other
 * 3) those who always returned 1 are the solution, but we still need 2,3,5,7
 * 4) for each of them try to use the ones already discovered to discard multiples
 * 5) and hope for the best
 *
 * sorry for the mess, but I can't spend much more time, no beautiful comments here, sorry, this is the state of the program as it was just when the served returned 'Congratulations'
 * (except for this header of course)
 */



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
var q = 0

const val primes = 25 // there are 25 primes between 1 and 100

val gcds = mutableMapOf<Pair<Int, Int>, Int>()

val multiples = (1..N).associateWith { sortedSetOf(1) }

//possible.map { (k, v)->
//    "$k -> $v"
//}.joinToString("\n")

fun ask(a: Int, b: Int): Int? {
    if (a == b) return 1
    if (a to b in gcds) return gcds[a to b]

    if (q == Q - 1)
        return null

    q++
    val gcd = communicate("? $a $b").toInt()
    gcds[a to b] = gcd
    gcds[b to a] = gcd

    multiples.getValue(a) += gcd.divisors()
    multiples.getValue(b) += gcd.divisors()

    return gcd
}

fun whereAreThePrimesAt() {

    val factors = listOf(2, 3, 5, 7).associateWith { null as Int? }.toMutableMap()

    // find at least one factor for each
    while (factors.any { it.value == null }) {

        val i = (1..N).random()
        val j = (1..N).random()

        val gcd = ask(i, j) ?: continue

        factors.filter { it.value == null }.keys.forEach { f ->
            if (f in gcd.divisors()) factors[f] = i
        }
    }

    // now ask those with the rest
    factors.values.filterNotNull().forEach { v ->
        (1..N).forEach { n -> ask(v, n) }
    }

    // we now know all the primes, except those factors

    //multiples.map { ""+it.key+" -> "+it.value.sorted() }.joinToString("\n")

    // get 7
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 7).toString() }.keys) {
        for (p in multiples.filter { 2 in it.value && 7 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 7) continue@k
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 7).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 7) continue@k
        }
    }

    // get 5
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 7 in it.value && 11 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 7 in it.value && 13 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }

    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter { 5 in it.value && 3 in it.value }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 5).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 5) continue@k
        }
    }

    // get 3
    for (i in listOf(11, 13, 17, 19, 23, 29, 31)) {
        k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
            for (p in multiples.filter { i in it.value }.keys) {
                val gcd = ask(k, p) ?: break@k
                if (gcd != 1 && gcd != 3) continue@k
            }
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 3) continue@k
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 3).toString() }.keys) {
        for (p in multiples.filter {
            it.value.toString() == setOf(1, 3).toString()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 3) continue@k
        }
    }


    // get 2
    for (i in listOf(11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47)) {
        k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
            for (p in multiples.filter { i in it.value }.keys) {
                val gcd = ask(k, p) ?: break@k
                if (gcd != 1 && gcd != 2) continue@k
            }
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
        for (p in multiples.filter {
            (it.value intersect setOf(2, 3, 5, 7)).isEmpty()
        }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 2) continue@k
        }
    }
    k@ for (k in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
        for (p in multiples.filter { it.value.toString() == setOf(1, 2).toString() }.keys) {
            val gcd = ask(k, p) ?: break@k
            if (gcd != 1 && gcd != 2) continue@k
        }
    }

//    val probable = multiples.filter { it.value.size == 1 }.keys.toList() + listOf(2, 3, 5, 7, 11).map { a -> multiples.filter { it.value.toString() == sortedSetOf(1, a).toString() }.keys.random() }
    val probable = multiples.filter { it.value.size <= 2 }.keys.take(26)

    println("Q=$q")

    communicate("! " + probable.sorted().joinToString(" "))

    Unit

}

private fun <E> List<E>.allMaxBy(mapper: (E) -> Int): List<E> {
    val max = this.maxOfOrNull(mapper)
    return filter { mapper(it) == max }
}

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

private fun Int.coprime(): List<Int> {
    if (this == 1) return emptyList()
    return (1..N).filter { gcd(it, this) == 1 }
}


private fun Int.divisors(): Set<Int> {
    val result = mutableSetOf(1, this)
    for (i in 2 until this) {
        if (this % i == 0) result += i
    }
    return result
}

private fun Int.mutiples(): Set<Int> {
    val result = mutableSetOf(this)
    for (i in 2 until N / this) {
        result += this * i
    }
    return result
}

fun Int.isPrime(): Boolean {
    var flag = true
    for (i in 2..this / 2) {
        // condition for nonprime number
        if (this % i == 0) {
            flag = false
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

    prompt()
    closeAll()
}

fun <T> List<T>.indexesOf(filter: (T) -> Boolean) = withIndex().filter { filter(it.value) }.map { it.index }