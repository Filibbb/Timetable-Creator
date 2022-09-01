# Timetable Creator

## Beschreibung
Dieses Repository enthält den Code für das Projekt 2 "Stundenplan-Ersteller". 

### Hauptanwendungsfall 
Vor Beginn eines neuen Semesters oder Schuljahres müssen die Datenbestände zum Einlesen der Daten aktualisiert werden. Da sich an den Raumressourcen kaum etwas ändert, können die Datenbestände vom letzten Mal wiederverwendet werden. Nachdem alle Dateien aktualisiert wurden, werden sie in die Stundenplaner-Applikation geladen und auf Knopfdruck wird ein optimaler Stundenplan erstellt. Sollte dennoch eine Änderung nötig sein, kann diese händisch noch angepasst werden, solange es keine Kollisionen mit anderen Ressourcen gibt. Danach kann der Stundenplan als Gesamtes oder pro Raum, Lehrperson oder Klasse exportiert werden. 

### Weitere Anforderungen 
Das Einlesen der Daten mittels CSV kann unter Umständen manuell aufwendig sein. Es stellt sich die Frage, ob diese Daten direkt aus anderen Applikationen geladen werden können. Dazu müsste man recherchieren, in welchen Formaten diese Daten in verschiedenen Schulen vorhanden sind. 

#### MVP 

Die Stundenplan-Applikation erstellt einen Stundenplan aufgrund der eingelesenen Daten ohne Berücksichtigung von optimaler Ressourcenauslastung und kurzer Wege. Es können manuell Anpassungen vorgenommen werden, solange die Ressourcen diese zulassen. Der Stundenplan soll als Gesamtes exportiert werden können. 

##### Erweiterte Funktionen 

