# UltraQuizzAPI
Kotlin web API using Ktor for your favorite Quizz app.

## API Endpoints

#### GET /start
Returns 5 random questions. The questions are chosen from a JSON file containing all possible questions for the quizz. This JSON file is available at `resources/questions.json`.

#### POST /score
Submit a new score. The body must be a JSON as:
```
{
  "gamerName": "NameA",
  "score": 4
}
```
If the score is valid, it is stored in the JSON file `resources/scores.json`. To be valid the score must be between `0` and `100`. It represents the pourcentage of good answers.

#### GET /scoreboard
Returns a JSON containing all submitted scores ordering descending by `date`.

#### GET /leaderboard
Returns a JSON containing 10 bests scores, ordering by `score` then `date`.

### Debug

#### POST /randomscore
Create and submit a random score. Usefull to fill up the `score.json` file.
Add the parameter `number` (int) to create multiples random scores at once.

#### GET /json/questions
Returns a JSON containing all the possible question.
