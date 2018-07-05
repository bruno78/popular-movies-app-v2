# Popular Movies App version 2.0


<p align="center"> <img src="https://cdn.rawgit.com/bruno78/popular-movies-app-v2/c8422e03/screenshots/Screen%20Shot%202018-07-04%20at%2010.22.58%20PM.png" width="200" alt="image of icon"> <img src="https://cdn.rawgit.com/bruno78/popular-movies-app-v2/8e31392d/screenshots/device-2018-07-04-205013.png" width="400" alt="image of the landscape mode"> </p>

<p align="center"> <img src="https://cdn.rawgit.com/bruno78/popular-movies-app-v2/8e31392d/screenshots/device-2018-07-04-204304.png" width="200" alt="movie other screen list"> <img src="https://cdn.rawgit.com/bruno78/popular-movies-app-v2/8e31392d/screenshots/device-2018-07-04-204138.png" width="200" alt="movie detail"> </p>


## Project Summary

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family.
Popular Movies helps users discover popular and highly rated movies on the web. It displays a scrolling grid of movie trailers, 
launches a details screen whenever a particular movie is selected, allows users to save favorites, play trailers, and read user reviews.

## Pre-requisites

* Android SDK v26
* Android min SDK v15

## Tools Used 

* [The Movie Database API](https://developers.themoviedb.org/3/getting-started)
* [Picasso](http://square.github.io/picasso/) 
* [Room](https://developer.android.com/topic/libraries/architecture/room)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) 
* [ButterKnife](http://jakewharton.github.io/butterknife/) 
* [SQLite](https://www.sqlite.org/index.html)
* [Icons8](https://icons8.com/material-icons/)

## Instructions

Download or clone this repo on your machine, open the project using Android Studio. Once Gradle builds
the project, click "run" and choose an emulator.

If you have an Android device, you can download the app from [Google Play](https://play.google.com/store/apps/details?id=com.brunogtavares.popmovies)

### User Experience

* Users can view and play trailers ( either in the youtube app or a web browser).
* Users can read movieReviews of a selected movie.
* Users can mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
* Users can modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

## License

The content of this repository is licensed under a **[Creative Commons Attribution License.](https://creativecommons.org/licenses/by/3.0/us/)**

## Notes about the project: 

This project is part of [Udacity's Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801) together with Grow With Google Scholarship.

This is Stage 2 of the Popular Movies App, for Stage 1, click [here](https://github.com/bruno78/popular-movies-app).

### Stage 1: Main Discovery Screen, A Details View, and Settings

See Stage 1 details [here](https://github.com/bruno78/popular-movies-app)

### Stage 2: Trailers, Reviews, and Favorites 

For complete rubric of Project Stage 2, click [here](https://github.com/bruno78/popular-movies-app-v2/blob/master/Rubric.md)



