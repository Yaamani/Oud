# Oud
<div align="center">
<img src="oud.png">
</div>

## About
> Music player app (spotify clone).

## Getting started
1) Install Android Studio.
2) Import project & let gradle do its magic!!

## Running 
### Using the mock server
1) Install node.js.
2) Install json-server. `npm install -g json-server` or head to their github page for more detailes. https://github.com/typicode/json-server
3) Change the local ip address of your machine(labtop/pc) to `192.168.1.198`. Or you can alternatively open `Constants` class and change a constat called `YAMANI_MOCK_BASE_URL` to match your local ip. Make sure to also change the port number in this constant if you changed the default one when running the server.
4) Open `json-server` folder in the terminal & copy paste the command found in `terminal_command.txt` file (exists in `json-server` folder) in your terminal window to run the server.
- make sure that private networks is allowed. (This depends on your os)

### Using the real server (currently shutdown)
- Simply change a constant named `MOCK` (inside `Constants` class) to `false`.

## Showcasing
| Login/signup navigation                     | Login                     | Home                               |
|:-------------------------------------------:|:-------------------------:|:----------------------------------:|
| ![](showcasing/login-signup-navigation.gif) | ![](showcasing/login.gif) | ![](showcasing/home-animation.gif) |

| Playlist                     | Artist                     | Library                     |
|:----------------------------:|:--------------------------:|:---------------------------:|
| ![](showcasing/playlist.gif) | ![](showcasing/artist.gif) | ![](showcasing/library.gif) |


| Player                     | Settings                     | Premium                     |
|:--------------------------:|:----------------------------:|:---------------------------:|
| ![](showcasing/player.gif) | ![](showcasing/settings.gif) | ![](showcasing/premium.gif) |

## File Structure
```
.
├── app
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── oud
│       │   │               ├── api
│       │   │               ├── artist
│       │   │               │   └── fragments
│       │   │               │       ├── albums
│       │   │               │       ├── bio
│       │   │               │       ├── home
│       │   │               │       └── settings
│       │   │               ├── authentication
│       │   │               ├── connectionaware
│       │   │               ├── dummy
│       │   │               └── user
│       │   │                   ├── fragments
│       │   │                   │   ├── artist
│       │   │                   │   ├── home
│       │   │                   │   │   └── nestedrecyclerview
│       │   │                   │   │       ├── adapters
│       │   │                   │   │       └── decorations
│       │   │                   │   ├── library
│       │   │                   │   │   ├── artists
│       │   │                   │   │   ├── likedtracks
│       │   │                   │   │   ├── notifications
│       │   │                   │   │   ├── playlists
│       │   │                   │   │   └── savedalbums
│       │   │                   │   ├── playlist
│       │   │                   │   ├── premium
│       │   │                   │   │   ├── database
│       │   │                   │   │   ├── offlinetracks
│       │   │                   │   │   └── redeemsubscribe
│       │   │                   │   ├── profile
│       │   │                   │   ├── search
│       │   │                   │   └── settings
│       │   │                   ├── player
│       │   │                   │   └── smallplayer
│       │   │                   └── ui
│       │   │                       └── main
│       │   └── res
│       │       ├── anim
│       │       ├── layout
│       │       ├── menu
│       │       ├── navigation
│       │       └── values
│       └── test
├── json-server
│   ├── db.json
│   └── routes.json
└── tryingstuff
    └── src
        └── main
            └── java
                └── com
                    └── example
                        └── tryingstuff
                            └── OudApiJsonGenerator.java
```

## Unit tests
1) Write click on `oud.example.oud (test)` and choose `Run tests in Oud` or `Run test in Oud with coverage`.
<div align="center">
<img src="images/how_to_run_unit_tests.png" width="60%">
</div>

## Functional documentation
In Android Studio `Tools -> Generate JavaDoc...`

## See also
- [Backend](https://github.com/Hassan950/OudBackEnd)
- [Frontend](https://github.com/AbdallahHemdan/Oud)
- [End-to-end testing](https://github.com/Thebrownboy/Oud_TestingTeam)
