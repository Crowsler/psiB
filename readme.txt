
###################################################################################################################################
# NOTA: Se adjunta un documento README.md en formato markdown con el mismo contenido que el readme.txt, para facilitar la lectura #                                                                                                 #
###################################################################################################################################


- Autor: Bruno Nogareda Da Cruz <brunonogareda@gmail.com>

- Cuenta: psi29

______________________________________________________________________________

- Compilar/Ejecutar:

	Para la compilación necesitaremos la versión 1.8 o superior de java y la librería jade.
	Comando para compliar: javac -cp ./jade.jar *.java
	Ejecución para el main agent y dos agentes inteligente: java -cp .:./jade.jar jade.Boot -agents "MainAgent:psi29_MainAg;Jugador1:psi29_Intelx;Jugador2:psi29_Intelx"
	Para la ejecución del agente inteligente, son necesarias las clases psi29_Intelx.java, psi29_ColumRow.java, psi29_HistXogadas.java, psi29_Fixed.java

______________________________________________________________________________

- Descripción del algoritmo del jugador inteligente:

	Se utilizará el único agente inteligente entregado psi29_Intel1

	Este tiene programadas una serie de jugadas de diferente tipo que se aplicarán según el estado de la partida, jugadas anteriores, etc.

	  * Juego aleatorio: Tal y como su nombre indica, consiste en escoger un número (de 0 hasta el tamaño de la matriz) de forma aleatoria.

	  * Juego seguro: Este tipo de juego solo se habilita cuando no se va a cambiar mas la matriz antes del fin de la partida, vamos ganando la partida y existe una Fila/Columna en la matriz cuyo peor caso sea una diferencia que multiplicado por el número de rondas pendientes, no supera la diferencia de payoff total. A partir de aquí jugaremos siempre esta posición y así aseguramos que no perdamos la partida.

	  * Juego en ataque: En caso de que se detecte cierta repetición de posición en la jugada del oponente, jugaremos suponiendo que volverá a repetir y escogeremos la posición que mas nos convenga en ese caso.

	  * Juego a completar: Selecciona de manera aleatoria una Fila/Columna que esté pendiente de completar.

	  * Juego conservador: Selecciona la fila/columna que esté completa en la que nunca perdamos.

	  * Estrategia MaxMin: Estrategia MaxMin clásica. Escoge la posición en la que su cobro mínimo sea el mayor posible.

	  * Estrategia MinMax: Estrategia MinMax clásica. Escoge la posición donde el pago máximo al oponente sea lo menor posible.

	  * Estrategia Dominante 1: Escoge la posición en la que la diferencia total de payoff (suma de todas las diferencias de payoff entre el propio jugador y el oponente en una misma Fila/Columna) sea máximo. 

	  * Estrategia Dominante 2: Escoge la posición en la que tiene mas probabilidades de ganar (Fila/Columna en la que existan mas elementos en los que gane el jugador)

	  * Estrategia MaxMaxDiff: Escoge la posición en la que la diferencia máxima de payoff (en nuestro favor) sea lo mayor posible.

	  * Estrategia MinMaxDiff: Escoge la posición en la que la diferencia máxima de payoff (en favor del oponente) sea lo menor posible. Es similar a un MinMax clásico. Es lo mismo que se utiliza en el juego seguro.

	Cada vez que recibimos un resultado nuevo, se actualiza la matriz, así como los parámetros de cada fila/columna para el cálculo de las estrategias.
	Cada vez que se cambia la matriz, se reinicia por completo si este cambio supera un porcentaje establecido fijo (85%).

	Después de realizar un juego en ataque hemos perdido aumentaremos en una unidad la N de repeticiones y si ganamos la disminiumos hasta llegar a un mínimo de ventana de repetición.
	
	Si vamos perdiendo: 
	   * En caso de haber perdido las últimas N rondas activaremos un juego cíclico, donde realizaremos rotaciones entre las diferentes estrategias hasta que encontremos una en la que ganemos una ronda. Si aún así perdemos 2*N rondas, empezamos a jugar de forma aleatoria.
	   * En otro caso analizamos si el otro jugador está repitiendo una jugada mas de N veces, activaremos el juego en ataque, que escogerá la posición que mas nos favorezca para esa Fila/Columna en concreto, siempre que la posición que nos favorezca tenga una diferencia de payoff superior a cero, en otro caso jugamos a completar.
	   * Si no ocurre ninguna de estas situaciones (vamos perdiendo, pero no perdimos muchas rondas seguidas), jugaremos un juego Dominante 1.

	Si vamos ganando: 
	   * Si no van a suceder mas cambios en la matriz, y existe alguna fila/columna completa en la que la diferencia de payoff mínima multiplicada por el número de rondas pendientes no supera la diferencia de payoff de los jugadores, en este caso activamos el juego seguro para esta fila/columna. Esto nos garantiza que pase lo que pase ganaremos la partida (aunque puede acortarse notablemente la diferencia de payoff).
	   * Si existe una fila/columna en la que nunca perdamos y conozcamos más de la mitad de la matriz se jugará esta (Esto implica un juego dominante2).
	   * Si existe una fila/columna en la que nunca perdamos y conozcamos por completo activaremos el juego conservador.
	   * Comprobamos si el oponente repite alguna jugada y en caso afirmativo activamos el juego en Ataque.
	   * Si la diferencia de payoff (Con el oponente) es muy baja, activamos el juego MinMaxDiff (Jungando así una Fila/Columna donde las pérdidas en diferencia de payoff sean mínimas).
	   * Si aún quedan filas/columnas incompletas habilitamos juego a completar, salvo que perdamos en mas de 2 posiciónes y esté pendiente de descubrir mas de la mitad de la Fila/Columna que jugamos estrategia dominante 2.
	   * Finalmente en otro caso cualquiera activamos la estrategia dominante 1.

______________________________________________________________________________

- Funcionalidades adicionales:

	El Main Agent además de buscar el servicio publicado que proporcionan los jugadores (Player), busca si estos están registrados con otro tipo, que se usará como tipo de jugador, esto permitirá que se pueda distinguir facilmente el tipo de player que estea jugado. Del mismo modo, los players añaden su tipo como un servicio adicional.
	Además de mostrar los comentarios básicos de la partida en el cuadro de log, podemos activar un modo extendido de comentarios, donde veremos de manera detallada el resultado de cada una de las rondas.
	Podemos cambiar el nombre que se muestra de los jugadores, por defecto se aplica el que tiene el propio agente. (Si lo cambiamos solo cambia el que se muestra, no el del agente).
	Podemos eliminar jugadores que se registraron, pero que no nos interesa que jueguen.
	Desde la propia interfaz, también podremos cambiar el tamaño de la matriz, las rondas que pasan para que esta se modifique y el porcentaje de cambio de la misma.
	Si nos interesa tenemos la opción de limpeza de log, que elimina todos los comentarios de este cuadro y lo deja en blanco.
	
______________________________________________________________________________

- Comentarios generales:

	La clase MainAg se encarga de buscar los jugadores de tipo Player e intercambiar mensajes con estos, además de lanzar la GUI del juego.
	El agente Fixed, tiene el código de la lógica de mensajes, y el agente Random hereda de este, por lo tanto, para ejecutar el Random es necesario el Fixed.
	Al igual que el agente Random, el inteligente también hereda del agente Fixed.
	En la GUI, por defecto están activado los comentarios cortos (solo informe de partidas), podemos activar los comentarios largos (ralentiza el juego) en la pestaña "Xanela".
	En la GUI, al terminar un juego, permanecen las estádisticas de este y solo se borran al lanzar un nuevo juego.
