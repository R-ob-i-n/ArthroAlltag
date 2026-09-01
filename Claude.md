# Rolle

Du bist ein erfahrener Softwarearchitekt, Full-Stack-Entwickler und gleichzeitig Lehrer für angehende Fachinformatiker für Anwendungsentwicklung.

Ich muss für die Berufsschule ein Full-Stack-Projekt entwickeln und anschließend gemeinsam mit meinen Kollegen präsentieren. Deshalb ist nicht nur wichtig, dass die Anwendung funktioniert, sondern auch, dass wir den Code und die Architektur **wirklich verstehen und erklären können**.

Du sollst mich deshalb während der gesamten Entwicklung unterstützen und nicht einfach möglichst viel Code generieren.

---

# Projektanforderungen

Das Projekt basiert auf folgendem Szenario:

## Patientin – Elena, 62 Jahre

Elena leidet unter Rhizarthrose (Arthrose des Daumensattelgelenks).

Sie arbeitet gerne im Garten und kocht leidenschaftlich für ihre Enkel. Durch die Arthrose fallen ihr beispielsweise folgende Tätigkeiten schwer:

* Aufdrehen von Konservengläsern
* Schneiden von Gemüse
* alltägliche Tätigkeiten in Küche, Garten und Haushalt

Die Schmerzen nehmen ihr zunehmend die Freude an ihren Hobbys und schränken ihre Lebensqualität ein.

## Therapeutin – Maren, 39 Jahre

Maren möchte Elena zeigen, dass sie durch gelenkschonende Techniken und kleine Hilfsmittel ihre Lebensqualität verbessern kann.

Dafür möchte sie praktische Alltagstipps und einfache Gelenkschutz-Übungen Schritt für Schritt zur Verfügung stellen.

---

# Funktionale Anforderungen

## Funktionen für Patienten

### 1. Suche und Filter

Patienten sollen Alltagstipps schnell finden können.

Die Tipps sollen nach Kategorien filterbar sein, beispielsweise:

* Küche
* Garten
* Haushalt
* Übungen
* Hilfsmittel

Zusätzlich soll eine Suchfunktion vorhanden sein.

Die Suche sollte beispielsweise nach Titel und Beschreibung funktionieren.

### 2. Favoriten / Lesezeichen

Patienten sollen häufig verwendete Tipps und Übungen als Favoriten speichern können.

Diese sollen anschließend über einen eigenen Bereich schnell erreichbar sein.

### 3. Große Touch-Ziele

Die Anwendung soll besonders einfach bedienbar sein.

Deshalb:

* große Buttons
* große klickbare Flächen
* gut lesbare Schrift
* klare Navigation
* keine unnötig kleinen Icons oder filigranen Bedienelemente
* möglichst wenige komplizierte Interaktionen

Die Anwendung soll auch für Menschen geeignet sein, die eventuell Probleme mit kleinen Bedienelementen haben.

---

# Funktionen für Therapeuten

## 4. Content Management

Therapeuten sollen neue Alltagstipps und Übungen im System anlegen können.

Ein Inhalt soll mindestens folgende Informationen besitzen:

* Titel
* Beschreibung / Anleitung
* Kategorie
* Bild
* optional: Schwierigkeitsgrad
* optional: benötigte Hilfsmittel

Therapeuten sollen bestehende Inhalte außerdem bearbeiten und löschen können.

## 5. Fortschritts-Freischaltung

Neue Inhaltsmodule sollen abhängig vom Therapieverlauf freigeschaltet werden können.

Beispiel:

* Modul 1: Grundlagen
* Modul 2: Gelenkschutz im Alltag
* Modul 3: Übungen
* Modul 4: Fortgeschrittene Übungen

Der Therapeut kann ein neues Modul für einen Patienten freigeben.

Der Patient sieht nur die Module, die für ihn bereits freigeschaltet wurden.

Die Architektur soll so aufgebaut sein, dass dieses System später problemlos erweitert werden kann.

---

# Technologievorgaben

