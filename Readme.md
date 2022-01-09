# Code challenge 1 (by Telefonica)
- Url: https://codechallenge.0x14.net/
- Author: Abel Naya

This repository contains all the files I made while solving the challenge.
All the files of each challenge are prefixed by C{index}. In most cases there is a kotlin code file (CX_main.kt) and the rest are input/output data required for the program (most of them obtained from the challenge page itself).
This readme file explains the procedure, solution and other minor comments about how I personally solved each problem. It is assumed you already know the problems statements or have access to them.

The git history tells with more details when I solved each, and maybe other interesting changes. I was only able to spend time on afternoons after work, and I solved the last one a few hours before the deadline. In the end I failed 3 challenges. One because I didn't notice a failure on my algorithm (fixable with a one-line), other which I couldn't solve for very hard inputs (not present in the test phase) and another because I literally removed a space while cleaning the code (and this was an impossible-to-fail challenge because the test and submit phases were the same, but that space made the submission wrong). Ranking position 19. It was a hard but very engaging and interesting contest.

If you want to run any of the files just clone the repository or copy the files and set them in the src/main/kotlin of a new project. Then, either move the non-kotlin files to the root folder, or run the programs directly in that folder as a working directory. You can also try running them on Kotlin playground, but some modifications to read input from code instead of file will be probably required.

Also, feel free to ask any questions, either as an issue, as a discussion or even an email. [Pueden ser también en español, que en realidad es mi lengua materna, el uso del inglés es por costumbre a la hora de programar]


## Challenge 1 - Roll the dice!
- Link: https://codechallenge.0x14.net/Challenges?id=1

### Explanation
Just a warmup problem: split each line by ':' and add both sides +1 (unless they add to 12, in which case the output is '-').

### Notes
For some reason the file I saved doesn't have comments, but I remember writing them in all challenges. I have redone them again, but may differ from the actual submission.



## Challenge 2 - Catch them all
- Link: https://codechallenge.0x14.net/Challenges?id=2

### Explanation
Another warmup:
1) Read the data: save the text and all the pokemons on a mutable list.
2) Iterate for each Pokemon until the list is empty 
   1) If the Pokemon is in the text, remove (both from the text and from the list) and repeat step 2
   2) If the Pokemon is in the list as reversed, remove (both from the text reversed and from the list) and repeat step 2
3) Write the remaining text as output



## Challenge 3 - The night of the hunter
- Link: https://codechallenge.0x14.net/Challenges?id=3

### Explanation
This one requires a parser of different formats, and I had fun writing my own.
1) Read the data, both strings and the letter values
2) Parse the letter values. The divisions make the text not json nor other common formats valid. One solution could be to replace any `\d+/\d+` (two numbers separated by a slash) with the result, and then parse. My solution is more generic:
   1) If the string starts with '{', mark as (`1`,`","`,`":"`)
   2) If the string starts with '[', mark as (`2`,`"),("`,`","`)
   3) else, mark as (`0`,`","`,`"="`)
   4) Now you have the text and three parameters. Remove the `first` n characters from both ends of the string, then split by `second` and each one split again by `third`. You can now associate each character by its value (if the value contains a slash, split again and divide). Example: `{'a': 2/3, 'e': 4, 'h': 3, 'l': 5, 'o': 1, 't': 6, 'v': 0}` is (`1`,`","`,`":"`) so --> `'a': 2/3, 'e': 4, 'h': 3, 'l': 5, 'o': 1, 't': 6, 'v': 0` --> `'a' 2/3` ` 'e': 4` ` 'h': 3` ` 'l': 5` ` 'o': 1` ` 't': 6` ` 'v': 0` --> `'a'` ` 2/3`, ` 'e'` ` 4`, ` 'h'` ` 3`, ` 'l'` ` 5`, ` 'o'` ` 1`, ` 't'` ` 6`, ` 'v'` ` 0`
3) For each word, map each character by its value and add them.
4) Return the word with the bigger score, or '-' if equal



## Challenge 4 - Let’s build musical scales
- Link: https://codechallenge.0x14.net/Challenges?id=4

### Explanation
Note: the submitted solution is wrong, it fails with some specific inputs, an explanation and fix is detailed below.

Constant: notes dictionary: `[["A"],["A#", "Bb"],["B", "Cb"],["C", "B#"],["C#", "Db"],["D"],["D#", "Eb"],["E", "Fb"],["F", "E#"],["F#", "Gb"],["G"],["G#", "Ab"]]`

