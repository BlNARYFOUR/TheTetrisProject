# Retro Block 
![logo](https://i.imgur.com/ctAFtoz.png =250x)


## Inhoudsopgave
1. Ons team
2. Installatie
3. Speel instructies  
3.1 Inloggen  
3.2 Registreren  
3.3 Main menu                              
3.3.1 Daily rewards  
3.3.2 Avatar veranderen  
3.3.3 Game  
3.3.3.1 Single player  
3.3.3.2 Multi player  
3.3.3.3 Last man standing  
3.3.3.4 Time attack  
3.4 Aantal spelers  
3.5 Hero  
3.6 Game over


## 1. Ons team
Ons team bestaat uit:
Dennis Van Hoorick, Reinbert Van Acker, Brend Lambert en Bryan Helsens.

Als eerste hebben we **Dennis Van Hoorick**.  
Hij is altijd bereid om de helpende hand te zijn.
Hij houdt zich het meeste bezig met het financiele gedeelde en het opstellen van de dossiers van het Retro Block.

Ten tweede hebben we **Reinbert Van Acker**.  
Hij is gepasioneerd door lopen,
hij heeft zich met alles beziggehouden i.v.m. Retro Block het meeste van de tijd heeft hij zich beziggehouden met het design van de game.


Ten derde hebben we **Brend Lambert**.  
Hij is gepasioneerd door freerunning en is onze backend wizard.
Hij heeft zich het grootste deel beziggehouden met de backend van de game.

Om af te sluiten hebben we nog **Bryan Helsens**.  
Zijn passie is om de Nederlandse taal en Engelse taal samen te mengen in 1 stukje code.
Hij heeft zich het meetste beziggehouden met de wireframes en de backend van de game.


## 2. Installatie
Als eerste moet u de map van git klonen met het commando.  
 _**git clone https://op-gitlab.howest.be/TI/Project-II/groep-16.git**_
Hierdoor heeft u al onze bestanden.

Daarna moet u de map groep-16 in **Intelij** (aangeraden) of in een ander programma openen.
Open het project als volgt: open -> de locatie waar u het heeft opgeslagen -> groep-16 -> selecteer je application -> druk je op OK.  

Nadat u dat gedaan hebt, ga je naar -> View -> Tool window -> gradle. Dan komt er een nieuw kolom tevoorschijn, daar zie je linksboven een **refresh knop** daar klik u op. Dit zorgt ervoor dat al uw gradle projecten worden vernieuwd.

Als volgt gaat u naar groep-16 -> application -> src -> main -> java -> server -> Tetris en drukt u op "run" of rechtermuisknop en klik op "run Tetris.main()".

Open daarna uw brower en surft u naar de website <http://localhost:8016/static>.
Je kunt niet direct naar het spel gaan als u nog niet bent ingelogd. Als u eenmaal bent ingelogd, zal het spel je cookies opslaan zodat je de volgende keer direct naar het hoofdmenu kunt gaan.

_Op dit moment moet u 2 browsers openen en inloggen met 2 verschillende users om een game te spelen!_

Have fun!


## 3. Speel instructies
### 3.1 Inloggen
Als u naar de website <http://localhost:8016/static>surft komt u op een inlogscherm.

Hier kunt u uw username en wachtwoord ingeven. Bij het klikken op de knop "login", controleert het programma of uw username en wachtwoord overeenkomen. Als deze overeenkomen wordt u naar de main_menu pagina gebracht, komt uw username en wachtwoord niet overeen krijgt u een foutmelding en kunt u opnieuw proberen of een nieuw account aanmaken.

Als u nog **geen account** hebt, zal u eerst moeten **registreren** dit kunt u doen door op "Register" te klikken, dan wordt u naar een nieuwe webpagina gebracht waar u kunt registreren.

### 3.2 Registreren
Op deze pagina kunt u registreren, door middel van een username en een wachtwoord in te geven.

Eenmaal u alle velden hebt ingevuld kunt u op "Register" klikken dan worden uw gegevens opgeslagen. 
Als alle gegevens goed zijn ingevuld wordt u direct naar de main menu, anders zult u een foutmelding krijgen.

### 3.3 Main menu
Eenmaal u op het main menu bent aangekomen krijgt u een pop-up i.v.m. met uw daily rewards. **(voor meer info ga naar Daily rewards)**.

Wat kan de user op de main menu doen:
* **Avatar veranderen**
* **Game spelen**
* **Clan aanmaken/joinen**
* **Item uit onze shop kopen**
* **High scores zien van elke speler**
* **Uitloggen**
* **Cubes kopen (ingame currency)**

Op de main menu kan de gebruiker ook zien welk level hij is en hoeveel xp hij nog moet gaan verzamelen om een level omhoog te gaan. Bij bepaalde levels kan de user een reward krijgen. vb: een skin voor een hero.

Als een gebruiker in een clan zit, kan hij zijn clannaam + rank zien op deze pagina.

### 3.3.1 Daily rewards
Daily rewards zijn **cadeautjes** die de gebruikt krijgt van de game omdat hij zich elke dag heeft ingelogd zonder 1 dag te missen, hoe meer dagen u na elkaar inlogd hoe beter de cadeautjes zijn.

U kunt maar 1 cadeautje per dag krijgen.
De cadeautjes bestaan uit XP, cubes (onze ingame currency), scratch card of een mystery box.


### 3.3.2 Avatar veranderen
Bij het **klikken op het icoontje** boven de gebruiker zijn/haar username op de main manu pagina. Komt u op een nieuwe pagina waar hij al zijn avatar kan zien die de gebruiker heeft gekrijgen/verzameld. Op deze pagina kan de gebruiker zijn/haar avatar veranderen.

Door middel van op een avatar te klikken kan de gebruiker de geselecteerde avatar gebruiken.

Deze avatar geeft geen invloed op de game die de gebruiker zal gaan spelen dit is puur voor decoratie.

### 3.3.3 Game
Bij de klikken op "Choose gamemode" wordt de gebruiker naar een nieuwe pagina gebracht waar hij/zij kan kiezen **welke soort tetris gamemode hij/zij wil spelen** .

Onze gamemodes bestaan uit :
* **Single player**
* **Multi player**
* **Last man standing**
* **Time attack**

Wanneer de gebruiker een gamemode heeft geselecteerd heeft moet hij een **hero selecteren** die hij wil gebruiken in de game.   

_**Bij de gamemode "Single player" kan de gebruiker geen hero selecteren omdat heroes die het spel voor de tegenstander(s) moeilijker maken niet kan worden gebruikt.**_

Na het selecteren van een hero, gaat de **game zelf opzoek** naar **tegenstanders** om tegen te spelen.


### 3.3.3.1 Single player
Single player is het gewone **standaard tetris game** waarbij het de bedoeling is dat de gebruiken met de blokken die hij krijgt **zoveel mogelijk rijen moet maken** om zo'n hoog mogelijke score te behalen, zonder de bovenste rand te raken.

### 3.3.3.2 Multi player
Multi player is een gamemode waarbij je tegen andere online spelers kan spelen.

Hierbij is het de bedoeling om **zoveel mogelijk rijen te maken** zonder de bovenste rand aan te raken.

Bij het maken van een rij krijgt de gebruiker een **bepaald aantal punten**, als de gebruiker een bepaald aantal punten heeft behaald kan hij zijn **hero inzetten**.

_(meer info over hero kunt u lezen bij de titel Hero)_

### 3.3.3.3 Last man standing
Last man standing is een gamemode waarbij je tegen andere online speler speelt.

Hierbij is het de bedoeling om als **laatste over te blijven**. Door middel van **rijen te maken** kunt u **punten verdienen**, die punten kunnen gebruikers dan gebruiken om hun **heroes te activeren**.

_(meer info over hero kunt u lezen bij de titel Hero)_

### 3.3.3.4 Time attack
Time attack is een gamemode waarbij je tegen andere online spelers speelt.

Hierbij is het de bedoeling dat elke speler **zoveel mogelijk punten verzameld binnen een bepaalde tijd**.

Ook bij deze gamemode kan er gebruik worden gemaakt van **heroes**.

_(meer info over hero kunt u lezen bij de titel Hero)_

### 3.4 Aantal spelers
Single player zegt het zelf, bij deze gamemode moet je alleen spelen.

Bij de andere gamemodes kan de game bestaan uit **2-5 online spelers**.


### 3.5 Hero

De heroes die in de game zitten zijn **chatacter die vroeger heel bekend waren**. Denk maar aan een Pac-man, Donkey Kong, enz...


Er bestaan **2 soorten** heroes in onze game:   
De ene soort hebben een power die ervoor zorgt dat het **spel van de tegenstander moeilijker wordt gemaakt**. 
Bijvoorbeeld door de controls van de ander user voor een bepaald tijd te veranderen. Dat als de user op de rechter pijl klikt het blok naar de linkerkant draait i.p.v. naar de rechterkant.

De andere soort hebben een power die **jezelf helpt om het spel voor jezelf gemakkelijker te maken**.
Bijvoorbeeld is er een hero wanneer u die activeerd verwijdert hij de 2 onderste rijen.

### 3.6 Game over
Wanneer een user de **bovenste rand** van het tetris veld raakt is de **speler "dood"** en heeft hij **verloren**.

