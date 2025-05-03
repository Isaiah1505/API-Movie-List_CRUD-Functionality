# Movie API With CRUD Functionality:
This Android Studio Application is made using Java and makes a call to a Movie API, as well as having full CRUD functionality and authentication of it's users. The app requires the user to login into their account, or register if they don't have one; once they do, they can access the search page. The search page makes a get request to the Movie API and displays the results in a recyclerView, each result can be clicked on to take the user to a detailed page for that movie. 

From the detailed page, a user can add the movie to their favorites list, when a movie is added, the user can view them and look at a detailed view for that movie. This also lets them edit the description of the movie and delete it from their favourites list.

## Summary
Making a call to an Movie API, filling a recyclerView w/results, each cell can take you to an individual detailed page about a movie.
Used a ViewModel and LiveData with it, as well as ViewBinding, also used Glide to display images from URLs.