The algorithm is as follows:
1) Read the data, the initial note and the steps.
2) Create an array of 'indexes' from the notes dictionary. For example if the first letter is `g` (index 10) and the steps are sT (+1,+2) build the array `[10,11,1]`
3) Recursively test each note to find a solution which doesn't repeat any letter for different notes. In the example above it will search for "GG#" [error] then "GAbA#" [error] and finally "GAbBb" [success]. Return that solution.

This algorithm fails if the initial note is one of the second elements in the notes dictionary (Bb, Cb, etc) because the algorithm will search using the first note first, and the returned scale will be the same one musically, but different textually. For example G#A#B#C#D#E#F#G# instead of AbBbCDbEbFGbAb. The solution requires to keep the first note without modifications. The code contains the one-line fix.



## Challenge 5 - Invictus
- Link: https://codechallenge.0x14.net/Challenges?id=5

### Explanation
I stumbled on this one for quite some time, but in the end it was far easier than I had though.
The file contains extra UTF8 characters outside the usual range of common english letters (0-128). You can see them with a hex editor.

The solution implemented was by trial and error, manually checking the list of bytes value and trying to find an operation (byte+x or x-byte) that converted them to something with 'MANDELA'. After several tests, the conversion 'byte - 56366' (using only the bytes greater than 128 and not 56128) returned the correct result.



## Challenge 6 - What day is it?
- Link: https://codechallenge.0x14.net/Challenges?id=6

### Explanation
The parsing of a date to calculate its day of week is a problem you should (probably) never attempt yourself due to all the [corner cases and different calendars](https://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week). In Java, we have [LocalDate#getDayOfWeek()](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#getDayOfWeek--) which allows to parse any date in ISO format (the input) and return the day of week in any of the [multiple supported locales](https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html).

The only difficulty is that the input contains a reduced set of languages and using 2 letters in a not supported format (not ISO639 nor IETF BCP 47). For this reason a manual mapping of input->ISO639 codes was written, which both solves the issue of using only the specified languages and converting them to the correct format.

The other difference was that, for some reason, the `ț` character in the Romanian language is considered invalid, so after checking the web for alternatives I found `ţ` which seems to be the 'correct' one. Later, they gave a hint on Twitter about this.



## Challenge 7 - Escape or Die
- Link: https://codechallenge.0x14.net/Challenges?id=7

### Note
Even though the test and submissions are the same (so if you solve the test you know the submission should be right) I failed this challenge. The reason is that, after completing the challenge and checking it was valid, I refactored, cleaned and commented the code (to improve its quality). In the process I changed the string output converter and forgot a space between the (x, y), which made the output algorithmically correct, but textually invalid. (funny thing, for the latest challenges I specifically made sure the output before and after the refactoring was the same, unfortunately I didn't do it here). The code contains the one-line fix.

### Explanation
This challenge is a labyrinth solver, but using a remote connection instead of local data. The algorithm is a Breadth First Tree Traversal:
1) Initializes a lifo list of states with an initial element as (0, 0, null, []) (x=0, y=0, no direction, no cells visited).
2) While there are states, pop the most recent one (x,y,direction,visited) and:
   1) Calculate the next cell position (except for the first visited node where direction is null).
   2) If the next position is already in the visited list, skip and continue step 2 for next state
   3) send 'go to x,y' and 'direction'. Note: it could be possible that the next position was different that the calculated in step 1 (for example if the grid wasn't a matrix) but it seems it wasn't the case. If it were the command should be sent and the result parsed before the visited check.
   4) Mark the new cell as visited and send 'is exit?'
      1) If exit, print the visited cells and exit (which is the shortest path because of the fifo state list, aka breadth first traversal).
      2) If not exit, send 'look' and push a new state with each direction.



## Challenge 8 - Awesome Sales Inc.!
- Link: https://codechallenge.0x14.net/Challenges?id=8

