# Challenge 13 - Find the New Earth
- Contest: https://codechallenge.0x14.net/Challenges?id=13
- Author: Abel Naya

## Explanation
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