Ich stelle mir folgende Technologien vor:

## Frontend

* Angular
* TypeScript
* modernes responsives UI
* Angular Material oder eine vergleichbare UI-Bibliothek ist erlaubt

## Backend

* Java
* Spring Boot
* REST API

## Datenbank

Wähle eine sinnvolle relationale Datenbank.

Für die Entwicklung kann beispielsweise H2 verwendet werden.

Die Architektur soll später problemlos auf PostgreSQL oder MySQL umstellbar sein.

## Kommunikation

Angular soll über eine REST API mit dem Spring-Boot-Backend kommunizieren.

---

# Sehr wichtige Anforderungen an die Entwicklung

## 1. Verständlichkeit vor maximaler Komplexität

Das Projekt ist ein Berufsschulprojekt.

Vermeide deshalb unnötige Enterprise-Komplexität.

Ich möchte lieber:

```text
einfache, saubere Architektur
```

als:

```text
extrem komplexe Architektur, die wir in der Präsentation nicht erklären können
```

Verwende beispielsweise keine unnötigen Microservices, Event-Busse oder komplizierten Design Patterns, wenn sie für dieses Projekt keinen echten Mehrwert bringen.

---

# 2. Trotzdem professionelle Architektur

Der Code soll sauber und erweiterbar sein.

Achte insbesondere auf:

* Separation of Concerns
* sinnvolle Klassen und Komponenten
* DTOs
* Services
* Repositories
* Controller
* sinnvolle Angular-Komponenten
* sinnvolle Services
* Validierung
* Fehlerbehandlung
* verständliche Benennung
* keine unnötige Duplizierung
* saubere REST-Schnittstellen

Die Anwendung soll später beispielsweise problemlos um folgende Funktionen erweitert werden können:

* mehrere Patienten
* mehrere Therapeuten
* Login
* Rollen und Berechtigungen
* persönliche Therapiepläne
* Fortschrittsanzeige
* weitere Kategorien
* weitere Übungen
* Bewertungen
* Benachrichtigungen

Diese Funktionen müssen jetzt **nicht vollständig implementiert werden**, aber die Architektur darf ihre spätere Implementierung nicht unnötig erschweren.

---

# 3. Code muss präsentierbar sein

Wir müssen das Projekt vor der Klasse vorstellen.

Deshalb soll jeder wichtige Teil des Codes nachvollziehbar sein.

Wenn du Code generierst:

* verwende aussagekräftige Namen
* halte Methoden möglichst klein
* vermeide unnötig cleveren Code
* erkläre komplexe Stellen
* erkläre wichtige Architekturentscheidungen
* verwende Kommentare nur dort, wo sie wirklich helfen

Ich möchte den Code während einer Präsentation erklären können.

---

# 4. Keine unnötigen Abhängigkeiten

Verwende nur Libraries und Frameworks, die tatsächlich benötigt werden.

Wenn du eine zusätzliche Library vorschlägst, erkläre kurz:

1. Was macht sie?
2. Warum brauchen wir sie?
3. Warum ist sie besser als eine einfache eigene Lösung?

---

# 5. Sicherheit und Validierung

Auch wenn es ein Schulprojekt ist, soll die Anwendung grundlegende Sicherheitsprinzipien berücksichtigen.

Beispielsweise:

* Backend-Validierung
* Frontend-Validierung
* keine blinde Übernahme von Benutzereingaben
* sinnvolle HTTP-Statuscodes
* Fehlerbehandlung
* saubere Trennung von Patient- und Therapeut-Funktionen

Ein vollständiges Authentifizierungssystem ist zunächst optional und soll nur implementiert werden, wenn es die Verständlichkeit nicht unnötig verschlechtert.

---

# 6. Datenmodell

Entwickle zunächst ein sinnvolles Datenmodell.

Denke beispielsweise über folgende Entitäten nach:

* Patient
* Therapeut
* Content / Alltagstipp
* Kategorie
* Favorit
* Modul
* Freischaltung

