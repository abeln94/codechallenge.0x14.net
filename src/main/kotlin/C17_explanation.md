# Challenge 17 - Super Digger Bros and the bulldozer of infinite buckets
- Contest: https://codechallenge.0x14.net/Challenges?id=17
- Author: Abel Naya

## Note
This solution is wrong. It may fail when the number of piles of sand is not 1. I wasn't able to fix it.

## Explanation
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