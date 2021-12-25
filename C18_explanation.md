# Challenge 18 - Bit Saver
- Contest: https://codechallenge.0x14.net/Challenges?id=18
- Author: Abel Naya

## Explanation
For this challenge we are required to compress a file using a specific algorithm that converts commands into unique binary sequences based on a dictionary. The algorithm is called [Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding), although we are also required to minimize the distance between the shorter and longer prefix which means we need to use a 'minimum variance Huffman coding' algorithm.

As with other previous challenges, you need to balance between writing them yourself or finding and using an existing implementation. For this challenge I searched for existing variants, and only found a valid one in the [Matlab programming language](https://es.mathworks.com/help/comm/ref/huffmandict.html). The correct way to proceed would be to write the program in Matlab, however my experience with Matlab is limited and the time it was taking for me to find the syntax and functions was too much, and there wasn't much remaining time.

So I decided to do a multi-programming solution (which is preferable to avoid, except maybe in cases like this one where time is very limited). The solution is:
1) A kotlin function parses the input and writes a valid Matlab program to solve the huffman problem with the required input.
2) That Matlab program is then run, which solves the huffman problem for each case, and writes the results on a rudimentary csv file.
3) A second Kotlin function reads these csv (and the original input for extra data) and converts them to the required output of the original problem.

The Kotlin file is then a converter between the program input/output and Matlab's huffman function input/output.