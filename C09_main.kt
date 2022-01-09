import java.io.BufferedReader
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {
    // prepare output file
    File("C9_${data}_out.txt").printWriter().use { out ->
        var index = 1
        // prepare input file
        File("C9_${data}_in.txt").bufferedReader().use { inp ->

            // get number of cases
            val t = inp.readLine().toInt()

            // read sprite data
            val originSprites = (1..inp.readLine().toInt()).map { d ->
                // the size
                val (w, h) = inp.readLine().splitInts()
                // and the data
                Sprite(
                    inp.readLines(h).map { it.split("").filter { it.isNotBlank() }.map { it == "1" } },
                    0, 0, w, h
                )
            }

            // now each case
            (1..t).forEach { _ ->

                // read the positions and generate the list of located sprites
                val sprites = (1..inp.readLine().toInt()).map { p ->
                    val (i, x, y) = inp.readLine().splitInts()
                    // create a copy of the sprite at that location
                    originSprites[i].move(x, y)
                }

                // now count all collision
                // this is slow, we match all pairs (over a million in the last case!)
                // but a few seconds of computer time is (for this contest!) better than spending minutes trying to optimize it ourselves with spatial hashing or other methods
                // this is not so unoptimized anyway
                val collisions = sprites.allPairs().count { (s1, s2) ->
                    // for each pair, we calculate the matching bounding box
                    val left = max(s1.left, s2.left)
                    val top = max(s1.top, s2.top)
                    val right = min(s1.right, s2.right)
                    val bottom = min(s1.bottom, s2.bottom)

                    // and for each element in that box, we check a collision
                    // empty bounding boxes will skip these loops automatically
                    (left until right).any { x ->
                        (top until bottom).any { y ->
                            s1[x, y] && s2[x, y]
                        }
                    }
                }

                // report output
                out.println("Case #${index++}: $collisions")
            }
        }
    }
}

/**
 * A sprite, raw data of each pixel and its bounding box location
 */
data class Sprite(val data: List<List<Boolean>>, val left: Int, val top: Int, val right: Int, val bottom: Int) {
    /**
     * returns a copy of the sprite moved x,y
     */
    fun move(x: Int, y: Int) = Sprite(data, left + x, top + y, right + x, bottom + y)

    /**
     * returns the pixel value in that position (should be inside the sprite, no null checks yet)
     */
    operator fun get(x: Int, y: Int) = data[y - top][x - left]
}

/**
 * Splits a string into a space separated list of integers
 */
private fun String.splitInts() = split(" ").map { it.toInt() }

/**
 * Lists all unique pairs from this list, all combinations (no permutations)
 * Optimized to be a sequence for faster and less memory-hungry later computations
 */
fun <T> List<T>.allPairs() = asSequence()
    .flatMapIndexed { i, e1 -> ((i + 1) until size).map { j -> e1 to get(j) } }

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()
