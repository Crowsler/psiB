- Autor: Bruno Nogareda Da Cruz <brunonogareda@gmail.com>

- Cuenta: psi29

- Compilar/Ejecutar:
	Para la compilación necesitaremos la versión 1.8 o superior de java y la librería jade.
	Comando para compliar: javac -cp ./jade.jar *.java
	Ejecución para el main agent y dos agentes inteligente: java -cp .:./jade.jar jade.Boot -agents "MainAgent:psi29_MainAg;Jugador1:psi29_Intelx;Jugador2:psi29_Intelx"

- Descripción del algoritmo del jugador inteligente:
	

- Comentarios generales:
	La clase MainAg se encarga de buscar los jugadores de tipo Player e intercambiar mensajes con estos, además de lanzar la GUI del juego.
	El agente Fixed, tiene el código de la lógica de mensajes, y el agente Random hereda de este, por lo tanto, para ejecutar el Random es necesario el Fixed.
	Al igual que el agente Random, el inteligente también hereda del agente Fixed.
	En la GUI, por defecto están activado los comentarios cortos (solo informe de partidas), podemos activar los comentarios largos (ralentiza el juego) en la pestaña "Xanela".
	En la GUI, al terminar un juego, permanecen las estádisticas de este y solo se borran al lanzar un nuevo juego.
