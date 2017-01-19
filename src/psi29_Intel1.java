import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 
 * Xogador, será cada axente que xogue o xogo.
 * Este será de varios tipos. (Fixo, Random, Intelixente)
 * Calculará o movemento adecuado e intercambiará os mensaxes co arbitro do xogo.
 * En este caso é o axente Intelixente, que utilizando algoritmos de GameTheory, MachineLearning
 * optará pola decisión mais adecuada.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */
@SuppressWarnings("serial")
public class psi29_Intel1 extends psi29_Fixed {

	//Tipos de xogo
	public static final int XogoAleatorio = 1;
	public static final int XogoSeguro = 2;
	public static final int XogoAtaque = 3;
	public static final int XogoCompletar = 4;
	public static final int XogoConservador = 5;
	public static final int XogoMaxMin = 10;
	public static final int XogoMinMax = 11;
	public static final int XogoDominante1 = 12;
	public static final int XogoDominante2 = 13;
	public static final int XogoMaxMaxDiff = 14;
	public static final int XogoMinMaxDiff = 15;
	
	private static final int ultimaXogadaCiclica = 15;
	
	private static final int porcentajeReinicioMatriz = 85;
	private static final int diffPayoffRiesgo = 10;

	
	//Parámetros para habilitar el modo Debug. (Imprime comentarios)
	public static boolean modedebug = true;
	public static final String debugPlayerName = "Bruno";
	
//	private int miPos;				//Indica la posición del jugador actual de la matriz (0 el de menor id - Filas, 1 el de mayor id - Columnas)
//	private int tuPos;				//Indica la posición del jugador contrario de la matriz (0 el de menor id - Filas, 1 el de mayor id - Columnas)
	private int tipoXogo;			//Determina o tipo de xogo que iremos realizar
	private int posXogadaAtaque;	//Esta será a posición que se decidirá xogar en caso de XogoAtaque.
	private int posXogadaSeguro;	//Esta será a posición que se decidirá xogar en caso de XogoSeguro.
	private int posXogoConservador;	//Esta será a posición que se decidirá xogar en caso de XogoConservador.
	private int contXogadaCiclica;	//Conta as veces seguidas que levamos realizando unha xogada Cíclica.
	
	ArrayList<psi29_ColumRow> listColumRow = new ArrayList<psi29_ColumRow>();
	ArrayList<psi29_HistXogadas> histXogadas = new ArrayList<psi29_HistXogadas>();
	
