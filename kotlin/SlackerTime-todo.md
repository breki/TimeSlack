- how to handle the state when the last activity is run?
    - change button icon
    - when the button is pressed, do what?
- add extra menu action for returning to previous activity 
- can we test the Android activity itself?
- how to log in Android?
- add minimum activity duration
- implement calculation of whether a certain plan can be done or not (based on the deadline and given current time)

## Mon 24.02.
- "next activity" display is now hidden if we're running the last activity
- Introduced SlackerPlanStatus.
- Removed the existing fab.

## Sun 23.02.
- Implemented a simple message-based model update function. 
- Added "next activity" button.
- Added a refresh timer to the plan activity.
- Implemented TimeMachine.
- Introduced Clock interface and SystemClock implementation.

## Sat 22.02.
- Made the plan activity view a bit more compact. 
- The app now shows the finish time of the current activity.
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