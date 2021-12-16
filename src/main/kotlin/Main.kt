import java.io.File

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { case ->
                println(case)
                val line = inp.readLine()

                val operands = mutableListOf<String>()
                val operations = mutableListOf<(Double, Double) -> Double>()
                line.split(" ").forEach { token ->
                    when (token) {
                        "+" -> operations += { a, b -> a + b }
                        "-" -> operations += { a, b -> a - b }
                        "*" -> operations += { a, b -> a * b }
                        "/" -> operations += { a, b -> a / b }
                        "=" -> operations += { a, b -> if (a == b) 1.0 else 0.0 }
                        else -> operands += token
                    }
                }

                val letters = operands.flatMap { it.toCharArray().toList() }.distinct().filter { it.isLetter() }

                val solution = solve(
                    line,
                    listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                    letters, operands, operations
                ).sorted().joinToString(";").ifEmpty { "IMPOSSIBLE" }

                // report output
                out.println("Case #${case}: $solution")
            }
        }
    }
}

fun solve(
    line: String,
    digits: List<Int>,
    letters: List<Char>,
    operands: List<String>,
    operations: MutableList<(Double, Double) -> Double>
): List<String> {
    if (letters.isEmpty()) {
        val parsedOperands = operands.map { it.toDouble() }.toMutableList()
        val result = operations.fold(parsedOperands.removeFirst()) { currentOperand, operation -> operation(currentOperand, parsedOperands.removeFirst()) }
        return if (result == 1.0) listOf(line) else emptyList()
    }
    val letter = letters[0]
    return (if (operands.any { it.startsWith(letter) }) digits.filter { it != 0 } else digits).flatMap { number ->
        fun String.replace() = replace(letter, number.digitToChar())

        solve(line.replace(), digits - number, letters.drop(1), operands.map { it.replace() }, operations)
    }
}