	Random rand;
	
	
	/**
	 * Constructor do axente Intelx, simplemente chama o constructor da clase Fixed e modifica o seu tipo por Intelixente.
	 */
	public psi29_Intel1() {
		super();		
		Tipo="Intelixente";
		rand = new Random();
	}
	
	
	/**
	 * Escolle unha xogada, da posición da matriz entre 0 e tamMatriz
	 * Dependendo do tipo de estratexia que estea seguindo o axente en cada momento
	 */
	public int xogada() {
			
		int ret=0;
		
//		modedebug = true;
//		System.out.println("MaxMaxDiff: "+getMaxMaxDiff());
//		System.out.println("MinMaxDiff: "+getMinMaxDiff());
//		printMatriz();
//		modedebug = false;
		
		
		
		//Dependendo do tipoXogo execturemos unha estratexia ou outra.
		switch(tipoXogo) {
		
			case XogoSeguro:
				ret = posXogadaSeguro;
				printMSX("Vou gañando e podo asegurar a victoria xogando: "+ret);
				break;
				
			case XogoAtaque:
				ret = getMellor(posXogadaAtaque);
				printMSX("O opoñente repite a posición: "+posXogadaAtaque+" ataco por aquí "+ret);
				break;
				
			case XogoConservador:
				ret = posXogoConservador;
				printMSX("Xogo de forma conservadora: "+ret);
				break;
				
			case XogoMaxMin:
				ret = getMaxMin();
				printMSX("Xogo MaxMin: "+ret);
				break;
				
			case XogoMinMax:
				ret = getMinMax();
				printMSX("Xogo MinMax: "+ret);
				break;
				
			case XogoDominante1:
				ret = getDominantePorDiffPayoff();
				printMSX("Xogo Dominante por diferencia de Payoff Total: "+ret);
				break;
				
			case XogoDominante2:
				ret = getDominantePorGanhadas();
				printMSX("Xogo Dominante por posicións totais gañadas: "+ret);
				break;
				
			case XogoMaxMaxDiff:
				ret = getMaxMaxDiff();
				printMSX("Xogo MaxMax por Diferencia: "+ret);
				break;
				
			case XogoMinMaxDiff:
				ret = getMinMaxDiff();
				printMSX("Xogo MinMax por Diferencia: "+ret);
				break;
			
			case XogoCompletar:
				ret = getCompletar();
				printMSX("Xogo a completar posicións da matriz: "+ret);
				break;

			case XogoAleatorio:
				ret = rand.nextInt(tamMatriz);
				printMSX("Xogo aleaotorio: "+ret);
				break;
				
			default:
				ret = rand.nextInt(tamMatriz);
				printMSX("Esto non deberia pasar. O tipo "+tipoXogo+" de xogo non existe");
				break;
				
		}
		
		if(ret<0 || ret>tamMatriz-1) {
			printMSX("Error: Se ha calculado un número que no está dentro de la matriz.");
			ret = rand.nextInt(tamMatriz);
		}
		
		return ret;
	
	}//Fin xogada
	
	
	/**
	 * Función que actualizará os parámetros xogados na última ronda
	 * @param xogada1 Fila seleccionada polo xogador de menor id
	 * @param xogada2 Columna seleccionada polo xogador de maior id
	 * @param payoff1 Payoff do xogador de menor id
	 * @param payoff2 Payoff do xogador de maior id
	 */
	public void resultado(int xogada1, int xogada2, int payoff1, int payoff2) {
		
		int xogadaProp, xogadaCont, payoffProp, payoffCont;
		
		//Miramos se na xogada anterior o resultado é diferente do que temos na matriz
		if(Matriz[0][xogada1][xogada2]!=payoff1 || Matriz[1][xogada1][xogada2]!=payoff2) {
			Matriz[0][xogada1][xogada2] = payoff1;
			Matriz[1][xogada1][xogada2] = payoff2;
			Matriz[2][xogada1][xogada2] = 1;
			if(xogada1!=xogada2) {
				Matriz[1][xogada2][xogada1] = payoff1;
				Matriz[0][xogada2][xogada1] = payoff2;
				Matriz[2][xogada2][xogada1] = 1;
			}
			
			//Actualizamos os datos de Filas/Columnas
			psi29_ColumRow.actualizaColuRowParametros(listColumRow, tamMatriz, Matriz);
		}
		
		//Calculamos a xogada propia e a contraria, o payoff propio e o contrario
		if(Id<Id_o) {
			xogadaProp = xogada1;
			xogadaCont = xogada2;
			payoffProp = payoff1;
			payoffCont = payoff2;
		}
		else {
			xogadaProp = xogada2;
			xogadaCont = xogada1;
			payoffProp = payoff2;
			payoffCont = payoff1;
		}
		
		//Engadimos os resultados no historico de xogadas
		
		PagoParcial += payoffProp;
		PagoParcial_o += payoffCont;
		
		if(payoffProp-payoffCont>0) {
			RondasGanhadas++;
			RondasPerdidas_o++;
		}
		else if(payoffProp-payoffCont<0) {
			RondasPerdidas++;
			RondasGanhadas_o++;
		}
		
		histXogadas.add(new psi29_HistXogadas(xogadaProp, xogadaCont, payoffProp, payoffCont));
		
		//Utilizamos a función control de estado para decidir que tipo de estratexia levaremos a cabo basándonos na situación actual
		//e nos datos de xogos anteriores.
		controlEstado();
		
		printMatriz();
		
	}//Fin resultado
	
	
	/**
	 * Realiza un estudio de como vai a partida, e basandose nas rondas que quedan, os resultados,
	 * as xogadas anteriores, etc. Realizase un axuste na estratexia.
	 */
	public void controlEstado() {
		
		int numRondasPendentes = numRondas-histXogadas.size();
		//int numRondasPendetesCamb = histXogadas.size()
		int diffPayoff = PagoParcial-PagoParcial_o;
		
		
		if(numRondasPendentes==0)
			return;
		
		//Se estamos xogando un xogo seguro non precisamos analizar nada.
		if(tipoXogo==XogoSeguro && PagoParcial > PagoParcial_o)
			return;
		
		//Se xoguei en ataque, pero perdin amplio a ventana de repetición para que sexa mais complicado. Se foi ben, baixamos o tamaño da ventana de repetición
		if(tipoXogo==XogoAtaque) {
			if(histXogadas.get(histXogadas.size()-1).perdido) {
				psi29_HistXogadas.VentanaRepeticion++;
			}
			else if(psi29_HistXogadas.VentanaRepeticion>psi29_HistXogadas.VentanaRepeticionMIN) {
				psi29_HistXogadas.VentanaRepeticion--;
			}
		}
		
		if(PagoParcial < PagoParcial_o) { //Vou perdendo
			
			//Comprobamos se o opoñente repite unha xogada.	
			int numMaisRepetidoContrincante = psi29_HistXogadas.numMaisRepetido(histXogadas, tamMatriz, 85);

			//Se perdín as últimas PartidasPerdidasParaXogoCiclico  partidas activaremos o xogo cíclico.
			if(psi29_HistXogadas.vecesPerdidasVentana(histXogadas)==psi29_HistXogadas.PartidasPerdidasParaXogoCiclico) {
				
				//Se xa levo tempo xogando de maneira cíclica, activamos un xogo aleatorio
				if(contXogadaCiclica>=psi29_HistXogadas.PartidasPerdidasParaXogoCiclico*2) {
					tipoXogo=XogoAleatorio;
				}
				else {//En outro caso (Sempre que estemos nunha xogada de tipo cíclica), imos cambiando de xogada entre as cíclicas hasta que una dea resultado.
					if(tipoXogo>=10) {
						if(tipoXogo<ultimaXogadaCiclica)
							tipoXogo++;
						if(tipoXogo==ultimaXogadaCiclica)
							tipoXogo=10;
					}
					else if(tipoXogo==XogoCompletar) {
						tipoXogo=XogoDominante1;
					}
				}
				contXogadaCiclica++;
			}
			else if(numMaisRepetidoContrincante>=0) {//Comprobamos se o opoñente repite unha xogada. (Sempre que non levemos perdidas deamsiadas)
				psi29_ColumRow xogContr = psi29_ColumRow.getColumRowByPosition(listColumRow, numMaisRepetidoContrincante);
				if(xogContr.maxDiffPayoff==0 && !xogContr.isComplete(tamMatriz)) {//Se o contrincante repite unha Fila/Columna que está incompleta completamos Matriz
					tipoXogo = XogoCompletar;
				}
				else {
					tipoXogo = XogoAtaque;
					posXogadaAtaque = numMaisRepetidoContrincante;
				}
			}
			else {
				if(contXogadaCiclica==0) { //Se imos perdendo pero non levamos perdidas moitas seguidas, activamos o xogo Dominante1.
					tipoXogo=XogoDominante1;
				}
				else if(histXogadas.get(histXogadas.size()-1).perdido) { //Se non perdín a anterior xogada mellor cambiamos, se si a perdín, volvo cambiar.
					if(tipoXogo<ultimaXogadaCiclica)
						tipoXogo++;
					if(tipoXogo==ultimaXogadaCiclica)
						tipoXogo=10;
				}
				contXogadaCiclica=0;

			}		
			
		}
		else {	//Vou gañando
			if(numRondasPendentes<numCambMatriz || numCambMatriz==0 || porCambMatriz==0) {		//Non vai haber mais cambios de matriz
								
				//Se imos gañando e a matriz non vai ter mais cambios, buscamos se existe algunha fila cuya diferencia de payoff máximo
				//sexa inferior a diferencia de payofftotal entre o numero de rondas pendentes (Ademais de que esta fila/Columna estea completa)
				//Se o encontramolo xogaremos esa Fila/Columna desta maneira asegurámos a victoria en esta Partida.
				
				for(int i=0; i<listColumRow.size(); i++) {
					
					if(listColumRow.get(i).minDiffPayoff*-1>=((float)diffPayoff/numRondasPendentes))
						continue;
										
					if(!listColumRow.get(i).isComplete(tamMatriz))
						continue;
					
					tipoXogo = XogoSeguro;
					posXogadaSeguro = listColumRow.get(i).posColRow;
					
					printMSX("Activo xogo seguro");
					break;				
				}
				
			}
			
			//Non se activou o Xogo seguro
			//Pode ser que existan cambios pendentes da matriz ou que non hai unha posición que nos asegure unha victoria
			//Polo tanto intentaremos analizar o xogo para seguir gañando o maior payoff.
			if(tipoXogo!=XogoSeguro) { //Non hai xogo seguro
				
				
				
				//Se a posición actual onde mais gaño non perdo, xogamos esa.
				psi29_ColumRow posMaisGanhadas = Collections.max(listColumRow, new psi29_CompareGanhadas());
				if(posMaisGanhadas.posganhadas > tamMatriz/2 && posMaisGanhadas.posPerdidas == 0) {
					tipoXogo = XogoDominante2;
					return;
				}
				
				//Se entre os completos existe unha posición onde sempre gañamos xogaremos Dominante2.
				ArrayList<psi29_ColumRow> completos;
				completos = psi29_ColumRow.ColumRowComplete(listColumRow);
				for(int i=0; i<completos.size(); i++) {
					if(completos.get(i).posPerdidas==0){
						tipoXogo = XogoConservador;
						posXogoConservador = completos.get(i).posColRow;
						return;
					}
						
				}
			
				//Comprobamos se o opoñente repite unha xogada.
				int numMaisRepetidoContrincante = psi29_HistXogadas.numMaisRepetido(histXogadas, tamMatriz, 85);
				if(numMaisRepetidoContrincante>=0) {
					tipoXogo = XogoAtaque;
					posXogadaAtaque = numMaisRepetidoContrincante;
				}
				else { //Non hai xogo en ataque
					
//					//Se existe unha posición que nunca perdamos xogamos esa.
//					if(Collections.max(listColumRow, new psi29_CompareGanhadas()).posganhadas >= tamMatriz-1) {
//						tipoXogo = XogoDominante2;
//						return;
//					}
					
					
					//Se a diferencia de payoff é moi baixa, xogamos a Fila/Columna onde arrisquemos menos o payoff
					if(diffPayoff<diffPayoffRiesgo) {
						tipoXogo = XogoMinMaxDiff;
						return;
					}
					
					
					ArrayList<psi29_ColumRow> incompletos;
					incompletos = psi29_ColumRow.ColumRowInComplete(listColumRow);
					
					if(incompletos.size()>0) {
						//Parte problemática. Se comezamos a buscar de maneira aleatoria é coincide que xogamos posicións que perdemos
						//en caso de que exista unha fila donde nunca se perde, non poderemos recuperar as victorias.
						psi29_ColumRow MaisGanhadasIncompletos = Collections.max(incompletos, new psi29_CompareGanhadas());
						if(MaisGanhadasIncompletos.posPerdidas<=1 && MaisGanhadasIncompletos.posComplete>tamMatriz/2)
							tipoXogo = XogoDominante2;
						else {		//Se existen posicións sen completar buscamos 
							tipoXogo = XogoCompletar;
						}
					 }
					else { //A Matriz está completa vou xogar unha estratexia dominante 1
						tipoXogo = XogoDominante1;
					}
				}			
			}			
		}
	}//Fin controlEstado
	
