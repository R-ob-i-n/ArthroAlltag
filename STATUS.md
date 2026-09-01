# Projekt-Status: Therapie-Management-System

Diese Datei ist der Einstiegspunkt für eine neue Claude-Session. Die vollständige
Anforderungsspezifikation steht in [Claude.md](Claude.md) — dort auch Rollen, Datenmodell,
REST-API-Übersicht und die Arbeitsweise-Regeln (Phasen-Ablauf, Erklär-Pflicht, KISS etc.).
Diese Datei hier beschreibt nur: **was schon steht und was als Nächstes dran ist.**

Stand: 2026-08-23.

---

## Tech-Stack (wie tatsächlich installiert, nicht nur geplant)

- Backend: Spring Boot 4.1.1, Java 21 (läuft lokal über Homebrew-OpenJDK 25, JDK 17 ist die
  einzige bei `/usr/libexec/java_home` registrierte Version — `JAVA_HOME` wird in `~/.zshrc`
  auf `/opt/homebrew/opt/openjdk` gesetzt)
- Datenbank: H2 in-memory (`jdbc:h2:mem:therapiedb`), Schema wird bei jedem Start aus den
  Entities neu erzeugt (`ddl-auto=create-drop`) + `data.sql` befüllt Testdaten
- Frontend: Angular 21, **zoneless** (kein `zone.js` im Projekt — Change Detection läuft über
  Signals), Tailwind CSS 4 (war im Scaffold schon vorkonfiguriert)
- Security: Spring Security + selbstgebauter JWT-Filter (kein Spring-Security-OAuth2/Resource-
  Server, das wäre für den Rahmen zu schwer) — Library `io.jsonwebtoken:jjwt` 0.12.6

---

## Backend — fertig (Phase 3 aus Claude.md, komplett bis auf Tests)

Alles unter `backend/src/main/java/Therapie_Management/backend/`:

- `entity/` — alle 8 Entities (Category, Module, User, Patient, Therapeut, Content,
  PatientModuleAccess, PatientFavorite). User/Patient/Therapeut nutzen
  `@Inheritance(strategy = JOINED)`.
- `dto/` — Records für alle Requests/Responses inkl. `ErrorResponse`.
- `repository/` — 8 Spring-Data-JPA-Repositories.
- `service/` — UserService, AuthService, ContentService, PatientService, FavoriteService,
  ModuleService, CategoryService.
- `controller/` — AuthController, PatientController, TherapeutController, ContentController,
  CategoryController, ModuleController.
  **Nachtraeglich ergaenzt** (fürs Freischaltungs-Frontend, beim Bauen der Module-UI als
  fehlende Luecke aufgefallen): `TherapeutController.getPatients()`
  (`GET /api/therapeuts/{id}/patients`, nutzt neue `PatientService.getAllPatients()`) und
  `TherapeutController.getPatientModules()` (`GET /api/therapeuts/{id}/patients/{patientId}/modules`,
  nutzt die bestehende `ModuleService.getUnlockedModules()` — ohne diese beiden Endpoints
  hätte ein Therapeut keine Möglichkeit gehabt, einen Patienten auszuwählen oder dessen
  aktuellen Freischaltstatus zu sehen, bevor er ein Modul freischaltet).
- `security/` — JwtTokenProvider, JwtAuthenticationFilter, SecurityConfig
  (`@PreAuthorize("hasRole('PATIENT') and #patientId == authentication.name")` auf
  PatientController/TherapeutController — Rolle **und** Ressourcen-Zugriff geprüft).
- `exception/` — 4 Custom Exceptions + `GlobalExceptionHandler` (`@RestControllerAdvice`).
- `data.sql` — 3 Patienten, 2 Therapeuten, 5 Kategorien, 4 Module, 13 Tipps, Favoriten,
  Modul-Freischaltungen. Passwörter bcrypt-gehasht.

**Test-Logins** (siehe Kommentar oben in `data.sql`):

