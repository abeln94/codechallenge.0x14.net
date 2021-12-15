import java.io.BufferedReader
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        var index = 1
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->
            // each case
            val t = inp.readLine().toInt()

            // read sprites
            val unlocatedSprites = (1..inp.readLine().toInt()).map { d ->
                val (w, h) = inp.readLine().splitInts()
                Sprite(inp.readLines(h).map { it.split("").filter { it.isNotBlank() }.map { it=="1" } }, 0, 0, w, h)
            }

            (1..t).forEach { _ ->

                val sprites = (1..inp.readLine().toInt()).map { p ->
                    val (i, x, y) = inp.readLine().splitInts()
                    unlocatedSprites[i].move(x, y)
                }

                val result = sprites.allPairs().count { (s1, s2) ->
                    val left = max(s1.left, s2.left)
                    val top = max(s1.top, s2.top)
                    val right = min(s1.right, s2.right)
                    val bottom = min(s1.bottom, s2.bottom)

                    (left until right).any { x ->
                        (top until bottom).any { y ->
                            s1[x, y] && s2[x, y]
                        }
                    }
                }

                // report output
                out.println("Case #${index++}: $result")
            }
        }
    }
}

data class Sprite(val data: List<List<Boolean>>, val left: Int, val top: Int, val right: Int, val bottom: Int) {
    fun move(x: Int, y: Int) = Sprite(data, left + x, top + y, right + x, bottom + y)
    operator fun get(x: Int, y: Int) = data[y - top][x - left]
}

fun String.splitInts() = split(" ").map { it.toInt() }

fun <T> List<T>.allPairs() = flatMapIndexed { i, e1 -> ((i + 1) until size).map { j -> e1 to get(j) } }

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
