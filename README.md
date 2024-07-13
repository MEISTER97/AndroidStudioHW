### Project Features:

- Supports 5 lanes and longer roads, resulting in a larger matrix for gameplay.
- Objects and points are dynamically moving within the game environment.
- Game can be reset after it ends, allowing for multiple play sessions.
- Options in the menu are not saved; returning to the main activity terminates the session (`finish()` method used).
- Utilizes `ScoreData` objects to store player score, meter score, and coordinates.
- Slow mode operates with the same mechanics as normal/default speed.
- Leaderboard functionality sorts scores based on the highest score (not meter score).
- Includes extensive use of `Log.d` for debugging and testing purposes.
- Leaderboard feature allows updating map location by tapping on the score in MeterScore.
- Issue exists with tilt in the y-axis affecting game speed adjustments during object updates.
