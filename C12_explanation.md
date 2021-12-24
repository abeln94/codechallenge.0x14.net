# Challenge 12 - The crypto bubble
- Contest: https://codechallenge.0x14.net/Challenges?id=12
- Author: Abel Naya

## Explanation
On this challenge we need to find the shorter path in a weighted directed graph from a 'BTC' to another 'BTC' node, and take the multiplication of the line weights (if multiple paths with same minimal length exist, the one which maximizes the multiplication wins) but never less than 1. Another Breadth First Tree Traversal:

1) Parse the trades' dictionary as a map of 'name' to ('name',weight): {BTC:{(ABC,1),(DEF,2)}, ABC:{(BTC,5)}, ...}. Ignore trades with weight 0.
2) Initialize a list of testing trades as (BTC,1), and a list of tested trades as empty.
3) Replace each element in the testing list by a new testing trade based on the dictionary (weights are multiplied), for example after 1 step the initial list will be replaced by [(ABC,1*1),(DEF,1*2)]. Remove duplicated trades (no need to check twice) and those in the tested list (they were already tested and with shorter steps).
4) Add all current trades in the testing list to the tested one.
5) If there is a BTC trade in the testing list with greater than 1 weight, that's the solution. (If multiple, take the maximum).
6) If not, and there are still trades in the testing list, repeat step 3.
7) If the testing list is empty, return '1' as no other better trade was found.