Einige erweiterten Funktionen, die anfangs geplant wurden, haben wir nicht umgesetzt. ([#8](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/8), [#9](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/9), [#10](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/10)) <br>
Jedoch haben wir andere Erweiterungen eingebaut die den Stundenplan verbessern und bei diesem Standpunkt des Algorithmus sinnvoller sind.

* [#26](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/26) Es wird auf die Pausen geachtet. Die Schule kann für jeden Tag ein Tagesschema definieren wie der Tag aussehen. 
Zum Beispiel kann nach jeder Lektion eine Pause von fünf Minuten eingebaut werden oder am Mittwochnachmittag kann schulfrei eingeplant werden. 
* [#43](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/43) Die Lektionen vom selben Fach werden nach einander Unterrichte.
  Zum Beispiel: 3 Lektionen Deutsch, werden nacheinander im Stundenplan eingeplant und nicht über die ganze Woche einzeln verteilt.
* [#35](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/issues/35) Es wird darauf geachtet, dass die gleichen Zimmer und Lehrpersonen pro Fach genommen werden. So müssen keine Schulzimmer oder Lehrpersonen, während den Lektionsblöcken, getauscht werden.

## Architekturdokumentation

## Bedienung
Um diesen Prototyp zu Benutzen müssen CSV Dateien erstellt werden, die dann über das GUI ausgewählt werden können.
Die Dateien müssen folgenden Anforderungen entsprechen:

Es sollen 3 CSV Dateien ausgewählt werden können, die folgende Werte haben:
- People               --> Id;SchoolVisitorRole;LastName;FirstName;WorkLoad;Weekdays;Subjects
- SchoolClasses    --> Id;Subjects;TEACHER_teacherIds;STUDENT_studentIds
- Building             --> BuildingIdentifier;Stockerk;RoomIdentifier;Capacity

Wobei zu beachten ist, dass bei allen Werten, die im Plural angegeben sind, mehrere durch ; getrennte Werte eingefügt werden können (z.B. Subjects --> ;THIN,4;ANA1,2)

Einschränkungen der Felder:
- Workload --> zwischen 1 und 100
- Weekdays --> MO, TU, WE, TH, FR, SA, SU sind gültige werte. Diese mit, getrennt angeben (z.B. ;MO,TH).
- Tacher und Student Felder in SchoolClasses --> Präfixe TEACHER_/STUDENT_ verwenden, Die nach dem Präfix angegebenen Id muss zu einem bereits erfassten Lehrer oder Schüler gehören.
- Subjects --> Nach dum SubjectName muss in form einer durch ein Komma abetrennten Zahl der Wochentlichen Stunden eingetragen werden (z.B. ;THIN,4;ANA1,2)
- Stockwerk --> Muss ein Integer sein.

Beispieldateien:
- People: [PeoplePropertiesExample.csv](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/blob/master/src/main/resources/ch/zhaw/pm2/napp/examples/PeoplePropertiesExample.csv)
- SchoolCLasses: [SchoolClassPropertiesExample.csv](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/blob/master/src/main/resources/ch/zhaw/pm2/napp/examples/SchoolClassPropertiesExample.csv)
- Buildings: [BuildingsExample.csv](https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/blob/master/src/main/resources/ch/zhaw/pm2/napp/examples/BuildingsExample.csv)

### Klassendiagramm
TBD

### Beschreibung
Als Erstes habe wir unser Projekt so aufgeteilt, dass wir den main code und die Tests getrennt haben. Sie können so klar unterschieden werden und ohne Umstände ihre eigenen Ressourcen verwenden. Ansonsten haben wir das Projekt in ihre Funktionen unterteilt. Ui, logger, fileio und school sind ein eigenes package. Diese Unterteilung dient zur Übersicht und ein Stück weit auch zur Zugriffseinschränkung.

Das School Package haben wir so aufgebaut wie auch die Schule an sich aufgebaut ist. Es besteht aus einem building, schoolclasses und timetable package. Diese enthalten die nötigen Klassen mit ihrer Logik.

Das ui- und das fileio-package haben wir nach Funktion aufgeteilt. Die Klassen die eine Funktion Bilden wie z.B. die ...CsvLoader Klassen bilden wiederum ein eigenes package.

Exceptions oder Utility Klassen sind auch in einem eigenen package, auch wenn es nur eine Klasse ist. So ist es ersichtlicher welche Exceptions und Utility-Klassen existieren.

Genauere Informationen zu der Umsetzung und den Entscheiden sind jeweils in den zugehörigen Issues zu finden.

# Team

## Gruppen-Mitglieder
* Adrian Büchi 
* Philippe Weber 
* Patric Fuchs
* Nico Wartmann 

## Teamrules
* Wir versuchen den Code in Englisch zu schreiben d.h. zum Beispiel ConsoleInputReader.java oder AddParagraph(String text, int paragraphNumber) anstelle von deutschen Namen.
* Wir arbeiten grundsätzlich mit Branches. Branches werden auf Englisch benammst und sollten beschreiben, was in diesem Branch gemacht wird.
* Wenn eine Änderung komplett ist, sollte diese im Idealfall in Review bei allen gestellt werden bevor auf den Master Branch gepushed wird. (4 Augen Prinzip)
* Git Commits bitte auf Englisch und nur Zustände comitten, die mindestens kompilieren und nach Möglichkeit nur auf die eigenen Feature Branches.
* Wenn wir feststellen, dass etwas nicht funktioniert bitte frühzeitig melden, wenn die oben genannten Teamrules nur hinderlich sind dies ansprechen, dann werden die Neu definiert.

## Git Workflow

Wir verwenden den Standard Git-Workflow, d.h. wir arbeiten grundsätzlich mit eigenen Branches. 

Das Naming Pattern, welches wir auf den Branches verwenden ist folgendes: 
* Für Feature Branches: prefix mit `feature`
* Für Bugfix Branches: prefix mit `bugfix`

Daraus resultiert z.B. folgender Name für einen Branch: `feature/issue-10-connect-multiple-clients` oder `bugfix/issue-23-fix-button-click-not-working`

Alles Weitere wie z.B. `release` oder vglw. wird für den Rahmen des Projektes nicht benötigt.

Der Hauptbranch ist bei uns der `master`. Auf diesen sollte nicht direkt gepushed werden (wird via rules auch verhindert). 

Grundsätzlich ist die Idee mit Pull-Requests zu arbeiten anstelle von direkten Pushes auf den `master`. 
Ein Pull-Request wird immer von einem Feature/Bug Branch aus gemacht. Pull-Requests sollten einen Zustand repräsentieren, der fertig ist, d.h. nicht noch TODOs oder unaufgeräumten Code enthalten. 
Es wird immer mind. ein Teammitglied den Code reviewen müssen um zu mergen. Idealerweise schauen aber alle Teammitglieder auf den Code und geben ihr Review ab. Adrian Büchi sollte grundsätzlich aufgrund seiner Erfahrung **immer** als Reviewer darauf sein. Nach einem Review sollten allfällige Änderungen entweder umgesetzt oder Rückfragen gestellt werden um Unklarheiten oder vglw. aufzulösen. Wenn das Feedback umgesetzt wurde, beginnt der Reviewzyklus von vorne.

Wenn ein Pull-Request gemerged wurde, sollte der dazugehörige Branch immer gelöscht werden. Dies dient dazu, dass keine "toten" branches herumliegen die potentiell verwirren könnten.

Commit Messages sollten nach Möglichkeit wiederspiegeln, was gemacht wurde. D.h. nicht "Did stuff" "Fixed" oder vglw. sondern repräsentativ für den Change stehen.

#### PR Beispiele mit "Diskussion"
* https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/pull/17
* https://github.zhaw.ch/PM2-IT21taWIN-bles-gan-kars/team01-napp-projekt2-timetable-creator/pull/23
usw. grundsätzlich wurde jeder PR mehr oder weniger diskutiert.


# Auftrag PM
## Anforderungen
* Die Anwendung muss mindestens den in der Projektskizze beschriebenen Hauptablauf
plus dazu notwendige Nebenabläufe umsetzen. Die Auswahl der umzusetzenden Abläufe
ist mit dem Betreuer abzusprechen.
* Es muss eine Benutzerinteraktion über eine JavaFX-Benutzerschnittstelle stattfinden. Das
Model-View-Control bzw. Model-View-Presenter Pattern soll konsequent umgesetzt
werden.
* Als Architekturdokumentation muss ein Klassendiagramm erzeugt werden. Achten Sie
auf einen sinnvollen Detaillierungsgrad und eine korrekte UML-Syntax gemäss Anleitung
Klassendiagramme (Link in der PM2 Kursinformation in Moodle). Erläutern Sie zudem
stichhaltig und nachvollziehbar, warum Sie diese Architektur gewählt haben. Der Umfang
der Erläuterung sollte zirka 0.5 - 1 A4-Seite umfassen. Die Dokumentation muss Teil des
Repository sein. Sie kann entweder direkt in der README-Datei enthalten sein oder muss
von dieser verlinkt werden.
* Für die Klassen, welche Domänenlogik (Verarbeitungslogik) enthalten, müssen sinnvolle
und gut dokumentierte Tests geschrieben werden. Die Testobjekte müssen isoliert getestet
werden (Mocktesting).
* Build-Tooling wird korrekt verwendet. Die Anwendung muss mit dem Befehl 'gradle
run' gestartet und die Tests mit 'gradle test' ausgeführt werden können.
* Die Projektstruktur muss sinnvoll sein und Dateien am richtigen Ort liegen.
* Das Branching-Model soll sinnvoll sein und konsequent angewendet werden. Die
Spezifikation des Branching-Models oder ein Link darauf muss in der README-Datei
enthalten sein.
* Das Projekt wird über Issues und GitHub-Project verwaltet (Mindestspalten: 'Todo' bzw.
'Backlog', 'Doing' und 'Done'; weitere wie z.B. 'Review' sind zulässig) und GitHub-Issues für
die Planung des Vorgehens und Dokumentation der Änderungen und Entscheidungen
verwendet. Verwenden Sie, wie in der Übung, strukturierte Beschreibungen und Labels zur
Kategorisierung.
* Pull-Requests werden konsequent für Reviews und das Merging der Beiträge verwendet.
Verlinken Sie in der README-Datei zwei Pull-Requests, in welchen Sie Reviews diskutiert
und Feedback integriert haben.
* Fassen Sie ihren zeitlichen Aufwand wöchentlich zusammen. Wird nicht bewertet, kann
aber für das Coaching nützlich sein.
* Ihr Projekt muss die im CleanCode-Handbuch definierten Regeln der Stufe L1, L2 und L3
erfüllen
