.PHONY: release

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
