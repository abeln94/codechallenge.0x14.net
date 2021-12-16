import java.io.File

// I'm not very proud of this, but it works, and not very slow, so good enough (I need more timeeeeee!)

fun main() {
    // prepare output file
    File("output.txt").printWriter().use { out ->
        // prepare input file
        File("input.txt").bufferedReader().use { inp ->

            // for each case
            (1..inp.readLine().toInt()).forEach { case ->
                println(case)

                // read line
                val line = inp.readLine()

                // operations with both multiplications and additions are not supported
                // operations will be applied from left to right
                // fortunately it doesn't seem to be any
                if (("*" in line || "/" in line) && ("+" in line || "-" in line)) throw Exception("Not supported")

                // only operations ending in the equality are supported
                if(line.substringAfterLast("=").trim().any{ !it.isLetterOrDigit()}) throw Exception("Not supported")

                // extract operands and operations in order (will be evaluated as a stack)
                val operands = mutableListOf<String>()
                val operations = mutableListOf<(Double, Double) -> Double>()
                line.split(" ").forEach { token ->
                    when (token) {
                        "+" -> operations += { a, b -> a + b }
                        "-" -> operations += { a, b -> a - b }
                        "*" -> operations += { a, b -> a * b }
                        "/" -> operations += { a, b -> a / b }
                        // the equality operation is somewhat codified: 1=true, 0=false
                        "=" -> operations += { a, b -> if (a == b) 1.0 else 0.0 }
                        else -> operands += token
                    }
                }

                // extract letters that will need to be replaced
                // all distinct letters present in the operands
                val letters = operands
                    .flatMap { it.toCharArray().toList() }
                    .distinct()
                    .filter { it.isLetter() }

                // get the solutions by solving the equation
                val solution = solve(
                    // of this line
                    line,
                    // using these digits
                    listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                    // replacing these characters
                    letters,
                    // consisting of these operands and operations
                    operands, operations
                )
                    // then sort the solutions
                    .sorted()
                    // and join them
                    .joinToString(";")
                    // special case if there are no solutions
                    .ifEmpty { "IMPOSSIBLE" }

                // report output
                out.println("Case #${case}: $solution")
            }
        }
    }
}

/**
 * The function that will solve an equation
 */
fun solve(
    // this is the raw line of the equation, for faster return when a solution is found
    line: String,
    // the digits that are still not used
    digits: List<Int>,
    // the letters that are still not assigned
    letters: List<Char>,
    // list of operands
    operands: List<String>,
    // list of operations
    operations: MutableList<(Double, Double) -> Double>
): List<String> {

    if (letters.isEmpty()) {
        // all letters were replaced, time to check if the mapping is a solution

        // convert the operands
        val parsedOperands = operands.map { it.toDouble() }.toMutableList()

        // and apply the operations in order
        val result = operations.fold(parsedOperands.removeFirst()) { currentOperand, operation ->
            operation(currentOperand, parsedOperands.removeFirst())
        }

        // the last operation is always an equality, so the mapping is valid only if it returned 1.0
        return if (result == 1.0) listOf(line) else emptyList()
    }
    // no solution, continue

    // get next letter to replace
    val letter = letters[0]
    // and possible replacements (exclude 0 if an operand starts with it)
    val replacementDigits =
        if (operands.any { it.startsWith(letter) }) digits.filter { it != 0 }
        else digits

    // and for each replacement, solve it and return the merged solutions
    return replacementDigits.flatMap { digit ->
        solve(
            // the line with the replacement
            line.replace(letter, digit.digitToChar()),
            // one less digit to choose
            digits - digit,
            // one less letter to replace
            letters.drop(1),
            // operands replaced
            operands.map { it.replace(letter, digit.digitToChar()) },
            // same operations
            operations
        )
    }
}