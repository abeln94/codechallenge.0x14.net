import java.io.File
import java.math.BigInteger

val players = listOf("Edu", "Alberto")

val THREE = BigInteger.valueOf(3)

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {

    // prepare output file
    File("C17_${data}_out.txt").printWriter().use { out ->
        // prepare input file
        File("C17_${data}_in.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { case ->
                inp.readLine().toInt() // n, ignored
                val originalPiles = inp.readLine().split(" ").map { it.toBigInteger() }

                // find the outcome of each pile (see test below)
                val values = originalPiles.map { it.mod(THREE).toInt() }
//                println("$case -> $values")

                // the remainder decides the outcome of that pile for us (first player)
//                val loses = values.count { it == 0 } // piles we will lose, unused
                val wins = values.count { it == 1 } // piles we will win
                val decides = values.count { it == 2 } // piles where we can decide

                // in order to know who can win, we use a similar %3 approach
                val winner = when (decides % 3) {
                    // we lose, but if the number of wins is odd we needed to lose, so we win!
                    0 -> 1 - (wins % 2)
                    // we win, unless we needed to lose
                    1 -> wins % 2
                    // the other decides, so unfortunately chooses to win and we lose
                    2 -> 1
                    else -> throw Exception()
                }

                // report output
                out.println("Case #${case}: ${players[winner]}")
            }
        }
    }
}


private fun test() {
    // empirical test for 1 pile
    // was found by trying and checking outcomes, and it appears it is true always
    // if you divide the pile number by 3, the remainder tells you who will win

    // the outcome of the first player for that pile
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

    // visualization
    fun convertDecimal(decimal: Int): String {
        var binary = ""
        var res = decimal
        while (res != 0) {
            binary = (res % 2L).toString() + binary
            res /= 2
        }
        return binary.padStart(8, '0')
    }
    println(state.map {""+ convertDecimal(it.key) + " -> " + (if (it.value) "WIN" else "lose") }.joinToString("\n"))

    // some element is not what we expect?
    println(state.filter { (it.key % 3 == 1) != it.value }) // result, none

    // conclusion: works (at least for very big numbers)
    // divide the number by 3, take the remainder, assuming the other player plays perfectly:
    // if 0, you always lose (if you want to win, you can't, if you want to lose, you lose)
    // if 1, you always win (if you want to win, you win, if you want to lose, you can't)
    // if 2, you can decide the outcome, win or lose
}