# Challenge 2 - Catch them all
- Contest: https://codechallenge.0x14.net/Challenges?id=2
- Author: Abel Naya

## Explanation
Another warmup:
1) Read the data: save the text and all the pokemons on a mutable list.
2) Iterate for each Pokemon until the list is empty 
   1) If the Pokemon is in the text, remove (both from the text and from the list) and repeat step 2
   2) If the Pokemon is in the list as reversed, remove (both from the text reversed and from the list) and repeat step 2
3) Write the remaining text as output