| User-ID | Passwort | Rolle |
|---|---|---|
| v110001 (Elena) | elena123 | PATIENT |
| v110002 (Maria) | maria123 | PATIENT |
| v110003 (Hans) | hans123 | PATIENT |
| v220001 (Maren) | maren123 | THERAPEUT |
| v220002 (Dr. Mueller) | mueller123 | THERAPEUT |

**Starten:** `cd backend && ./mvnw spring-boot:run` (läuft auf Port 8080).
**Ende-zu-Ende per curl verifiziert:** Login, eigene Daten lesen, Fremdzugriff → 403,
Rollenverstoß → 403, doppelte Favoriten/Modul-Freischaltung → 400, unbekannte Kategorie → 404,
CORS von `localhost:4200`, H2-Console unter `/h2-console`, neue Therapeuten-Endpoints
(Patientenliste, Freischaltstatus pro Patient) inkl. Rollenprüfung (Patient → 403).

**Tests** (`src/test/java/.../{service,controller,repository}/`, siehe eigener Abschnitt
weiter unten): `mvn test` → 9/9 grün.

### Offen / bewusst zurückgestellt
- Kein produktionsreifes Secret-Management (`app.jwt.secret` steht im Klartext in
  `application.properties`, ist als Dev-only markiert).

---

## Frontend — begonnen (Phase 5 aus Claude.md)

Grundgerüst steht, Patienten-Seite und Therapeuten-Verwaltung haben jetzt fachlichen Inhalt.
Unter `angular-frontend/src/app/`:

- `core/` — `api-base-url.ts`, `services/auth.ts` (Login-State als Signal + localStorage),
  `services/category.ts` (`GET /api/categories`), `services/content.ts`
  (`GET /api/patients/{id}/contents` fürs Patienten-Frontend, `GET /api/contents` als
  rollenunabhängige Liste fürs Therapeuten-Frontend, plus `createContent`/`updateContent`/
  `deleteContent` über `/api/therapeuts/{id}/contents`), `services/favorite.ts`
  (`GET/POST /api/patients/{id}/favorites`, `DELETE .../favorites/{favoriteId}`),
  `services/modul.ts` (`GET /api/modules`, `GET /api/patients/{id}/modules`,
  `GET /api/therapeuts/{id}/patients/{patientId}/modules`,
  `POST /api/therapeuts/{id}/unlock-module`), `services/patient.ts`
  (`GET /api/therapeuts/{id}/patients` — Patientenliste fürs Freischalten),
  `interceptors/auth-interceptor.ts` (hängt JWT automatisch an), `guards/auth-guard.ts`,
  `guards/own-resource-guard.ts` (Rolle + eigene ID in der URL — reines UX, keine echte
  Sicherheit, die bleibt im Backend).
- `shared/models/` — TS-Interfaces 1:1 zu den Backend-DTOs.
- `shared/components/error-message/` und `shared/components/loading-spinner/` — einheitliche
  Darstellung für `ErrorResponse.message` bzw. Ladezustände, genutzt von Login, patient-home,
  therapeut-home und content-form statt vier fast identischer `<p>`/Text-Blöcke.
- `features/auth/login/` — fertiges Login-Formular.
- `features/patient/patient-home/` — **fertig für Suche + Filter + Favoriten + Module**
  (Kategorie-Buttons, Textsuche über Titel/Beschreibung, "Nur Favoriten"-Umschalter als
  eigener Bereich, Stern-Button je Tipp-Karte zum Merken/Entmerken, eigener Abschnitt
  "Meine Module" mit den freigeschalteten Modulen).
- `features/therapeut/therapeut-home/` — **fertig für Content-Verwaltung + Modul-
  Freischaltung**: Liste aller Tipps mit Kategorie-Filter, "Bearbeiten"/"Löschen" je Karte,
  "+ Neuer Tipp"-Button; eigener Abschnitt "Module freischalten" mit Patienten-Auswahl,
  Freischaltstatus je Modul und "Freischalten"-Button für noch gesperrte Module.
