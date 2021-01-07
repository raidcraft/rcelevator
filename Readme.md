# RCElevator
Dieses Plugin ermöglicht das erstellen von komplexen Personen-Aufzügen für mehrstöckige Gebäude.
Spieler können - wie im echten Leben - das Zielstockwerk des Aufzugs wählen und so auch in Wolkenkrazern schnell zur richtigen Ebene gelangen.

## Schild
Aufzüge werden über Schilder erstellt und gesteuert.

### Bedienung

Spieler können über das Klicken auf das Aufzug-Schild das Ziel-Stockwerk wählen. Ein Linksklick erhöht das ausgewählte Stockwerk (nach oben), ein Rechtsklick verringert das ausgewählte Stockwerk (nach unten).
Mit einem Redstone-Trigger (z.B. Button oder Schalter) kann der Aufzug in Gang gesetzt werden.

### Formatierung bei der Erstellung

*Zeile 1:* ``<Aktuelle Etage>``

In dieser Zeile wird angegeben in welcher Etage des Aufzugs dieses Schild steht.
Möglich sind Angaben von Untergeschossen (beginnend mit ``UG``), das Erdgeschoss (``EG``) oder Obergeschosse (beginnend mit ``OG``).
Diese Zeile wird nach der Erstellung mit dem Prefix ``Etage`` ergänzt. Dies darf jedoch nicht bei der Erstellung angegeben werden.

*Zeile 2:* ``[Aufzug]``

Diese Zeile markiert das Schild als Aufzug-Schild.

*Zeile 3:* ``<Unterstes Geschoss> - [Radius -] <Oberstes Geschoss>``

In dieser Zeile wird festgelegt zwischen welchen Stockwerken der Aufzug fährt. Beginnend mit dem niedrigsten und anschließend den obersten Stockwerk.
Optional kann zwischen den Angaben noch die Aufzug-Kabinen-Größe eingestellt werden. Diese wird als Volumen in Blöcken angegeben. D.h. eine Angabe von 2 bedeutet alle Spieler im Radius von 2x2x2 blöcken werden beim aktivieren des Aufzugs in das gewählte Stockerk teleportiert.

*Zeile 4:* ``<Ziel-Stockwerk>``

Hier wird das initiale Ziel-Stockwerk angegeben.
Möglich sind Angaben von Untergeschossen (beginnend mit ``UG``), das Erdgeschoss (``EG``) oder Obergeschosse (beginnend mit ``OG``).
Diese Zeile wird automatisch bei Auswahl des Ziel-Stockwerks geändert.

#### Beispiel 1 
Dieses Schild wird für das Erdgeschoss erstellt (1. Zeile).
Mit der zweiten Zeile wird dieses Schild als Aufzug markiert.
Dieser Aufzug reicht von Erdgeschoss bis zum 25. Obergeschoss (3. Zeile).

```
EG
[Aufzug]
EG - OG25
OG1
```

#### Beispiel 2 (Optionale Kabinen-Größe) 
Dieses Schild wird für Obergeschoss 1 erstellt (1. Zeile).
Mit der zweiten Zeile wird dieses Schild als Aufzug markiert.
Dieser Aufzug reicht von Untergeschoss 2 bis Obergeschoss 3 und verwendet eine Kabinengröße von 2x2x2 Blöcken (3. Zeile).

```
O1
[Aufzug]
UG2 - 2 - OG3
EG
```

## Permissions
Um Aufzüge erstellen zu können, wird die Permission ``elevatorpro.build`` benötigt.

## Konfiguration und Datenbank
Das Plugin benötigt keine Konfiguration und Datenbankanbindung
