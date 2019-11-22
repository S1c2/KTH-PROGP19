Protokollet

Vinst : genom att båda spelare tagit upp varsin belöning, då vinner de båda.

Mellan server och klient skickas 5 datapunkter : spelarnas x-koordinater, spelarnas y-koordinater, en touchedFlag variabel som skickar vilken "reward" som en spelare just nu plockat upp, samt de två spelarnas nuvarande poäng.

I spelet används JFrame som GUI. Spelarna (spelare 1 : blå, spelare 2 : röd) är rektanglar som ritas med hjälp av klassen PlayerSprite. Likaså ritas hindren samt belöningarna med hjälp av denna klass, men då i annan färg( hinder : svart, belöningar : grönt). Belöningarna ska kunna raderas från skärmen, därför måste vi hålla koll på vilken belöning som tagits upp eller inte tagits upp. I DrawingComponent klassen ritas alla spelets rektanglar upp - den körs kontinuerligt och uppdaterar konstant det som sker på skärmen. Här ritas belöningarna endast upp om de inte har rörts - för att kolla ifall de rörts eller ej använder vi oss av if-satser som kollar ifall de boolska variablerna touched1 och touched2 är sanna, är t.ex. touched1 sann så ritar man inte längre upp den gröna belönings-rektangel som motsvarar denna variabel på skärmen. Touched1 och touched2 ändras från falsk till sann när heltals variabeln touchedFlag ändras. TouchedFlag ändras från 0 till antingen 1 eller 2 när en spelare tar en belöning, den ändrar då värdet av touched1 eller touched2 i en separat metod. Eftersom alla dessa är globala variabler och DrawingComponent (som kontinuerligt körs) testar dessa variabler kan förändringen ses på skärmen. 

Servern:

Spelet kan max ha två spelare. För varje klient som ansluts inkrementeras variabeln numPlayers som håller koll på hur många spelare som är med i spelet. Variabeln maxPlayers är satt till 2 i början för att alltid kolla så att numPlayers inte blir större än maxPlayers. 

Klienten:

Metod : connectToServer() 
 
		Instantierar socketen, InputStream, OutputStream.
		Läser in playerID som skickats från servern och skriver ut vilken playerID man har som spelare till respektive spelare
		En ny instans av ReadFromServer() och WriteToServer() skapas där Input och OutputStreams skickas in som parametrar
		waitForStartMsg() kallas från ReadFromServer instansen
		Hela metoden enkapslas i ett try/catch block med en IOException

Klass : ReadFromServer
	
	Ny privat instans av InputStream
	
	Metoder : 

		ReadFromServer():
			Tar in InputStream som parameter

		run():
			Här läser vi motspelarens koordinater, touchedFlag, samt motspelarens poäng
			För att undvika att få en nullpointerexception (när motspelaren ännu ej skapats)
			enkapslar vi detta i en if-statement som kollar att motspelaren inte är likamed null

		waitForStartMsg():
			Skickar ut ett meddelande, inläst från servern, till båda spelarna
			Skapar två trådar (typen Thread) med ReadFromServer och WriteToServer instanserna som parametrar
			Startar de båda trådarna

Klass : WriteToServer

	Ny privat instans av OutputStream

	Metoder:

		WriteToServer()
			Tar in OutputStream som parameter.

		run():
			Den egna spelarens koordinater ska skickas till servern, även touchedFlag, samt spelarens egna poäng
			För att undvika en nullpointerexception enkapslas allting i en if-statement som kollar att spelaren inte är likamed null
			Thread.sleep() funktionen kallas för att lägga mindre belastning på processorn

Metod : main()
	
	Kallar connectToServer()