- `features/therapeut/content-form/` — eigene Formular-Komponente (Modal-Overlay), wird
  sowohl fürs Anlegen als auch fürs Bearbeiten genutzt (`content`-Input ist `null` beim
  Anlegen); Eltern-Komponente `therapeut-home` übernimmt den eigentlichen HTTP-Call und
  reicht Backend-Validierungsfehler über ein `errorMessage`-Input zurück ins Formular.
- `app.routes.ts` + `features/*/*.routes.ts` — Lazy-Loading pro Feature-Bereich.
- `app.ts`/`app.html` — Root-Shell mit Header (Name + Abmelden-Button).

**Starten:** `cd angular-frontend && npm start` (Port 4200). Backend muss separat laufen.
**Getestet:** `ng build` läuft sauber, Login-Call und alle Endpunkte (`/api/categories`,
`/api/patients/{id}/contents`, `/api/patients/{id}/favorites`, `/api/contents`,
`/api/therapeuts/{id}/contents` — Anlegen/Bearbeiten/Löschen, leerer Titel → 400 mit
Feldfehler, Patient darf nicht als Therapeut anlegen → 403, `/api/modules`,
`/api/therapeuts/{id}/patients`, `/api/therapeuts/{id}/patients/{patientId}/modules`,
Freischalten inkl. Duplikat → 400 und Patient darf Therapeuten-Endpoints nicht sehen → 403)
gegen echtes Backend per curl verifiziert (SPA-Routing, CORS, Token-Erhalt, alle
Antwortformen stimmen mit den TS-Modellen überein). **Nicht** im echten Browser
durchgeklickt — das sollte vor der Präsentation einmal manuell gemacht werden.
**Tests** (`src/app/{core/services,features/therapeut/content-form}/*.spec.ts`, siehe
eigener Abschnitt weiter unten): `ng test` → 6/6 grün.

### Phase 5 ist fachlich komplett

Alle 5 Punkte aus Claude.md (Suche + Filter, Favoriten, Therapeut-Bereich,
Module/Freischaltung, Fehler- und Ladezustände) sind erledigt. Fehler-/Ladezustände laufen
jetzt über `ErrorMessage`/`LoadingSpinner` (siehe oben) statt pro Komponente dupliziert zu
sein.

---

## Phase 6 — Zusammenschaltung (bewusster Durchlauf am 2026-08-23)

Ergebnis: Datenfluss, CORS und Fehlerbehandlung wurden gezielt end-to-end getestet (per curl,
mit echten Preflight-Requests), eine echte Lücke gefunden und behoben.

- **API-Aufrufe/Datenfluss**: Jede Komponente ruft nie `HttpClient` direkt auf, sondern einen
  Service (`AuthService`, `ContentService`, `FavoriteService`, `ModulService`,
  `PatientService`, `CategoryService`); Service gibt `Observable<T>` zurück, Komponente
  setzt in `.subscribe()` ein Signal. Kein globaler State-Store (NgRx o.ä.) — für dieses
  Projekt bewusst nicht nötig, Signals + Services reichen.
- **CORS**: `SecurityConfig.corsConfigurationSource()` erlaubt nur `http://localhost:4200`.
  Verifiziert per echtem Preflight (`OPTIONS` mit `Origin`-Header): korrekte
  `Access-Control-*`-Header für den erlaubten Origin, `403 "Invalid CORS request"` ohne
  `Allow-Origin`-Header für einen fremden Origin.
- **Fehlerbehandlung — Lücke gefunden und behoben**: Ein Request ohne/mit ungültigem Token
  bekommt vom `JwtAuthenticationFilter`/`SecurityConfig` ein `401` **ohne JSON-Body** (passiert
  im Filter, bevor der `GlobalExceptionHandler` greifen kann — anders als z.B. ein
  Rollenverstoß, der als `AccessDeniedException` sauber durchgereicht wird). Bisher landete
  der Nutzer dabei nur auf einer Fehlermeldung ("Tipps konnten nicht geladen werden"), ohne
  Weg zurück zum Login. **Fix**: `core/interceptors/auth-interceptor.ts` fängt jetzt jedes
  `401` global ab, meldet ab (`AuthService.logout()`) und leitet zu `/login` um — mit einer
  bewussten Ausnahme für den Login-Request selbst, weil ein `401` dort "falsches Passwort"
  bedeutet (eigener JSON-Body, wird schon vom Login-Formular angezeigt) und kein
  Session-Problem ist.