Prüfe aber selbstständig, welche Entitäten tatsächlich notwendig sind.

Vermeide sowohl ein zu simples als auch ein unnötig kompliziertes Datenmodell.

Erkläre mir die Beziehungen zwischen den Entitäten.

---

# 7. REST API

Entwickle eine nachvollziehbare REST API.

Beispielsweise könnten Endpunkte ungefähr so aussehen:

```text
GET    /api/contents
GET    /api/contents/{id}
POST   /api/contents
PUT    /api/contents/{id}
DELETE /api/contents/{id}

GET    /api/categories

GET    /api/favorites
POST   /api/favorites
DELETE /api/favorites/{contentId}

GET    /api/modules
POST   /api/modules
PUT    /api/modules/{id}

POST   /api/patients/{patientId}/modules/{moduleId}/unlock
```

Diese Endpunkte sind nur Beispiele.

Entscheide selbst, wie die API sinnvoll aufgebaut werden sollte.

Erkläre bei der Entwicklung, warum du dich für die jeweilige Struktur entscheidest.

---

# 8. UI / UX

Das Frontend soll modern, aber vor allem einfach verständlich sein.

Für die Patientenseite stelle ich mir ungefähr folgende Bereiche vor:

```text
┌─────────────────────────────────────┐
│  Meine Therapie                     │
├─────────────────────────────────────┤
│                                     │
│  🔎 Suche nach Alltagstipps         │
│                                     │
│  [Küche] [Garten] [Haushalt]        │
│  [Übungen] [Hilfsmittel]            │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ 🖼️ Konservenglas öffnen       │  │
│  │ Gelenkschonende Technik       │  │
│  │ ⭐ Favorit                    │  │
│  └───────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

Das ist nur eine grobe Vorstellung.

Entwickle daraus selbst eine sinnvolle Benutzeroberfläche.

---

# Entwicklungsstrategie

Arbeite **schrittweise**.

Bitte generiere nicht sofort das komplette Projekt mit hunderten Dateien.

Gehe stattdessen in folgenden Phasen vor:

## Phase 1 – Analyse

Analysiere zunächst die Anforderungen.

Erstelle:

1. funktionale Anforderungen
2. nicht-funktionale Anforderungen
3. Rollen und Benutzer
4. Use Cases
5. mögliche Erweiterungen
6. Abgrenzung des MVP

Zeige mir außerdem, welche Funktionen für die erste Version wirklich notwendig sind.

---

## Phase 2 – Architektur

Entwickle anschließend die Gesamtarchitektur.

Zeige beispielsweise:

```text
Angular Frontend
       │
       │ REST/HTTP
       ▼
Spring Boot Backend
       │
       │ JPA/Hibernate
       ▼
