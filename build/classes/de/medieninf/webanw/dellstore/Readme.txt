Hinweise zur Persistenzschicht
==============================

Mit den Dateien in einem Verzeichnis unterhalb von
  /opt/share/praktika/WebAnw/Persistenz*
erhalten Sie eine Beans-Schicht für eine Aufgabe aus dem letzten Übungsblatt 
und Ihre zweite Abgabe.

Stellen Sie sicher, dass Sie die Dateien in Ihr Projekt im Paket 
  de.medieninf.webanw.dellstore
einbinden und das gesamte Paket nicht mehr verändern - weder 
vorhandene Dateien ändern noch weitere Dateien hinzufügen. Erstellen
Sie eigene Klassen in einem separaten Paket. In dem Paket finden
sich "Datentransfer"-Beans für alle für die Anwendung relevanten 
Objekte. Es gibt eine Fassade (Schnittstelle/Implementierung)
	Dellstore/DellstoreBean
von dem Sie eine Instanz verwenden können um persistent DatenBeans
zu lesen, ändern, erstellen und löschen. Als Beispiel für die Verwendung
der Schnittstelle dienen u.a. die Unit-Tests in TestDellstoreBean.

Zu der Umgebung gehört auch die Datei \verb|persistence.xml|, die 
unter WEB-INF/classes (in Eclipse in Quellen src) im Verzeichnis
META-INF abgelegt werden muss. Konfigurieren Sie darin Ihre 
Datenbankzugriffsparameter.
Die JAR-Dateien haben Sie schon für das letzte Übungsblatt gebraucht. 
eclipselink*.jar und javax.persistence.*.jar braucht die Persistenzschicht. 
Kopieren Sie einfach alle Dateien unter Persistenz/WebContent/lib/ in das
WebContent/lib-Verzeichnis in Ihrem Projekt. 

Kopieren Sie sich die SQL-Dateien und initialisieren Sie Ihre Datenbank.
Reihenfolge: Projekt anlegen, Biblioteken (JARs) importieren, Ordner anlegen 
und Quellen sowie persistence.xml importieren; dann TestDellstoreBean 
als Unit-Test ausführen. 

Der nächste Schritt ist ein erster Zugriff im Rahmen der Web-Anwendung 
im Container auszuprobieren. Um auf die Daten-Beans zuzugreifen,
benötigen Sie eine Bean, die die Persistenzschicht managed. Dazu dient 
eine Instanz der Klasse DellstoreBean. Diese müssen Sie als Managed Bean 
im Scope application anlegen (entweder in der faces-config.xml oder 
als Annotation). Des weiteren sollten Sie sich eine erste selbstgeschriebene 
Backing Bean erstellen und als Managed Bean im Scope session leben lassen.
Auf Basis dieser Bean schreiben Sie dann eine Web-Seite auf der Sie etwas 
aus der DB anzeigen und dann ändern.

Stellen Sie nach der Installation erst sicher, dass die Unit-Tests 
durchlaufen und dann erst beginnen Sie die ersten Schritte im Container.
Vergessen Sie nicht die Bibliotheken zu importieren (inklusive JUnit4).
Ändern Sie die Persistenzschicht nicht. Wenn Sie etwas unbedingt brauchen,
dann melden Sie sich und es wird entweder gebaut oder Alternativen 
vorgeschlagen (es geht auch darum über Alternativen nachzudenken).

==========================================================================
