# Challenge 15 - The revenge of the non-Tuentistic numbers
- Contest: https://codechallenge.0x14.net/Challenges?id=15
- Author: Abel Naya

## Explanation
The challenge is basically to calculate the factorial of a number minus some specific ones, modulo N = 100000007. there are several thing to notice:

* Due to the properties of factorials, it is better to calculate them directly (as multiplications). You also need to calculate all the previous factorials for any given number, so in this case the algorithm read all the cases and calculates the factorial for the bigger input, while also caching the required ones to write the output. In this case the program time is not linear with the number of cases.

* The factorial of a number is the multiplication of all the previous ones, and then we need to do it modulo N. This means that a number x bigger or equals to N will always return 0 because `x>=N => x! % N = x*(x-1)*...*(N+1)*MOD*(N-1)*...*1 % N => N*(...) % N` which is 0. This means that we don't need to calculate all of them, only until N-1 at most.

* The number N fits into an int, so if a number doesn't, it means it's bigger so the result is 0, and we can ignore it. For numbers in the factorial computations it is required to allow arbitrarily big numbers, so Java BigInts were used.

* The tuentistic numbers are those who contains the word 'twenty' in english, and are simply skipped when calculating the factorial. Those numbers are 20-29, 20 000,21 000,...29 000, 20 000 000,21 000 000,...,29 000 000. In other words any (20-29) * 1000^i for i>=0. The algorithm to check if a number is tuentistic is:
  * x in 20..29 -> yes
  * x >= 20000 -> isTuentistic(x/1000)
  * else no