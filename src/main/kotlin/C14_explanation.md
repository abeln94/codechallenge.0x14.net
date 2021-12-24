# Challenge 14 - SEND + MORE = MONEY
- Contest: https://codechallenge.0x14.net/Challenges?id=14
- Author: Abel Naya

## Explanation
In this case we need to solve an equation where some numbers 0-9 are replaced with letters. The implemented solution is slow because it checks all possible combinations, but not slow enough (the submission input takes less than a minute) to consider optimizing it in a time-limited contest.

The challenge requires us to parse and solve an equation with operations '+', '-', '*' and '/'. There are no equation with an addition and a multiplication simultaneously (I checked) and also all the equations end in a '=' (I also checked). This means that we can use a stack-based method to check if a specific equation is valid, also using the equality operator as one. 

So for example: `AB + BA - CC = ABC` will be solved (with any specific mapping ABC<->0-9) to: x = AB --> x = x + BA --> x = x - CC --> x = (x == ABC ? 1 : 0). If the value of x is 1 at the end, the equation is valid, else is discarded. This is repeated for all possible mappings, and the valid ones are sorted and concatenated as required. This is the reason why the algorithm is slow.