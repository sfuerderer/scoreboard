# AGENTS.md – Anzeigetafel

Anleitung für KI-Agenten (Claude Code, Codex, Cursor, Copilot …) und Menschen, die an diesem Repo arbeiten.

## Projekt

JavaFX-Desktop-Anwendung, die eine Stadion-Anzeigetafel für Fußballspiele (SC Sand) steuert:
Spielstand, Spielzeit, Aufstellungen, Auswechslungen, Karten, Werbung, Videos (Tor-Animation, Hymne).
Ein **Control-Fenster** bedient der Operator, mehrere **Scoreboard-Fenster** (Hauptanzeige + Zusatzanzeigen
wie Datum/Uhrzeit) werden auf der LED-Wand dargestellt.

- Java 17 (`maven.compiler.release`), JavaFX 17 (`javafx.version` in `pom.xml`), Gson, commons-io
- Einstiegspunkt: `system.Main` → `system.Boot` → `gui.GUIControlling`
- Build: Maven mit Wrapper – **kein globales `mvn` voraussetzen**

## Befehle

```bash
./mvnw -B compile          # kompilieren
./mvnw -B verify           # kompilieren + (künftige) Tests + Fat-JAR bauen
java -jar target/Anzeigetafel-*.jar   # App starten (aus dem Repo-Root!)
make release               # Release anlegen (nur Menschen, siehe „Release“)
make prerelease            # Beta aus einem feature/*-Branch (nur Menschen, siehe „Prerelease“)
```

- Tests: JUnit 5 unter `src/test/java` (laufen mit `./mvnw -B verify`). Neue Logik außerhalb der GUI
  (z. B. `football.*`, `system.settings.*`) bitte mit Tests absichern, wenn sinnvoll.
- Die App ist eine GUI. Ein Agent kann das Ergebnis nicht sehen: nach UI-Änderungen mindestens `./mvnw -B verify`
  laufen lassen und den Menschen bitten, die App manuell zu prüfen.

## Laufzeit-Daten (nicht im Git)

Die App liest/schreibt **relativ zum Arbeitsverzeichnis** (deshalb `cwd` = Repo-Root):

| Ordner           | Inhalt                                                     |
|------------------|------------------------------------------------------------|
| `configuration/` | `settings.json` (wird beim Start erzeugt/migriert)         |
| `leagues/`       | `leagues.json` + je Liga/Team Ordner mit JSON und Bildern   |
| `advertisment/`  | Werbe-Bilder/-Videos (Schreibweise im Code so belassen!)   |
| `media/`         | Videos (Hymne, Trailer)                                    |

Diese Ordner sind lokal/vereinsspezifisch und stehen in `.gitignore`. Nicht committen, nicht löschen.

## Code-Struktur (`src/main/java`)

| Paket                         | Zweck                                                                 |
|-------------------------------|-----------------------------------------------------------------------|
| `system`                      | Start, Laden/Speichern (`Load`, `FileHandling`), `Defaults`, Ressourcen (`Images`, `Medias`, `Fonts`) |
| `system.settings`             | `Settings` (per Gson als `configuration/settings.json` persistiert), Enums wie `Themes`, `GoalAnimation` |
| `football`, `football.match`, `football.person` | Domänenmodell: Liga, Team, Spiel, Halbzeit, Aufstellung, Spieler, Trainer |
| `gui`                         | `GUIControlling` (Fenster-Setup), Frames, Panes, Dialoge, Menüs, Animationen |
| `gui.frames.control`          | Zustände des Control-Fensters (State-Pattern `ControlFrameState*`)   |
| `gui.frames.control.buttons`  | Je Bedien-Button eine Klasse, abgeleitet von `ControlButton`          |
| `gui.frames.panes`            | Inhalte, die auf der Anzeigetafel dargestellt werden (`Pane*`)        |
| `background`                  | JavaFX-`Service`/`Task` für Spielzeit & Uhrzeit, Werbe-Thread         |
| `language`                    | Mehrsprachigkeit                                                      |
| `message`                     | `ErrorMsg` / `InfoMsg` (Dialoge)                                      |

Ressourcen in `src/main/resources`: `config/language.json`, `style/*.css`, `images/`, `videos/`, `fonts/`.

## Konventionen

- **Texte in der UI immer über `Language.getText("key")`.** Neuen Key in
  `src/main/resources/config/language.json` unter `hmTexts` mit `GERMAN` **und** `ENGLISH` anlegen.
- **Fehler über `ErrorMsg.showErrorMessage(code)`.** Codes stehen in `ErrorMsg.init()`:
  `1xx` = Systemfehler, `2xx` = Eingabefehler. Neue Codes dort fortlaufend ergänzen.
- **Neue Einstellung** braucht alle Stellen:
  1. Feld mit `@Expose` in `system.settings.Settings` + beide Konstruktoren
  2. Default in `system.Defaults` und Verwendung in `Load.loadSettings()`
  3. Null-Fallback in `Settings.update()` (migriert alte `settings.json`)
  4. UI in `gui.frames.settingsFrame.SettingsFrame*` inkl. `saveToSettings()`
- **Version** steht in `Main.VERSION`, `<version>` in `pom.xml` und `.cz.toml`. Nicht von Hand ändern, das macht
  `make release`. Ein Versionswechsel löst in `Settings.update()` die Migration aus.
