# Challenge 6 - What day is it?
- Contest: https://codechallenge.0x14.net/Challenges
- Author: Abel Naya

## Explanation
In order to convert each date into the weekday I used Java [LocalDate#getDayOfWeek()](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#getDayOfWeek--) which allows to parse any date in ISO format (the input) and return the day of week in any of the [multiple supported locales](https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html).

The only difficulty is that the input contains a reduced set of languages and using 2 letters in a not supported format (not ISO639 nor IETF BCP 47). For this reason a manual mapping of input->ISO639 codes was written, which both solves the issue of using only the specified languages and converting them to the correct format.

The other difference was that, for some reason, the `ț` character in the Romanian language is considered invalid, so after checking the web for alternatives I found `ţ` which seems to be the 'correct' one. Later, they gave a hint on Twitter about this.