### Explanation
The problem asks to find which cities, when removed, will make some paths impossible. This is the same as finding the nodes of an undirected graph which will split it into two separated non-connected subgraphs. Those special nodes are called articulation points (or cut vertices or separation vertices). This problem is common and you can find some solutions already available online. After some searching and reviewing I found [this solution](https://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/) which solves correctly the problem in Java.

The implemented solution parses the data and converts it to an adjacency matrix, which is then feed into the algorithm, and the result is then parsed and printed as specified.

Note: I could have implemented it myself, or probably could have found a library and use it instead, but this way the file can be executed without external dependencies which was faster for a time-limited challenge.



## Challenge 9 - Collisions
- Link: https://codechallenge.0x14.net/Challenges?id=9

### Explanation
This is a collision checker between several sprites on a grid. The implemented algorithm is as follows:
1) Parse the data into a list of 'sprites' which contains the sprite data (matrix of booleans) and the bounding box of the sprite (left, right, top and bottom sides).
2) For each unique pair of sprites (ignoring order, aka combinations) check if they collide:
   1) Calculate the collision bounding box, by taking the maximum of the left sides, the max of the tops, the min of the right and the min of the bottom.
   2) For each pixel in the collision bounding box, check if both sprites collide (both have a pixel there). Note that the collision bounding box may be empty, in that case there is no collision.
   3) Count all collisions and print the result.

Note: this algorithm is theoretically slow, compares all pairs of sprites even if they are far away (although in that case the collision bounding box is empty so no further checks are made). In order to improve the speed I could have implemented a spacial hashing division or other methods, however both inputs take a few seconds to compute and the optimization will probably take quite a few minutes to implement, so for a time-limited challenge I decided to just leave it like this.



## Challenge 10 - Packets delivery
- Link: https://codechallenge.0x14.net/Challenges?id=10

### Explanation
No source code this time, the challenge requires us to analyze a pcap file for an 'experimental' protocol (probably the funniest challenge to read). The steps I made were as follows:

1) Open wireshark, load the pcap file.

- There are multiple packages, all seems to contain a single byte of data, the protocol says that a IP packet is split and sent.

2) Extract all the data bytes, concatenate as bin hex (binary file), load in wireshark again ... nothing (the data doesn't seem to be valid).

- After a bit of testing and documentation, I saw the sequence number of the packages is not in timestamp order. I guess some 'avian carriers' were lazy or slower.

3) Sort the packets by sequence number instead of timestamp.

4) Extract all the data, concatenate as bin hex and load in wireshark again ... nothing. It seems the data is still not a valid IP dataframe.

- But then I notice that the binary file generated in the previous step ... is a qr image!

5) Scan the qr and get the solution. Bingo!



## Challenge 11 - ALOT Another library of tools
- Link: https://codechallenge.0x14.net/Challenges?id=11

### Explanation
For this challenge we are required to split a list of strings into groups of K elements, where the length of the common prefix in each group is maximum. A bruteforce solution (check every grouping, aka each combination) is infeasible, so instead the algorithm solves the problem directly as follows:

1) Initialize all strings into a list.
2) From the list of strings, take the K with the longer prefix (if multiple take just one, later we will take the others). This is done recursively starting with T=0:
   1) First group each line based on the T (T=0 means first) character (g->[get,group,...], s->[set, split,...], ...). If a string is shorter than T consider the character is a space.
   2) Take the group with the bigger size greater or equal to K (or any of them if multiple). If there are no groups with K or more strings, return any K element of the list, which have prefix length T, and stop.
   3) If all the strings are T characters length, return any K strings, which have prefix length T.
   4) Otherwise, repeat this process for T+1.
3) Remove those K strings from the list of strings, and mark its length (the algorithm already gives us the length), repeat step 2 if there are still strings in the list.
4) Once there are no more strings, the sum of the marked lengths is the solution.

Example of 1 iteration with K=2:
- T=0 `[getA,gotB,git,setC]` --> `g->[getA,gotB,git], s->[setC]` => take 'g' 
- T=1 `[getA,gotB,git]` --> `e->[getA], o->[gotB], i->[git]` --> no group with 2 or more strings, return any 2 `[getA,gotB]` with common prefix length T=1



## Challenge 12 - The crypto bubble
- Link: https://codechallenge.0x14.net/Challenges?id=12

### Explanation
On this challenge we need to find the shorter path in a weighted directed graph from a 'BTC' to another 'BTC' node, and take the multiplication of the line weights (if multiple paths with same minimal length exist, the one which maximizes the multiplication wins) but never less than 1. Another Breadth First Tree Traversal:

