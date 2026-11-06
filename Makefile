.PHONY: release prerelease

# Optional für prerelease: INCREMENT=MAJOR|MINOR|PATCH erzwingt die Stufe,
# z. B. für die erste Beta einer neuen Hauptversion (make prerelease INCREMENT=MAJOR).
INCREMENT ?=
INCREMENT_FLAG = $(if $(INCREMENT),--increment $(INCREMENT))

# Nur von Menschen auszuführen: committet und pusht direkt auf main (siehe AGENTS.md).
release:
	@command -v cz >/dev/null || { echo "Commitizen fehlt: brew install commitizen"; exit 1; }
	@test "$$(git branch --show-current)" = main || { echo "Release nur von main aus möglich"; exit 1; }
	@test -z "$$(git status --porcelain)" || { echo "Arbeitsverzeichnis nicht sauber:"; git status --short; exit 1; }
	@git fetch origin
	@test "$$(git rev-parse HEAD)" = "$$(git rev-parse origin/main)" || { echo "main entspricht nicht origin/main"; exit 1; }
	./mvnw -B verify
	@echo
	@echo "=== Vorschau ==="
	@cz bump --dry-run --changelog
	@echo
	@next="$$(cz bump --get-next)" && \
		printf 'Release v%s erstellen und pushen? [y/N] ' "$$next" && \
		read answer && \
		case "$$answer" in \
			y|Y) ;; \
			*) echo "Abgebrochen, nichts wurde geändert."; exit 1 ;; \
		esac
	cz bump --yes
	git push --atomic origin main "v$$(cz version --project)"

# Nur von Menschen auszuführen: committet die Beta-Version auf den Feature-Branch, taggt und pusht beides.
prerelease:
	@command -v cz >/dev/null || { echo "Commitizen fehlt: brew install commitizen"; exit 1; }
	@case "$$(git branch --show-current)" in feature/*) ;; *) echo "Prerelease nur von einem feature/*-Branch aus möglich"; exit 1 ;; esac
	@test -z "$$(git status --porcelain)" || { echo "Arbeitsverzeichnis nicht sauber:"; git status --short; exit 1; }
	@git fetch origin
	@branch="$$(git branch --show-current)" && \
		{ git rev-parse --verify --quiet "origin/$$branch" >/dev/null || { echo "$$branch ist noch nicht gepusht"; exit 1; }; } && \
		{ test "$$(git rev-parse HEAD)" = "$$(git rev-parse "origin/$$branch")" || { echo "$$branch entspricht nicht origin/$$branch"; exit 1; }; }
	./mvnw -B verify
	@echo
	@echo "=== Vorschau ==="
	@cz bump --dry-run --changelog --prerelease beta $(INCREMENT_FLAG)
	@echo
	@next="$$(cz bump --get-next --prerelease beta $(INCREMENT_FLAG))" && \
		printf 'Prerelease v%s erstellen und pushen? [y/N] ' "$$next" && \
		read answer && \
		case "$$answer" in \
			y|Y) ;; \
			*) echo "Abgebrochen, nichts wurde geändert."; exit 1 ;; \
		esac
	cz bump --yes --prerelease beta $(INCREMENT_FLAG)
	git push --atomic origin "$$(git branch --show-current)" "v$$(cz version --project)"