	/**
	 * Función para actualizar os parámetros de un novo xogo.
	 * @param numXogadores  Número de xogadores no xogo.
	 * @param tamMatriz Tamaño da matriz no xogo.
	 * @param numRondas Número de rondas en cada partida no xogo actual.
	 * @param numCambMatriz Número de rondas para que cambie a matriz.
	 * @param porCambMatriz Porcentaxe de cambio de matriz.
	 */
	public void inicioXogo(int numXogadores, int tamMatriz, int numRondas, int numCambMatriz, int porCambMatriz) {
		super.inicioXogo(numXogadores, tamMatriz, numRondas, numCambMatriz, porCambMatriz);
		Matriz = new int[3][tamMatriz][tamMatriz];
	}//inicioXogo
	
	/**
	 * Función para informar dos xogadores que intervirán na nova partida
	 * @param idXog1 Id do xogador con menor id (Xogador de filas)
	 * @param idXog2 Id do xogador con maior id (Xogador de columnas)
	 */
	public void inicioPartida(int idXog1, int idXog2) {
		
		//Resetea a matriz memorizada
		for(int i=0; i<tamMatriz; i++) {
			for(int j=0; j<tamMatriz; j++) {
				Matriz[0][i][j] = -1;
				Matriz[1][i][j] = -1;
				Matriz[2][i][j] = -1; //Indica se os datos se corresconden a esta partida ou polo contrario están pendentes de actualizar
			}
		}
		
		//A partida sempre se inicia completando a matriz
		tipoXogo = XogoAleatorio;
		
		//Eliminamos o historico e os datos todos da matriz
		listColumRow.clear();
		histXogadas.clear();
		
		//Obtenemos nuestra posición en la matriz y la del contrario.
		if(idXog1==Id) {
//			miPos=0;
//			tuPos=1;
			Id_o = idXog2;
		}
		if(idXog2==Id) {
//			miPos=1;
//			tuPos=0;
			Id_o = idXog1;
		}
		
		contXogadaCiclica=0;
		
		//Reiniciamos  os parametros do xogador e do opoñente.
		reiniciarXogador();
		reiniciarOponente();
		
		//listColumRow = psi29_ColumRow.getNewColumRowParams(tamMatriz, Matriz);
		//Creamos a nova lista de Fila/Columna valeira.
		for(int i=0; i<tamMatriz; i++) {
			listColumRow.add(new psi29_ColumRow(i));
		}
		
	}//inicioPartida
	