- Namensstil des Bestands beibehalten: Präfixe für Felder (`sName`, `iCount`, `bActive`, `lPlayers`),
  Singletons über statische Getter (`Load.getLoad()`, `FileHandling.getFileHandling()`).
- Einrückung: Dateien mischen Tabs und Spaces – **die Einrückung der jeweiligen Datei übernehmen**,
  keine Massen-Umformatierung (macht Diffs unlesbar).
- Copyright-Header (`Copyright (c) … Sebastian Fürderer`) in bestehenden Dateien nicht entfernen;
  neue Dateien bekommen ihn mit aktuellem Jahr.
- GUI-Änderungen nur auf dem JavaFX-Application-Thread (`Platform.runLater` aus Services/Threads).
- Große Medien (> 50 MB) nicht ohne Rücksprache committen – GitHub lehnt Dateien > 100 MB ab.

## Git-Workflow: GitHub Flow

- `main` ist der einzige langlebige Branch und ist **immer lauffähig** (`./mvnw -B verify` grün).
- Für jede Änderung einen kurzlebigen Branch von `main` abzweigen:
  `feature/<kurzbeschreibung>`, `fix/<kurzbeschreibung>`, `chore/<kurzbeschreibung>`
- Kleine, fokussierte Commits nach [Conventional Commits](https://www.conventionalcommits.org), z. B.
  `feat: add goal animation selection to settings`. `fix:` erhöht die Patch-, `feat:` die Minor-Version,
  `feat!:` die Major-Version; `chore:`, `docs:` usw. lösen kein Release aus.
- **Der PR-Titel folgt derselben Form**, denn er wird beim Squash-Merge zum Commit und steuert die nächste
  Version. CI prüft das (Workflow `PR title`), kann den Merge aber nicht erzwingen.
- Push → Pull Request gegen `main` → CI (GitHub Actions) muss grün sein → Review → **Squash-Merge** → Branch löschen.
- Releases werden als Git-Tag auf `main` markiert (`v1.3.0`), nicht als Branch (siehe „Release“).
- Agenten: **nie direkt auf `main` committen oder pushen**, kein Force-Push, keine Branches löschen
  ohne ausdrückliche Anweisung.

## Release

Voraussetzung einmalig: `brew install commitizen`. Dann auf einem sauberen, aktuellen `main`:

```bash
make release
```

Das Target prüft Branch und Arbeitsverzeichnis, läuft `./mvnw -B verify`, zeigt Version und Changelog-Eintrag als
Vorschau und fragt nach `y`. Erst dann erzeugt Commitizen Commit und Tag und beides wird gepusht. Der Tag startet
`.github/workflows/release.yml`, das das JAR baut und als GitHub-Release veröffentlicht.

- Einzige Ausnahme von „alles über PRs“: Der Bump-Commit geht direkt auf `main`. **Nur Menschen führen
  `make release` aus, Agenten nie.**
- Schlägt nur der Push fehl, liegen Commit und Tag lokal vor: `git push --atomic origin main v<version>` wiederholen.

### Prerelease (Beta aus einem Feature-Branch)

Größere Umbauten (z. B. Version 2) lassen sich vor dem Merge als Beta testen. Auf einem sauberen, gepushten
`feature/*`-Branch:

```bash
make prerelease                   # nächste Beta, z. B. 2.0.0-beta.1 → 2.0.0-beta.2
make prerelease INCREMENT=MAJOR   # erste Beta einer neuen Hauptversion, z. B. 1.4.0 → 2.0.0-beta.1
```

Ablauf wie bei `make release` (Prüfungen, `./mvnw -B verify`, Vorschau, Rückfrage `y`), aber der Bump-Commit und
der Tag `v<version>-beta.<n>` gehen auf den Feature-Branch. Der Tag startet `.github/workflows/prerelease.yml`,
das das JAR als GitHub-**Pre-release** veröffentlicht.

- **Nur Menschen führen `make prerelease` aus, Agenten nie.**
- Nach dem Squash-Merge des Feature-Branches ergibt `make release` auf `main` automatisch die finale Version
  (z. B. `2.0.0`); die Beta-Einträge im `CHANGELOG.md` werden dabei zu einem Eintrag zusammengefasst
  (`changelog_merge_prerelease`).
- Für große Umbauten einen Integrationsbranch nutzen (z. B. `feature/v2-design`), Teilschritte als eigene
  Branches mit PR gegen diesen Integrationsbranch, und `main` regelmäßig hineinmergen.
- Das JAR enthält die JavaFX-Bibliotheken für Windows x64, macOS arm64 und Linux x64 und braucht dort ein
  installiertes Java 17+. Intel-Macs werden nicht unterstützt, weil sich `mac`- und `mac-aarch64`-Dateien
  überschreiben würden.

## Bekannte Stolperfallen

- Auf Apple-Silicon-Macs nur ein arm64-JDK 17 verwenden; alte x86-JDK-8-Installationen brechen den Build.
- Bei JVM-Abstürzen entstehen `hs_err_pid*.log` im Repo-Root (ignoriert). Frühere Abstürze kamen von
  gemischten JavaFX-Versionen – JavaFX-Module immer über die eine Property `javafx.version` pflegen.
- `background/tasks/Test.java` ist ein Überbleibsel (absichtliche NullPointerException), kein echter Test.
