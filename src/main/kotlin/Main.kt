import java.io.File
import java.math.BigInteger

val players = listOf("Edu", "Alberto")

val THREE = BigInteger.valueOf(3)

fun main() {

    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { case ->
                inp.readLine().toInt() // n, ignored
                val originalPiles = inp.readLine().split(" ").map { it.toBigInteger() }
                val winner: Int

                // find the outcome of each pile (see test below)
                val values = originalPiles.map { it.mod(THREE).toInt() }
                println("$case -> $values")

//                val loses = values.count { it == 0 }
                val wins = values.count { it == 1 }
                val decides = values.count { it == 2 }

                val canDecide = decides % 3 != 0

                winner = if (canDecide) 0
                else 1 - (wins % 2)

                // report output
                out.println("Case #${case}: ${players[winner]}")
            }
        }
    }
}

data class State(val piles: List<BigInteger>, val player: Int)

val steps = ArrayDeque<State>()


fun test() {
    // empirical test

    val state = mutableMapOf(0 to false, 1 to true)
    n@ for (n in 2 until 999) {
        var i = 1
        while (i <= n) {
            if (state[n - i] == true) {
                state[n] = false
                continue@n
            }
            i *= 2
        }
        state[n] = true
    }

    fun convertDecimal(decimal: Int): String {
        var binary = ""
        var res = decimal
        while (res != 0) {
            binary = (res % 2L).toString() + binary
            res /= 2
        }
        return binary.padStart(8, '0')
    }

    //    println(state.map {""+ convertDecimal(it.key) + " -> " + (if (it.value) "WIN" else "lose") }.joinToString("\n"))

    println(state.filter { (it.key % 3 == 1) != it.value }) // result, none

    // conclusion: should work
    // divide the number by 3, take the remainder, assuming the other player plays perfectly:
    // if 0, you always lose (if you want to win, you can't, if you want to lose, you lose)
    // if 1, you always win (if you want to win, you win, if you want to lose, you can't)
    // if 2, you can decide the outcome, win or lose
}