1) Parse the trades' dictionary as a map of 'name' to ('name',weight): {BTC:{(ABC,1),(DEF,2)}, ABC:{(BTC,5)}, ...}. Ignore trades with weight 0.
2) Initialize a list of testing trades as (BTC,1), and a list of tested trades as empty.
3) Replace each element in the testing list by a new testing trade based on the dictionary (weights are multiplied), for example after 1 step the initial list will be replaced by [(ABC,1 * 1),(DEF,1 * 2)]. Remove duplicated trades (no need to check twice) and those in the tested list (they were already tested and with shorter steps).
4) Add all current trades in the testing list to the tested one.
5) If there is a BTC trade in the testing list with greater than 1 weight, that's the solution. (If multiple, take the maximum).
6) If not, and there are still trades in the testing list, repeat step 3.
7) If the testing list is empty, return '1' as no other better trade was found.



## Challenge 13 - Find the New Earth
- Link: https://codechallenge.0x14.net/Challenges?id=13

### Explanation
No code here either, the input is an url and a hint `plyba:xvyy_nyy_uhznaf`. The url requires a username and password and if you check the headers of the petition (using the browser developer tools for example) you can see that the server return a header: `WWW-Authenticate
Basic realm="Galactica Position"`. This suggests we need to enter a basic authentication based on Galactica Position. A basic authentication requires a user/password, and the standard format is to get `user:password`, encode as base 64 and send as Basic header. The browser do this for us though. The hint consists on two alphanumeric strings separated by a ':' which probably means those are the user and password. The steps I followed were: 

- First enter the user/password directly, doesn't work. They are not the required user and password.
1) Try a lot of things, one of them caesar cipher. With n=13 the hint is converted into `cylon:kill_all_humans` (note: caesar 13 is the same as ROT13, which probably was the expected method from 'Galactica', but I'm not familiar with that)
2) Enter the decoded user/password, works. Download `here-is-the-position` file.
4) Open file, it's a hex dump of another file, convert hex to binary.
5) Check probable file type, gzip, uncompress, extract file.
- It seems to be a text about Frodo. Contains, again, extra ASCII characters.
- Get the program from challenge 5
- Check again those extra bytes in the file, only 3 different codes, two repeated a lot.
6) Try binary, convert one of them to 0, the other to 1, the less common to space, obtain a 0/1 string with spaces: `001 101000 01100 10001 10100001110000011000 100110101001100010011011000110 010001100110011010000110010001101100011011000110110   `
10) Convert the string using any online converter (ignoring spaces in the end) to text: `424815162342666`, which is the solution



## Challenge 14 - SEND + MORE = MONEY
- Link: https://codechallenge.0x14.net/Challenges?id=14

### Explanation
In this case we need to solve an equation where some numbers 0-9 are replaced with letters. The implemented solution is slow because it checks all possible combinations, but not slow enough (the submission input takes less than a minute) to consider optimizing it in a time-limited contest.

The challenge requires us to parse and solve an equation with operations '+', '-', '*' and '/'. There are no equation with an addition and a multiplication simultaneously (I checked) and also all the equations end in a '=' (I also checked). This means that we can use a stack-based method to check if a specific equation is valid, also using the equality operator as one. 

So for example: `AB + BA - CC = ABC` will be solved (with any specific mapping ABC<->0-9) to: x = AB --> x = x + BA --> x = x - CC --> x = (x == ABC ? 1 : 0). If the value of x is 1 at the end, the equation is valid, else is discarded. This is repeated for all possible mappings, and the valid ones are sorted and concatenated as required. This is the reason why the algorithm is slow.



## Challenge 15 - The revenge of the non-Tuentistic numbers
- Link: https://codechallenge.0x14.net/Challenges?id=15

### Explanation
The challenge is basically to calculate the factorial of a number minus some specific ones, modulo N = 100000007. there are several thing to notice:

* Due to the properties of factorials, it is better to calculate them directly (as multiplications). You also need to calculate all the previous factorials for any given number, so in this case the algorithm read all the cases and calculates the factorial for the bigger input, while also caching the required ones to write the output. In this case the program time is not linear with the number of cases.

* The factorial of a number is the multiplication of all the previous ones, and then we need to do it modulo N. This means that a number x bigger or equals to N will always return 0 because `x>=N => x! % N = x*(x-1)*...*(N+1)*MOD*(N-1)*...*1 % N => N*(...) % N` which is 0. This means that we don't need to calculate all of them, only until N-1 at most.

* The number N fits into an int, so if a number doesn't, it means it's bigger so the result is 0, and we can ignore it. For numbers in the factorial computations it is required to allow arbitrarily big numbers, so Java BigInts were used.