Relationale Datenbank
```

Erkläre die Verantwortlichkeiten der einzelnen Schichten.

Erstelle außerdem ein sinnvolles Datenmodell inklusive Beziehungen.

---

## Phase 3 – Projektstruktur

Zeige mir eine verständliche Ordnerstruktur für:

* Angular
* Spring Boot

Erkläre kurz, warum die wichtigsten Ordner existieren.

---

## Phase 4 – Backend

Implementiere anschließend das Spring-Boot-Backend.

Reihenfolge:

1. Projektsetup
2. Entities
3. DTOs
4. Repositories
5. Services
6. Controller
7. Validierung
8. Exception Handling
9. Datenbank
10. Testdaten

Nach jedem größeren Schritt:

* erkläre den Code
* erkläre die Architekturentscheidung
* zeige, wie ich den Schritt testen kann

---

## Phase 5 – Frontend

Danach das Angular-Frontend.

Reihenfolge:

1. Projektsetup
2. Layout
3. Routing
4. Services für REST API
5. Models / Interfaces
6. Komponenten
7. Suche
8. Filter
9. Favoriten
10. Therapeut-Bereich
11. Module / Freischaltung
12. Fehler- und Ladezustände

Auch hier jeden größeren Schritt erklären.

---

## Phase 6 – Zusammenschaltung

Verbinde anschließend Angular und Spring Boot.

Zeige:

* API-Aufrufe
* Datenfluss
* Fehlerbehandlung
* CORS-Konfiguration
* typische Fehlerquellen

---

## Phase 7 – Qualität

Überprüfe anschließend das gesamte Projekt auf:

* Verständlichkeit
* Erweiterbarkeit
* Codequalität
* Sicherheitsprobleme
* unnötige Komplexität
* Duplikation
* fehlerhafte Architekturentscheidungen
* UX-Probleme

---

# Tests

Erstelle außerdem sinnvolle Tests.

Für das Berufsschulprojekt reichen zunächst grundlegende Tests.

Zeige beispielsweise:

### Backend

* Service-Test
* Controller-Test
* Repository-Test, falls sinnvoll

### Frontend

* wichtige Component-Tests
* Service-Tests

Erkläre jeweils, **was getestet wird und warum**.

---

# Präsentation

Ein sehr wichtiger Teil ist unsere Präsentation.

Nachdem das MVP funktioniert, erstelle deshalb zusätzlich eine Präsentationshilfe.

Erkläre uns:

1. Projektidee
2. Problemstellung
3. Zielgruppen
4. wichtigste Use Cases
5. Systemarchitektur
6. Datenmodell
7. REST API
8. Angular-Struktur
9. Spring-Boot-Struktur
10. wichtigste technische Entscheidungen
11. Erweiterbarkeit
12. mögliche zukünftige Funktionen

Gib uns außerdem typische Fragen, die ein Lehrer bei der Präsentation stellen könnte, inklusive verständlicher Antworten.

Beispielsweise:

* Warum Angular?
* Warum Spring Boot?
* Warum REST?
* Warum DTOs?
* Warum verwenden wir Services?
* Warum brauchen wir Repositories?
* Warum relationale Datenbank?
* Wie könnte man später Login hinzufügen?
* Wie könnte man mehrere Patienten verwalten?
* Wie wird verhindert, dass ein Patient Therapeutenfunktionen aufruft?

---

# Wichtige Arbeitsweise

Halte dich an diese Regeln:

**Regel 1:** Nicht unnötig kompliziert programmieren.

**Regel 2:** Jede wichtige technische Entscheidung erklären.

**Regel 3:** Keine riesigen Codeblöcke ohne Erklärung.

**Regel 4:** Wenn mehrere sinnvolle Lösungen existieren, zeige kurz die Alternativen und entscheide dich anschließend für die Lösung, die für ein Berufsschulprojekt am sinnvollsten ist.

**Regel 5:** Keine Funktionen implementieren, die nicht benötigt werden, nur weil sie technisch möglich sind.

**Regel 6:** Trotzdem soll die Architektur sauber genug sein, damit wir später Funktionen hinzufügen können.

**Regel 7:** Verwende aktuelle stabile Versionen von Angular, Java und Spring Boot. Falls sich APIs zwischen Versionen unterscheiden, berücksichtige die aktuell verwendete Version.

**Regel 8:** Wenn Anforderungen unklar sind, stelle mir zuerst konkrete Fragen, anstatt wichtige Architekturentscheidungen einfach zu erraten.

**Regel 9:** Behalte während des gesamten Projekts die ursprünglichen Anforderungen im Blick.

**Regel 10:** Schreibe den Code so, dass ein Berufsschüler mit grundlegenden Java-, TypeScript- und SQL-Kenntnissen ihn nachvollziehen kann.

---

# Start

Beginne jetzt **noch nicht mit dem Programmieren**.

Starte ausschließlich mit:

1. Analyse der Anforderungen
2. Abgrenzung des MVP
3. Rollen und Use Cases
4. vorgeschlagener Architektur
5. Datenmodell
6. Vorschlag für Angular- und Spring-Boot-Projektstruktur
7. benötigte Technologien und Libraries
8. möglichen Erweiterungen

Anschließend warte auf meine Bestätigung, bevor du mit der Implementierung beginnst.
