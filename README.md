A simple Android app to help memorizing letters.

The app:
- shows an image of an item corresponding to a letter
- says e.g. "b like banana"
- shows 4 buttons with letters. When one of the letters is selected, the app either paints it
green (for the good answer) or red (for the bad answer, painting the proper one blue).


**NOTE** Currently in Polish only. Pull requests to add support for other languages are welcome.

## Installation
Clone the repository:
```shell script
git clone https://github.com/michalszynkiewicz/letter-teacher
```

Build the project:
```shell script
./gradlew build
```

Copy the file located in `./app/build/outputs/apk/debug/app-debug.apk` to your phone and install the app from it.


## Screenshots

![good answer](/docs/good-example.jpg?raw=true)

![bad answer](/docs/bad-example.jpg?raw=true)