	/**
	 * Función para informar a intelixencia artifial do fin da partida.
	 */
	public void finPartida() {
	}//finPartida
	
	/**
	 * Función para informar a intelixencia artificial do cambio da matriz en un porcentaxe
	 * @param porCamMatriz Porcentaxe de cambio de matriz.
	 */
	public void cambioMatriz(int porCamMatriz) {
		
		if(porCambMatriz>=porcentajeReinicioMatriz) {
			tipoXogo = XogoAleatorio;
			//Resetea a matriz memorizada
			for(int i=0; i<tamMatriz; i++) {
				for(int j=0; j<tamMatriz; j++) {
					Matriz[0][i][j] = -1;
					Matriz[1][i][j] = -1;
					Matriz[2][i][j] = -1;
				}
			}
		}
		else {
			for(int i=0; i<tamMatriz; i++) {
				for(int j=0; j<tamMatriz; j++) {
					Matriz[2][i][j] = -1;
				}
			}
		}
		
	}//cambioMatriz
	
	
	public void printMatriz() {
		if(!modedebug || !getLocalName().equals(debugPlayerName))
			return;
					
		String matriz="";
		psi29_ColumRow params;
		for(int i=0; i<tamMatriz; i++) {
			for(int j=0; j<tamMatriz; j++) {
								
				matriz+="(";
				matriz+=(Matriz[0][i][j]==-1)? "/" : Matriz[0][i][j];
				matriz+=",";
				matriz+=(Matriz[1][i][j]==-1)? "/" : Matriz[1][i][j];
				matriz+=") ";
			}
			params = listColumRow.get(i);
			matriz+=" - "+params.toString();
			matriz+="\n";
		 }		
		printMSX("");
		printMSX(matriz);
	}
	
	
	public void printMSX(String msx) {
		if(modedebug && getLocalName().equals(debugPlayerName))
			System.out.println(msx);
	}
	
	
	//#################################################################################
	//#########################      Estratexias de xogo     ##########################
	//#################################################################################
	
	
	/**
	 * Estratexia MaxMin. Obtén a posición onde o cobro mínimo do xogador actual sexa o maior posible
	 * @return  Posición MaxMin
	 */
	public int getMaxMin() {
		return Collections.max(listColumRow, new psi29_CompareMaxMin()).posColRow;
	}//fin getMaxMin
	
