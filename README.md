# Project 4 - *Parstagram*

**Parstagram** is a photo sharing app using Parse as its backend.

Time spent: **TBD** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User sees app icon in home screen.
- [x] User can sign up to create a new account using Parse authentication
- [x] User can log in and log out of his or her account
- [x] The current signed in user is persisted across app restarts
- [x] User can take a photo, add a caption, and post it to "Instagram"
- [x] User can view the last 20 posts submitted to "Instagram"
- [x] User can pull to refresh the last 20 posts submitted to "Instagram"
- [x] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [x] Style the login page to look like the real Instagram login page.
- [x] Style the feed to look like the real Instagram feed.
- [x] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [x] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [x] Show the username and creation time for each post
- [x] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [x] Allow the logged in user to add a profile photo
  - [x] Display the profile photo with each post
  - [x] Tapping on a post's username or profile photo goes to that user's profile page
  - [x] User Profile shows posts in a grid view
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [x] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [x] Custom SquareRelativeLayout for Grid View on profile fragment to micmic Instagram feel

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Introduce comments on the actual most instead of relying on the recycler view in the Post Details Activity to view them.
2. Figure out a way to optimize functions, reuse code, and introduce likes into comments.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='1.gif' title='Video Walkthrough' width='35%' alt='Video Walkthrough' />
<img src='2.gif' title='Video Walkthrough' width='35%' alt='Video Walkthrough' />

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

When designing the profile view, I wanted to model my approach off Instagram's square photos. I came to the conclusion that this is only possible through making your own layout. Another problem ensued when I wanted to implement click on image or username to take you to their profile. I had no way of making a new fragment in my PostsAdapter, but figured out that if I pass in my FragmentManager into my constructor for it, I could have a reference and thus create a new Profile Fragment. I struggled a lot with the ActionBar, eventually turning it into a Toolbar and fixing everything that was wrong. My last challenge was learning about ParseQueries, specifically when implementing comments and likes. For likes, query.find and query.whereEqualTo to be valuable in reaching my goal. For comments, I was unsure how to to approach it. Whether I would make it a pointer, relation, etc. I tried to make an array of JSON objects, but was unsure of how to parse it.

## License

    Copyright [2020] [Danny Mota]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