* The tuentistic numbers are those who contains the word 'twenty' in english, and are simply skipped when calculating the factorial. Those numbers are 20-29, 20 000,21 000,...29 000, 20 000 000,21 000 000,...,29 000 000. In other words any (20-29) * 1000^i for i>=0. The algorithm to check if a number is tuentistic is:
  * x in 20..29 -> yes
  * x >= 20000 -> isTuentistic(x/1000)
  * else no



## Challenge 16 - Where are the primes at?
- Link: https://codechallenge.0x14.net/Challenges?id=16

### Explanation
This problem was the hardest challenge BY FAR. I was at the edge of skipping it. In the end, and after a lot of tests and failures, and asking a mathematician friend to remember some prime properties (note that this friend didn't participate in the contest) I finally got the answer. The algorithm doesn't always solve the problem, some luck in the permutation is required.

In the end it was an algorithm somehow like:
1) Try to find a multiple of 2,3,5,7
2) Ask those with all the others
3) Those who always returned 1 are the solution except for 2, 3, 5 and 7 which we still need to find
4) For each number in the solution, ask them with the ones we already know to discard those who aren't 2,3,5,7. The code contains a more detailed description of the checks.
5) If everything goes right, the ones which only returned 1 or 1 and a prime are the solution. Not always the solution is returned, some bit of luck from the permutation is required.

Note: the submitted file was the state of the program as soon as I got the 'Congratulations' from the server, because I couldn't spend more time on it. The version here is the same program algorithmically, but cleaned and commentated.



## Challenge 17 - Super Digger Bros and the bulldozer of infinite buckets
- Link: https://codechallenge.0x14.net/Challenges?id=17

### Note
This solution is wrong. It may fail when the number of piles of sand is not 1. I wasn't able to fix it.

### Explanation
First lets analyze if there is only 1 pile of sand:
* If we write the number as binary, each turn you either:
  - remove a '1'
  - convert any 1 followed by 0s to a 0 followed by 1s, for example 11000 -> 10110
  - Lose if the number is 0 (there are no ones)

While playing with small numbers I couldn't notice any way to use this information, but I noticed a pattern: If you take the number and calculate its module 3, then:
  - If the result is 0 (multiple of three) the first player always lose, assuming both play perfectly.
  - If the result is 1, the first player always wins, even if they want to lose.
  - If the result is 2, the first player can decide the result (either win or lose).

This means that, with only 1 pile of sand, the solution is simply to take the number module 3 and check if the result is not 0.

With multiple piles of sand, however, each player has a turn to decide the outcome of each pile of sand, and even change some of them. I wasn't able to deduce the correct result, and I tried to find another module 3 relation which made the 'test input' pass, but unfortunately didn't solve the 'submit' phase.



## Challenge 18 - Bit Saver
- Link: https://codechallenge.0x14.net/Challenges?id=18

### Explanation
For this challenge we are required to compress a file using a specific algorithm that converts commands into unique binary sequences based on a dictionary. The algorithm is called [Huffman coding](https://en.wikipedia.org/wiki/Huffman_coding), although we are also required to minimize the distance between the shorter and longer prefix which means we need to use a 'minimum variance Huffman coding' algorithm.

As with other previous challenges, you need to balance between writing them yourself or finding and using an existing implementation. For this challenge I searched for existing variants, and only found a valid one in the [Matlab programming language](https://es.mathworks.com/help/comm/ref/huffmandict.html). The correct way to proceed would be to write the program in Matlab, however my experience with Matlab is limited and the time it was taking for me to find the syntax and functions was too much, and there wasn't much remaining time.

So I decided to do a multi-programming solution (which is preferable to avoid, except maybe in cases like this one where time is very limited). The solution is:
1) A kotlin function parses the input and writes a valid Matlab program to solve the huffman problem with the required input.
2) That Matlab program is then run, which solves the huffman problem for each case, and writes the results on a rudimentary csv file.
3) A second Kotlin function reads these csv (and the original input for extra data) and converts them to the required output of the original problem.

The Kotlin file is then a converter between the program input/output and Matlab's huffman function input/output.



## Challenge 19 - Code Red Chaos
- Link: https://codechallenge.0x14.net/Challenges?id=19

### Explanation
The most difficult part of this challenge is to understand what is required.

