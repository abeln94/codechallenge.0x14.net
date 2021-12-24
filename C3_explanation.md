# Challenge 3 - The night of the hunter
- Contest: https://codechallenge.0x14.net/Challenges
- Author: Abel Naya

## Explanation
1) Read the data, both strings and the letter values
2) Parse the letter values. The divisions make the text not json nor other common formats valid. One solution could be to replace any `\d+/\d+` (two numbers separated by a slash) with the result, and then parse. My solution is more generic:
   1) If the string starts with '{', mark as (`1`,`","`,`":"`)
   2) If the string starts with '[', mark as (`2`,`"),("`,`","`)
   3) else, mark as (`0`,`","`,`"="`)
   4) Now you have the text and three parameters. Remove the `first` n characters from both ends of the string, then split by `second` and each one split again by `third`. You can now associate each character by its value (if the value contains a slash, split again and divide). Example: `{'a': 2/3, 'e': 4, 'h': 3, 'l': 5, 'o': 1, 't': 6, 'v': 0}` is (`1`,`","`,`":"`) so --> `'a': 2/3, 'e': 4, 'h': 3, 'l': 5, 'o': 1, 't': 6, 'v': 0` --> `'a' 2/3` ` 'e': 4` ` 'h': 3` ` 'l': 5` ` 'o': 1` ` 't': 6` ` 'v': 0` --> `'a'` ` 2/3`, ` 'e'` ` 4`, ` 'h'` ` 3`, ` 'l'` ` 5`, ` 'o'` ` 1`, ` 't'` ` 6`, ` 'v'` ` 0`
3) For each word, map each character by its value and add them.
4) Return the word with the bigger score, or '-' if equal