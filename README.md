# Referentieproject & enkele configuratie regels

(Last update: 27/11/2018)

Beste studenten, 

We zijn volop bezig met het finaliseren van de deployment omgeving.

Graag de volgende **verplichte configuratie regels** in acht nemen bij jullie project: 

(xx is overal groepsnummer met leading 0)

* HTTP Webserver Poort: 80xx 
* Socket URL (eventbus): /tetris-xx/socket/
* DB conn. string: jdbc:h2:~/tetris-xx
* Static files url: /static
* DB poort: houden op default
* DB web client poort: 90xx


## Structuur project:

Enkel **binnen** de directories **src/main/java** en **src/main/resources/webroot/** mag je je eigen files aanmaken en packages toevoegen. De rest van de directorystructuur (ook gradle etc.) blijft ongewijzigd! 

Op root niveau mag je wel directories toevoegen, maar zeker geen verwijderen

Documentatie: https://vertx.io/docs/vertx-core/java/#event_bus

DB is verplicht embedded 

## Referentieproject

Een referentieproject voor zij die nog problemen hebben is te zien op de Gitlab onder "Groep 00" (deze repository). Je bent **niet verplicht om van dit project te starten**, maar je mag wel. Uiteraard moet je de nodige aanpassingen uitvoeren met bovenstaande regels en is de basiscode indicatief. 

In het basisproject staan ook alle lokale gradle build CI settings correct, dus je kan je project (net zoals Chatty) **lokaal** testen voor je deployt. Zo weet je op voorhand of het de regels zal passeren of niet. 

Opgelet: indien je geen toegang hebt tot het referentieproject, zal je je moeten wenden tot een coach die je manueel toegang geeft op de repository. 

## .gitlab-ci.yml

Wanneer er een **.gitlab-ci.yml** file in je repository staat dan zal GitLab zelf een CI (Continuous integration) pipeline opzetten. In de gegeven .gitlab-ci.yml files staan dezelfde gradle tasks als degene die je lokaal kan runnen. 
Als al deze zogenaamde jobs slagen, krijgen we een groene pipeline met als laatste stap de eigenlijke deployment (hier zijn we nog volop mee bezig). We spreken in dat geval ook van CD (Continuous deployment). Meer info kunnen jullie vinden onder de CI / CD optie.

Veel succes!

_Het Projecten II team_