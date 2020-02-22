- show the finish time of the current activity
- reduce the height of the activity display 
- implement re-rendering of the activity based on the model
    - how do we refresh, using a timer?
- add minimum activity duration
- implement calculation of whether a certain plan can be done or not (based on the deadline and given current time)

## Sat 22.02.
- Added `rebuild.bat` script.
- SlackerDuration is now used instead of ints.
- The app now shows the remaining duration for the current activity. 
- The app is now showing the slack time.
- Moved the view update logic to its own function.
- The app is now showing the current and next activity from the model, and also the plan finish time.

## Mon 17.02.
- Added the basic information to the plan run activity.

## Sat 15.02.
- Introduced SlackerDuration.
- Fixed the development environment on FUSION.

## Fri 14.02.
- Prepared the project to use JUnit 5 and made some initial unit tests.
- Started preparing the model for app.