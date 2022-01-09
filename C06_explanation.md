# Challenge 6 - What day is it?
- Contest: https://codechallenge.0x14.net/Challenges?id=6
- Author: Abel Naya

## Explanation
The parsing of a date to calculate its day of week is a problem you should (probably) never attempt yourself due to all the [corner cases and different calendars](https://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week). In Java, we have [LocalDate#getDayOfWeek()](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#getDayOfWeek--) which allows to parse any date in ISO format (the input) and return the day of week in any of the [multiple supported locales](https://www.oracle.com/java/technologies/javase/jdk8-jre8-suported-locales.html).

The only difficulty is that the input contains a reduced set of languages and using 2 letters in a not supported format (not ISO639 nor IETF BCP 47). For this reason a manual mapping of input->ISO639 codes was written, which both solves the issue of using only the specified languages and converting them to the correct format.

The other difference was that, for some reason, the `ț` character in the Romanian language is considered invalid, so after checking the web for alternatives I found `ţ` which seems to be the 'correct' one. Later, they gave a hint on Twitter about this.