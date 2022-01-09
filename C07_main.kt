import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*

//----------------- socket -----------------//

// prepare
private val pingSocket = Socket("codechallenge-daemons.0x14.net", 4321)
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

//----------------- labyrinth -----------------//

// visited cells, to avoid repeating
private val visited = mutableSetOf<Pair<Int, Int>>()

// a state of the process
private data class State(val x: Int, val y: Int, val direction: String?, val path: List<String>)

// states to check, fifo list (so that the solution is the shortest).
private val states = ArrayDeque<State>()

/**
 * Solves the labyrinth
 */
private fun labyrinth() {
    // skip introduction
    readReady()
    // initialize
    states.add(State(0, 0, null, mutableListOf()))
    // check all states in order
    while (states.isNotEmpty()) {
        // get next position
        var (x, y, direction, path) = states.remove()

        // traverse where needed (unless this is the first state)
        if (direction != null) {
            // calculate new cell
            val (nx, ny) = when (direction) {
                "west" -> x + 1 to y
                "east" -> x - 1 to y
                "north" -> x to y + 1
                "south" -> x to y - 1
                else -> throw Exception()
            }
            // skip if already visited
            if (nx to ny in visited) continue
            // go there
            communicate("go to $x,$y")
            communicate(direction)
            println("I think I'm in $nx $ny")
            x = nx
            y = ny
        }
        // mark as visited and calculate new path
        visited += x to y
        val newPath = path + "($x,$y)"
//        val newPath = path + "($x, $y)" // replace the previous line with this one for the fix

        // check exit
        if (!communicate("is exit?").startsWith("No")) {
            // exit! log path and exit
            File("C7_out.txt").printWriter().use { out ->
                out.write(newPath.joinToString(", "))
            }
            return
        }
        // no exit, look and prepare next states
        communicate("look").substringAfterLast(": ").split(" ")
            .forEach { newDirection ->
                states.add(State(x, y, newDirection, newPath))
            }

    }
}

//----------------- testing -----------------//

/**
 * The most basic interactive prompt
 */
private fun prompt() {
    while (true) {
        println(readReady())
        readLine().let {
            when (it) {
                // run the program, should be run while in 0,0
                "_labyrinth" -> labyrinth()
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
    labyrinth()
//    prompt()
//    closeAll()
}
