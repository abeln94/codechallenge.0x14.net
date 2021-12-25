# Challenge 16 - Where are the primes at?
- Contest: https://codechallenge.0x14.net/Challenges?id=16
- Author: Abel Naya

## Explanation
This problem was the hardest challenge BY FAR. I was at the edge of skipping it. In the end, and after a lot of tests and failures, and asking a mathematician friend to remember some prime properties (note that this friend didn't participate in the contest) I finally got the answer. The algorithm doesn't always solve the problem, some luck in the permutation is required.

In the end it was an algorithm somehow like:
1) Try to find a multiple of 2,3,5,7
2) Ask those with all the others
3) Those who always returned 1 are the solution except for 2, 3, 5 and 7 which we still need to find
4) For each number in the solution, ask them with the ones we already know to discard those who aren't 2,3,5,7. The code contains a more detailed description of the checks.
5) If everything goes right, the ones which only returned 1 or 1 and a prime are the solution. Not always the solution is returned, some bit of luck from the permutation is required.

Note: the submitted file was the state of the program as soon as I got the 'Congratulations' from the server, because I couldn't spend more time on it. The version here is the same program algorithmically, but cleaned and commentated.