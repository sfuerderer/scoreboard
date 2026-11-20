# Scoreboard

[![Build](https://github.com/sfuerderer/scoreboard/actions/workflows/build.yml/badge.svg)](https://github.com/sfuerderer/scoreboard/actions/workflows/build.yml)
[![Latest release](https://img.shields.io/github/v/release/sfuerderer/scoreboard)](https://github.com/sfuerderer/scoreboard/releases/latest)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A JavaFX desktop app that drives the LED video wall at a football stadium. It was built for the
matches of SC Sand and is in use there on match days.

One operator works in a **control window** on a laptop. The app shows the content in one or more
**scoreboard windows** on the LED wall: the main scoreboard with score and match time, plus extra
displays such as date and time.

## Features

- **Score and match time**: running clock for up to four halves (including extra time), goals for
  home and guest
- **Teams and line-ups**: leagues, teams, players and coaches with photos; starting line-up and
  substitutes are introduced on the wall
- **Match events**: goals with goal animation, yellow, yellow-red and red cards, substitutions
- **Advertising**: rotating sponsor images and videos between the match phases
- **Media**: welcome screen, pictures, videos (e.g. anthem, trailer), black screen
- **Settings**: themes, goal animation, transition animations, scaling of the scoreboard to the
  resolution of the LED wall
- **Languages**: German and English user interface

Version 2 with a new wall design, cut-out player photos and sponsor placements is available as a
[pre-release](https://github.com/sfuerderer/scoreboard/releases) and is being tested on the wall.

## Requirements

- Java 17 or newer (e.g. [Eclipse Temurin](https://adoptium.net/))
- Windows x64, macOS on Apple silicon or Linux x64. Intel Macs are not supported.
- A second screen or video output for the LED wall

## Getting started

1. Download `Anzeigetafel-<version>.jar` from the [latest release](https://github.com/sfuerderer/scoreboard/releases/latest).
2. Put it into its own folder, e.g. `Scoreboard/`.
3. Start it from that folder:

   ```bash
   cd Scoreboard
   java -jar Anzeigetafel-<version>.jar
   ```

The app reads and writes its data **relative to the working directory**, so always start it from the
same folder. It creates these folders next to the JAR as needed:

| Folder           | Content                                                  |
|------------------|----------------------------------------------------------|
| `configuration/` | `settings.json` with your settings                       |
| `leagues/`       | leagues, teams, players and their pictures               |
| `advertisment/`  | sponsor images and videos                                |
| `media/`         | other videos, e.g. anthem or trailer                     |

Back up these folders. They hold all your club data.

## Building from source

The project uses Maven with the Maven wrapper, so you don't need to install Maven.

```bash
git clone https://github.com/sfuerderer/scoreboard.git
cd scoreboard
./mvnw -B verify                       # compile, run tests, build the fat JAR
java -jar target/Anzeigetafel-*.jar    # start the app from the repository root
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

The JAR bundles the JavaFX libraries for all supported platforms, so one JAR runs everywhere.

## Project structure

| Package (`src/main/java`) | Purpose                                                        |
|---------------------------|----------------------------------------------------------------|
| `system`                  | Start-up, loading and saving, defaults, resources              |
| `system.settings`         | Settings, persisted as `configuration/settings.json`           |
| `football`                | Domain model: league, team, match, line-up, player, coach      |
| `gui`                     | Windows, panes shown on the wall, dialogs, animations          |
| `background`              | Background services for match time, clock and advertising      |
| `language`                | Translations (`src/main/resources/config/language.json`)       |

[AGENTS.md](AGENTS.md) describes the conventions in more detail (in German). It is written for both
people and AI coding agents.

## Contributing

Issues and pull requests are welcome. The project follows GitHub Flow:

1. Create a branch from `main`, e.g. `feature/<topic>` or `fix/<topic>`.
2. Make sure `./mvnw -B verify` passes.
3. Open a pull request against `main`. The title must follow
   [Conventional Commits](https://www.conventionalcommits.org) (`feat: …`, `fix: …`), because it becomes
   the commit message and sets the next version.

New UI texts go into `language.json` in German and English.

## License

The source code is licensed under the [MIT License](LICENSE).

The bundled font Barlow Condensed is licensed under the
[SIL Open Font License](src/main/resources/fonts/BarlowCondensed-OFL.txt).
