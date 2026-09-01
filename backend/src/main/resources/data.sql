-- Testdaten fuer die Entwicklung. Wird bei jedem Start automatisch geladen (siehe
-- application.properties: ddl-auto=create-drop + defer-datasource-initialization).
--
-- Passwoerter sind bcrypt-Hashes (Klartext siehe Tabelle unten) - erzeugt mit derselben
-- BCryptPasswordEncoder-Klasse, die auch AuthService zur Laufzeit nutzt:
--   v110001 (Elena)      elena123
--   v110002 (Maria)      maria123
--   v110003 (Hans)       hans123
--   v220001 (Maren)      maren123
--   v220002 (Dr. Mueller) mueller123

-- Kategorien: Reihenfolge legt bewusst die IDs fest (1=Kueche ... 5=Hilfsmittel), da
-- categories.id noch von keiner anderen Stelle referenziert wird ausser ueber die
-- automatisch vergebene IDENTITY.
INSERT INTO categories (name) VALUES ('Kueche');
INSERT INTO categories (name) VALUES ('Garten');
INSERT INTO categories (name) VALUES ('Haushalt');
INSERT INTO categories (name) VALUES ('Uebungen');
INSERT INTO categories (name) VALUES ('Hilfsmittel');

-- Module haben laut Modul.java bewusst KEINE automatisch generierte ID - die vier Module
-- sind fachlich fest vorgegeben.
INSERT INTO modules (id, name, reihenfolge, beschreibung) VALUES
    (1, 'Grundlagen', 1, 'Einstieg: was ist Rhizarthrose und worauf kommt es im Alltag an?'),
    (2, 'Gelenkschutz im Alltag', 2, 'Gelenkschonende Techniken fuer Kueche, Garten und Haushalt.'),
    (3, 'Uebungen', 3, 'Einfache Mobilisations- und Kraeftigungsuebungen fuer die Hand.'),
    (4, 'Fortgeschrittene Uebungen', 4, 'Weiterfuehrende Uebungen fuer mehr Beweglichkeit und Kraft.');