	/**
	 * Estratexia MinMax. Obtén a posición onde o pago máximo do opoñente sexa o menor posible
	 * @return Posición MinMax
	 */
	public int getMinMax() {
		return Collections.max(listColumRow, new psi29_CompareMinMax()).posColRow;
	}//fin getMinMax
	
	/**
	 * Escolle a posición onde a diferencia de payoff (en negativo) do xogador sexa a mais baixa. Similar a un MinMax
	 * @return Devolve a posición da Fila/Columna onde a pérdida máxima sexa a menor.
	 */
	public int getMinMaxDiff() {
		return Collections.max(listColumRow, new psi29_CompareMinDiffPayoff()).posColRow;
	}//fin getMinMaxDiff
	
	/**
	 * Escolle a posición onde a diferencia de payoff (en positivo) máxima sexa a mais alta.
	 * @return Devolve a posición da Fila/Columna onde a beneficio máximo sexa o maior.
	 */
	public int getMaxMaxDiff() {
		return Collections.max(listColumRow, new psi29_CompareMaxDiffPayoff()).posColRow;
	}//fin getMaxMaxDiff
	
	public int getDominantePorDiffPayoff() {
		return Collections.max(listColumRow, new psi29_CompareDiffPayoff()).posColRow;
	}//fin getDominantePorDiffPayoff
	
