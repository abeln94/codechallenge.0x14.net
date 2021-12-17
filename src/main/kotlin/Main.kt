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
    val probable = (1..N).associateWith { (1..N).toMutableSet() }

    val found = mutableSetOf<Int>()

    while (true) {

        val (i, j) = (1..N).flatMap { i ->
            (1..N).map { j ->
                (i to j) to (probable.getValue(i) intersect probable.getValue(j)).size
            }
        }.allMaxBy { it.second }.map { it.first }.random()

        if (i == j) continue

        val gcd = communicate("? $i $j").toInt()
        probable.getValue(i) -= gcd.coprime().toSet()
        probable.getValue(j) -= gcd.coprime().toSet()

        while (true) {
            probable.filter { it.value.size == 1 }.filter { it.value.random() !in found }
                .takeUnless { it.isEmpty() }
                ?.forEach { n ->
                    found += n.value.random().also { println("FOUND $it") }
                    probable.filter { it.key != n.key }.forEach { it.value -= n.value.random() }
                } ?: break
        }

        if (probable.any { it.value.isEmpty() }) throw Exception()

        println(probable)
        println("unknowns remaining: " + (probable.count { it.value.size > 1 }))

        if (q++ == Q) communicate("! 1").also { return }

        if (probable.values.none { it.size != 1 }) {
            communicate(
                "! " + probable.filter { it.value.random().isPrime() }.map { it.key }
                    .joinToString(" ")
            )
            return
        }


    }
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

private fun Int.mutiples(max: Int): Set<Int> {
    val result = mutableSetOf(this)
    for (i in 2 until max / this) {
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
                "_primes" -> whereAreThePrimesAt()
                // exit
                "_exit" -> return
                // send
                else -> out.println("? $it")
            }
        }
    }
}

//----------------- main -----------------//


fun main() {

    val pairs = (1..N).associateWith { n ->
        (1..N).filter { it != n }.map { gcd(it, n) }.groupBy { it }.map { it.key to it.value.size }.toMap()
    }

    println(pairs)

    pairs.forEach { (n, map) ->
        println(
            "$n is " +
                    pairs.filterValues { it.toString() == map.toString() }.keys

        )
    }

    return

    prompt()
    closeAll()
}

fun <T> List<T>.indexesOf(filter: (T) -> Boolean) = withIndex().filter { filter(it.value) }.map { it.index }