- **Bewusste Vereinfachung**: JWTs haben laut `JwtTokenProvider` keinen Ablauf-Claim (im Code
  kommentiert) — kein Session-Timeout während der Präsentation, aber auch kein Grund für ein
  automatisches Re-Login-Szenario im MVP.
- `ng build` weiterhin sauber, kein neues Backend nötig für diesen Schritt.

---

## Phase 7 — Qualität (Durchlauf am 2026-08-23)

Gesamtes Backend (alle Entities/Services/Repositories/Exceptions/DTOs/`data.sql`/`pom.xml`)
und die restlichen Frontend-Dateien (package.json, Tailwind-Config, `app.spec.ts`, `index.html`)
gelesen und gegen die 8 Kriterien aus Claude.md geprüft. Ergebnis: Architektur, Sicherheit
(bcrypt, parametrisierte JPA-Queries, CSRF-Begründung, CORS) und Erweiterbarkeit waren schon
sauber — vier konkrete Funde behoben:

- **Toter Code entfernt**: `UserService` (weder `findById()` noch `extractRoleFromId()` wurde
  irgendwo aufgerufen — `AuthService` liest die Rolle direkt aus der `User`-Entity) und
  `ContentRepository.findByErstelltDurchId()` (nie genutzt). Veraltete Kommentare in
  `User.java`/`UserRepository.java`, die noch auf das nicht mehr existierende `UserService`
  verwiesen, korrigiert.
- **Duplikation behoben**: Der Kategorie-Filter (Alle-Button + Kategorie-Buttons) war zu ~90%
  identisch in `patient-home.html` und `therapeut-home.html` dupliziert → neue Komponente
  `shared/components/category-filter/`. Nutzt `host: { style: 'display: contents' }`, damit
  die Buttons weiterhin direkt im `flex flex-wrap`-Container des Elternteils sitzen (in
  patient-home neben dem "Nur Favoriten"-Chip in derselben Zeile) statt einen eigenen
  Layout-Block zu bilden.
- **Kaputten Test entfernt**: `app.spec.ts` war der unveränderte Angular-CLI-Scaffold-Test und
  prüfte auf `<h1>Hello, angular-frontend</h1>`, das es in der App nicht mehr gibt — der Test
  wäre bei `ng test` fehlgeschlagen. Entfernt (keine neue Testlogik geschrieben, das bleibt wie
  besprochen zurückgestellt).
- **N+1-Trade-off dokumentiert statt "gefixt"**: `ContentService.toResponse()` lädt Kategorie
  und Ersteller pro Tipp lazy nach — bei den 12 Testtipps irrelevant, ein echter Fix
  (`@EntityGraph`/JOIN-FETCH) wäre für diese Datenmenge Overengineering. Jetzt als bewusste
  Entscheidung kommentiert, damit es in der Präsentation nicht wie ein übersehener Fehler wirkt.

**Verifiziert:** Backend neu gestartet, kompletter Smoke-Test über alle Kernendpunkte (Login,
Kategorien, Contents patient-/therapeutenseitig, Favoriten, Module, Therapeuten-Patientenliste,
voller Content-CRUD-Rundgang) — alles funktioniert unverändert. `ng build` weiterhin ohne
Warnungen.

---

## Tests (2026-08-23)

Grundlegende Tests laut Claude.md-Vorgabe: je ein Service-, Controller- und Repository-Test
im Backend, je ein Service- und Component-Test im Frontend — bewusst nicht erschöpfend,
sondern exemplarisch mit Erklärwert für die Präsentation.