	public int getDominantePorGanhadas() {
		return Collections.max(listColumRow, new psi29_CompareGanhadas()).posColRow;
	}//fin getDominantePorGanhadas
	
	
	public int getCompletar() {
		ArrayList<psi29_ColumRow> incompletos;
		incompletos = psi29_ColumRow.ColumRowInComplete(listColumRow);
		if(incompletos.size()>0)
			return incompletos.get(rand.nextInt(incompletos.size())).posColRow;
		else {
			printMSX("Esto non deberia pasar. Solicitase completar a matriz pero está completa");
			return 0;
		}
	}
	
	/**
	 * Calcula a posición onde a diferencia de payoff sexa maior (en beneficio do xogador) en unha Columna/Fila en concreto
	 * @param ColRow Columna/Fila que se selecciona como fixa para o calculo
	 * @return Columna/Fila coa mellor elección.
	 */
	public int getMellor(int ColRow) {
		
		int diffPayoff=0, diffPayoffMax=-10, posPayoffMax=0, posUnknow=-1;
		
		for(int i=0; i<tamMatriz; i++) {
			
			diffPayoff = Matriz[0][i][ColRow]-Matriz[1][i][ColRow];

			if(Matriz[0][i][ColRow]==-1) {
				posUnknow=i;
			}
			
			if(diffPayoff>diffPayoffMax) {
				diffPayoffMax = diffPayoff;
				posPayoffMax = i;
			}
		}
		
		//En caso de que a diferencia de payoff máximo sexa 0, e exista algunha posición descoñecida, escollemos esta, para ver cal é o valor.
		if(diffPayoffMax==0 && posUnknow!=-1) {
			posPayoffMax = posUnknow;
		}

		return posPayoffMax;
	}//Fin getMellor
	
}
