# Confeedence

Confeedence is a simple app that allows you to easily build conference landing pages. It is built on top of the [WhenHub's API](http://whenhub.com) as an entry for the [WhenHub Hackathon.](https://whenhub.devpost.com/)

Confeedence was built by [Tibor Kranjčec](https://github.com/tiborkr) and [Mihael Konjević](https://github.com/retro) in ClojureScript, using the [Keechma Framework.](http://keechma.com)

[Try the app](http://confeedence.com)


## Implementation

Confeedence is using the following API endpoints:

- Get User Info - GET https://api.whenhub.com/api/users/me
- Get Schedule list - GET https://api.whenhub.com/api/users/me/schedules
- Get Schedule - POST https://api.whenhub.com/api/users/me/schedules/
- Create Schedule - POST https://api.whenhub.com/api/users/me/schedules
- Update Schedule - PUT https://api.whenhub.com/api/users/me/schedules/:id
- Create Event - POST https://api.whenhub.com/api/users/me/schedules/:schedule-id/events
- Update Event - PUT  https://api.whenhub.com/api/users/me/schedules/:schedule-id/events/:event-id
- Load Events - GET https://api.whenhub.com/api/users/me/schedules/:schedule-id/events

Confeedence allows users to customize the look of the landing page (change colors), and to define number of conference tracks. Since the schedule API doesn't allow custom fields, we're (ab)using the schedule tags as a primitive key/value store.

Confeedence implements three different event types:

- Event
- News
- Talk

It's using custom fields to store different data for each one of them. For instance talk has the following custom fields:

- Description
- Speaker Name
- Speaker Bio
- Speaker Photo URL
- Track

Since we wanted to ensure that the data created through Confeedence app looks good in WhenHub, we compile all of this data and generate the HTML that gets stored in the (built-in) description field.

## Running the app locally

- Clone the repository
- Install [Leiningen](https://leiningen.org/)
- Run `$ lein figwheel dev` from the project root