**Backend** (`mvn test` → 9/9 grün), unter `backend/src/test/java/Therapie_Management/backend/`:

- `service/FavoriteServiceTest.java` — Unit-Test mit gemockten Repositories (Mockito, keine
  echte DB, daher schnell). Prüft die fachliche Logik: Favorit erfolgreich speichern,
  Duplikat-Regel wirft `ValidationException`, Entfernen eines fremden Favoriten wirft
  `ForbiddenException`.
- `controller/PatientControllerTest.java` — `@SpringBootTest` + `@AutoConfigureMockMvc` mit
  echtem `JwtAuthenticationFilter` und echtem `@PreAuthorize` (nicht gemockt!). Erzeugt echte
  Tokens über den echten `JwtTokenProvider` für die aus `data.sql` bekannten Test-User und
  prüft: kein Token → 401, falscher Patient → 403, Therapeut auf Patienten-Route → 403,
  eigener Patient → 200. Beantwortet direkt die in Claude.md vorgeschlagene Präsentationsfrage
  "Wie wird verhindert, dass ein Patient Therapeutenfunktionen aufruft?" mit einem lauffähigen
  Beweis statt nur mit Text.
- `repository/PatientFavoriteRepositoryTest.java` — `@DataJpaTest` mit `TestEntityManager`,
  legt eigene Testdaten an (unabhängig von `data.sql`) und prüft die von Spring Data aus dem
  Methodennamen abgeleitete Query `existsByPatientIdAndTippId`.

  Wichtig für spätere Sessions: Spring Boot 4 hat die Test-Annotationen neu aufgeteilt/verschoben
  (kein `spring-boot-starter-test` mehr, sondern einzelne Starter pro Bereich, siehe `pom.xml`).
  Die neuen Packages: `@DataJpaTest` → `org.springframework.boot.data.jpa.test.autoconfigure`,
  `TestEntityManager` → `org.springframework.boot.jpa.test.autoconfigure`,
  `@AutoConfigureMockMvc`/`@WebMvcTest` → `org.springframework.boot.webmvc.test.autoconfigure`.
  Die klassischen `org.springframework.boot.test.autoconfigure.orm.jpa.*`-Pfade aus älteren
  Boot-Versionen funktionieren hier **nicht** mehr.

**Frontend** (`ng test` → 6/6 grün), als `*.spec.ts` neben der jeweils getesteten Datei:

- `core/services/favorite.spec.ts` — Service-Test mit `provideHttpClientTesting()` (Angulars
  HTTP-Test-Provider). Prüft den HTTP-Vertrag: richtige Methode, URL und Request-Body für
  `getFavorites`/`addFavorite`/`removeFavorite`, ohne ein echtes Backend zu brauchen.
- `features/therapeut/content-form/content-form.spec.ts` — Component-Test über echte
  DOM-Interaktion (Werte in Inputs tippen, Formular absenden), nicht über Zugriff auf
  `protected`-Felder — genau das, was auch ein Nutzer tut. Prüft: Speichern-Button bleibt
  deaktiviert, bis alle Pflichtfelder ausgefüllt sind; `save` emittiert die eingegebenen
  Werte; bestehender Tipp wird beim Bearbeiten korrekt vorbefüllt (testet damit auch den
  `effect()`, der `content` beobachtet).

Bewusst nicht getestet (Umfang für "grundlegende Tests" reicht): `patient-home`/
`therapeut-home` selbst (viele injizierte Services, würde viel Mocking brauchen für wenig
zusätzlichen Erkenntnisgewinn), Guards, Interceptor — bei Bedarf nach demselben Muster
erweiterbar.

---

## Für eine neue Claude-Session: womit anfangen?

Am besten mit `git status` in beiden Repos (nur `angular-frontend/` ist aktuell ein
Git-Repo, `backend/` nicht) und dieser Datei plus `Claude.md` starten, dann fragen, ob es mit
der **Präsentationshilfe** weitergehen soll — Phasen 6 und 7 sowie grundlegende Tests sind
jetzt abgeschlossen.
