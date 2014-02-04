grubdice
========

LIARS dice score keeper

To add a DB script run `./gradlew createDbMigrationScript -Psql='some really descriptinve name of your script'`. Gradle will run and create a new file in `src/main/resources/db/migration`. It will tell you what the file name is.

Todo:
* Add an API key to authenticate w/ spring security
* Add more public API's (Get's only, no need to allow anyone to post data)
* Save who posted a game
* Allow players to add nicknames
* Allow the wildcard player 'Guest' that points to an empty character
* Hook into LDAP to get email / real name for score updates
* Add multi league support
   * Players will need to associate with a league?
   * How to do multi leage games?
* Profit.

Done:
* Update the data model to have ranking of a game vs score.  (see: persistance_updates)
* Update the data model to have a 'score' on the player that gets updated when a point is scored. (see: persistance_updates)