-- Users (Basistabelle) - erst hier, dann erst patients/therapeuten (FK auf users.id).
INSERT INTO users (id, vorname, nachname, geburtsdatum, passwort_hash, role, erstellt_am, aktualisiert_am) VALUES
    ('v110001', 'Elena', 'Fischer', '1964-03-12', '$2a$10$02CsBgnPLvAy1wCDjd02hORFMjlkMDfy3/ZyhK0RZ9y7J.FZ8/sbK', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('v110002', 'Maria', 'Weber', '1958-07-04', '$2a$10$9heDYidpCk3aHowbwZo/4OpVC5MFCEtsPqBFtnE7zU83T/ByjKjbm', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('v110003', 'Hans', 'Schneider', '1949-11-30', '$2a$10$ZetTefAtmbvpxc30KDirhuSoOd5wGepH1rl/Z2O1arECuIDu0yV5u', 'PATIENT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('v220001', 'Maren', 'Klein', '1987-05-20', '$2a$10$DPvMmt.D1oAYcqYE4IBcduJ1z..OvXwg7aYjAs8fspp4uCCUSXjHm', 'THERAPEUT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('v220002', 'Stefan', 'Mueller', '1975-09-08', '$2a$10$NfpxKbDZ5JiKAwpBzoGjdO4gMg1pcFv1UOFYXvQTJc1al.Hgf2q8e', 'THERAPEUT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO patients (user_id, diagnostik, notizen) VALUES
    ('v110001', 'Rhizarthrose beidseitig', 'Kocht und gaertnert leidenschaftlich gerne, Schmerzen vor allem beim Greifen.'),
    ('v110002', 'Rhizarthrose rechts', 'Bevorzugt Uebungen im Sitzen.'),
    ('v110003', 'Fingerpolyarthrose', 'Traeger von Hilfsmitteln bereits gewohnt.');

INSERT INTO therapeuten (user_id, email, spezialisierung) VALUES
    ('v220001', 'maren.klein@therapie-management.example', 'Ergotherapie / Handrehabilitation'),
    ('v220002', 'stefan.mueller@therapie-management.example', 'Physiotherapie / Gelenkschutz');

-- Tipps (Content). Reihenfolge legt bewusst die IDs fest (1-12), da patient_favorites weiter
-- unten per fester tipp_id darauf verweist.
INSERT INTO contents (titel, beschreibung, kategorie_id, bild_url, schwierigkeitsgrad, hilfsmittel, erstellt_durch_id, erstellt_am, aktualisiert_am) VALUES
    ('Konservenglas oeffnen', 'Glasoeffner mit Gummigriff nutzen statt mit der Hand zu drehen - schont das Daumensattelgelenk.', 1, 'https://placehold.co/400x300?text=Konservenglas', 'LEICHT', 'Glasoeffner mit Gummigriff', 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Gemuese schneiden mit Wiegemesser', 'Ein Wiegemesser verteilt die Kraft auf die ganze Hand statt nur auf den Daumen.', 1, 'https://placehold.co/400x300?text=Wiegemesser', 'MITTEL', 'Wiegemesser, rutschfeste Schneidunterlage', 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Topfdeckel greifen ohne Drehbewegung', 'Deckel am Knauf mit der flachen Hand anheben statt mit den Fingerspitzen zu drehen.', 1, 'https://placehold.co/400x300?text=Topfdeckel', 'LEICHT', NULL, 'v220002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Giesskanne mit beidhaendigem Griff', 'Eine Giesskanne mit zwei Griffen verteilt das Gewicht auf beide Haende.', 2, 'https://placehold.co/400x300?text=Giesskanne', 'LEICHT', 'Giesskanne mit Doppelgriff', 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Unkraut jaeten im Sitzen', 'Eine Kniebank entlastet Ruecken und Haende beim laengeren Jaeten.', 2, 'https://placehold.co/400x300?text=Unkraut+jaeten', 'MITTEL', 'Kniebank, ergonomische Handschaufel', 'v220002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Waescheklammern mit Federgriff', 'Klammern mit grossem, weichem Griff statt kleiner Federklammern verwenden.', 3, 'https://placehold.co/400x300?text=Waescheklammern', 'LEICHT', 'Klammern mit grossem Griff', 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Tuerklinken statt Rundknaeufe nutzen', 'Klinken lassen sich mit der ganzen Hand oder dem Unterarm bedienen, Rundknaeufe erfordern Drehkraft im Daumen.', 3, 'https://placehold.co/400x300?text=Tuerklinke', 'LEICHT', NULL, 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Daumen-Kreisen zur Mobilisation', 'Den Daumen langsam 10x in jede Richtung kreisen lassen, taeglich morgens.', 4, 'https://placehold.co/400x300?text=Daumen-Kreisen', 'LEICHT', NULL, 'v220002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Faustschluss und Oeffnen', 'Langsam zur Faust schliessen, kurz halten, dann Finger komplett spreizen. 10 Wiederholungen.', 4, 'https://placehold.co/400x300?text=Faustschluss', 'LEICHT', NULL, 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Fingerstrecker mit Gummiband', 'Ein Gummiband um alle Finger legen und gegen den Widerstand oeffnen - staerkt die Streckmuskulatur.', 4, 'https://placehold.co/400x300?text=Fingerstrecker', 'MITTEL', 'Therapie-Gummiband', 'v220002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Ergonomischer Schreibstift', 'Ein dickerer Stiftaufsatz verringert den noetigen Greifdruck beim Schreiben.', 5, 'https://placehold.co/400x300?text=Schreibstift', 'LEICHT', 'Stiftverdickung', 'v220001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Anziehhilfe fuer Socken', 'Ein Sockenanzieher erspart das Buecken und den Kraftaufwand der Finger beim Ueberziehen.', 5, 'https://placehold.co/400x300?text=Anziehhilfe', 'MITTEL', 'Sockenanzieher', 'v220002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Freigeschaltete Module je Patient.
INSERT INTO patient_modul_zugang (patient_id, modul_id, freigeschaltet_am, freigeschaltet_von) VALUES
    ('v110001', 1, CURRENT_TIMESTAMP, 'v220001'),
    ('v110001', 2, CURRENT_TIMESTAMP, 'v220001'),
    ('v110002', 1, CURRENT_TIMESTAMP, 'v220001'),
    ('v110003', 1, CURRENT_TIMESTAMP, 'v220002'),
    ('v110003', 2, CURRENT_TIMESTAMP, 'v220002'),
    ('v110003', 3, CURRENT_TIMESTAMP, 'v220002');

-- Favoriten je Patient (tipp_id bezieht sich auf die Einfuege-Reihenfolge der Tipps oben,
-- 1=Konservenglas, 4=Giesskanne, 8=Daumen-Kreisen, 11=Schreibstift, 2=Wiegemesser).
INSERT INTO patient_favorites (patient_id, tipp_id, hinzugefuegt_am) VALUES
    ('v110001', 1, CURRENT_TIMESTAMP),
    ('v110001', 4, CURRENT_TIMESTAMP),
    ('v110001', 11, CURRENT_TIMESTAMP),
    ('v110002', 2, CURRENT_TIMESTAMP),
    ('v110003', 8, CURRENT_TIMESTAMP);
