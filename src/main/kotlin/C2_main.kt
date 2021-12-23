import java.io.BufferedReader
import java.io.File

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data:String) {
    // prepare output file
    File("C2_${data}_out.txt").printWriter().use { out ->
        var index = 1
        // prepare input file
        File("C2_${data}_in.txt").bufferedReader().use { inp ->
            // each case
            (1..inp.readLine().toInt()).forEach { n ->
                // read data
                val (p, r, c) = inp.readLine().split(" ").map { it.toInt() }
                val pokemons = inp.readLines(p)
                var text = inp.readLines(r).joinToString("").replace(" ", "")

                // iterate
                pokemon@while (pokemons.isNotEmpty()) {
                    for (pokemon in pokemons) {
                        if (pokemon in text) {
                            // remove if found
                            text = text.replace(pokemon, "")
                            pokemons.remove(pokemon)
                            continue@pokemon
                        } else if (pokemon.reversed() in text) {
                            // remove if found reversed
                            text = text.replace(pokemon.reversed(), "")
                            pokemons.remove(pokemon)
                            continue@pokemon
                        }
                    }
                    // just in case
                    throw Exception("InvalidInput")
                }

                // report output
                out.println("Case #${index++}: $text")
            }
        }
    }
}

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()


