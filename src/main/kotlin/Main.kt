import java.io.BufferedReader
import java.io.File

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        var index = 1
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->
            // each case
            (1..inp.readLine().toInt()).forEach { n ->
                // read data
                val t = inp.readLine().toInt()
                val tickets = inp.readLines(t).map { it.split(",").let { (f, s) -> f to s } }
                val cities = tickets.flatMap { it.toList() }.distinct()

                // Creating first example graph
                val V = cities.size
                val adj1 = ArrayList<ArrayList<Int>>()
                for (i in 0 until V) adj1.add(ArrayList())
                tickets.forEach { (a, b) ->
                    Graph.addEdge(adj1, cities.indexOf(a),cities.indexOf(b))
                }
                val result = Graph.AP(adj1, V).map { cities[it] }.sorted().takeIf { it.isNotEmpty() }?.joinToString(",") ?: "-"

                // report output
                out.println("Case #${index++}: $result")
            }
        }
    }
}

/**
 * Read multiple lines
 */
private fun BufferedReader.readLines(p: Int) = (1..p).map { readLine() }.toMutableList()