We need to find the bigger polynomial that allows us to not modify the crc when applying some noise.
This is the equivalent as saying we need to find the bigger polynomial that zeroes (when applying crc) all the noise, because CRC(message^noise^0..0)=CRC(message)^CRC(noise)^CRC(0..0), and since CRC(noise)=0 then CRC(message^noise)=CRC(message).

The CRC operation is just as a 'simple' division, where the result (the CRC output) is equivalent to the remainder of the operation. So we 'simply' need to find the gcd (great common divisor) of all the polynomial noises, which can be calculated sequentially gcd(1,2,3) = gcd(gcd(1,2),3) etc.

To find the gcd, we can use the Euclidean algorithm which just so happens to only require the modulo operation. Exactly what we know how to do! (CRC)

So, to recap:
1) Get all the noise
2) Apply gcd to each pair of noise until only one is left
   1) to apply gcd use the euclidean algorithm using crc itself as the modulo operation (divide the greater into the smaller until the result is 0)
3) The result is the bigger polynomial that solves the problem

Except there is one extra requirement: the polynomial must end in '1'.
So, if the resulting polynomial ends in 0, it means it isn't valid. But in that case we just need to remove the extra leading zeros, because a polynomial still works when adding or subtracting zeros (if the length fits). So

4) Remove all leading zeros of the output (in binary), and write it as hex.



## Challenge 20 - Hidden toy story
- Link: https://codechallenge.0x14.net/Challenges?id=20

### Explanation
Even though it took me the longest to solve, it was because I misread a hint and solved a challenge which wasn't needed. Here are the steps I took:

- The input is a picture without alpha, so no alpha hidden.
- The PNG has valid IEND at the end, so no hidden data
- I tested some color modifications, nothing obvious but the line seem to be a specific greyscale
- The line is a separate colorspace, and without aliasing (the rest of the image is). Definitely something hidden here.
1) I made a program to traverse the line and report the pixels in order.
- I then tried to convert the pixels to something in binary that when decoded produced something. Take the colors, reverse them, invert, reverse and invert...
2) After lots (LOTS) of tests, the git first commit was printed on the screen when you take the last bit of each pixel (rgb) and concatenate them in order. I was socked, but then I found the message that explains there is another tablet. There was also a link to a YouTube video, which I checked and found it was about learning how to decode an unknown algorithm.
3) I downloaded the new tablet, now in color, and with just a bit of tweaking of the traversal algorithm (follow pixels similar in color instead of grayscale) and then the same conversion, it printed the new decoded output.
4) Same git output, but now the payload is like a random bytecode. Seems binary, and the header was 1f 8b 08, the header of a gzip file, so I just wrote it as binary.
- After unzipping, the inside file explains there is just one more tablet.
6) Another tweak of the line traversal algorithm (allowing 'jumps' of two pixels) and again valid output.
7) It seems like a binary file again, with strange ascii output, but this time they are not outside the 256 byte range.

- I stumbled at this binary file for a while, and then I remembered the hint video in the first tablet output. It had the same similar ff tokens, and so I thought it was an invented compression technique which I needed to decode (as in the video).

And that's what I did. I took the binary file and with the only 'hints' that it was a linear-feed algorithm where ff probably meant '8 literals next' and if not there were 'holes' that required you to either look a dictionary, or use a sliding window or pointers, I started to decode it. The text started with a toys in the attic transcription, so I had almost the original and encoded text. By hand, I found that the codes in reverse meant that '1' is literal and '0' is a two bytes sliding window, where the first 12 bits (but with the last 4 at the beginning) was the position offset 18, and the last 4 the length offset 3. 

I was able to decode the whole string, but there were a single sliding window with a negative offset, which was impossible to deduce (the transcription didn't have that part). I needed to calculate the sha256 of the file, as it said, but there were 5 unknown characters so I couldn't.

I can't describe how I felt, I knew the end of the challenge was just 5 bytes away, but at the same time out of reach.

After a bit of rest I suddenly remembered the hint video from the first tablet. I saw it again, went to the wiki file they mention, and ... [the algorithm was there](https://moddingwiki.shikadi.net/wiki/Softdisk_Library_Format#LZW). The code format, the 18 and 3 offset...and what to do with a negative offset: write a space.

8) I implemented the missing negative=space into my algorithm...and the correct output was generated.

So, in the end I made the already hard latest challenge even harder, but that was all my fault, and even though it took a while, I'm surprised I was able to do it.
