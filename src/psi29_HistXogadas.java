import java.util.ArrayList;


/**
 * 
 * Parámetros de cada xogada.
 * Que xogou o propio xogador e que xogou o opoñente
 * Isto permitenos levar un historico das xogadas.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */
public class psi29_HistXogadas {

	
	//public static final int XogoDesconocido = -1;
	public static final int VentanaRepeticionMIN = 5;
	public static final int PartidasPerdidasParaXogoCiclico = 3;

	
	public static int VentanaRepeticion = 5;
	int xogadaProp=0;
	int xogadaCont=0;
	boolean ganhado=false;
	boolean perdido=false;
	
	public psi29_HistXogadas() {}
	
	public psi29_HistXogadas(int xogadaProp, int xogadaCont, int payoffProp, int payoffCont) {
		
		this.xogadaProp = xogadaProp;
		this.xogadaCont = xogadaCont;
		
		if(payoffProp>payoffCont) {
			ganhado=true;
		}
		if(payoffProp<payoffCont) {
			perdido=true;
		}
	}
	
	/**
	 * Analiza se durante un número de xogadas pasadas do tamaño da VentanaRepeticion, o contrincante repetiu unha xogada mais de un porcentax
	 * @param xogadas	Listado das xogadas anteriores
	 * @param tamMatriz	Tamaño da matriz de xogo
	 * @param porRep	Porcentaxe que se debe repetir un número para que sexa considerado como a proxima posible xogada. (Debe ser polo menos 50)
	 * @return			Número que se repite polo menos o porcentaxe indicado, en caso de devolver XogoDesconocido quere dicir que non se detecta un patrón de repetición
	 */
	public static int numMaisRepetido(ArrayList<psi29_HistXogadas> xogadas, int tamMatriz, int porRep) {	
		
		if(xogadas.size()>=VentanaRepeticion) {	
			
			//Inicializamos un vector que conta as veces que se repite unha xogada
			int[] repeticions = new int[tamMatriz];
			int posMax=0, valorMax=0;
			
			for(int i=0; i<tamMatriz; i++) {
				repeticions[i]=0;
			}

			//Detección de repetición
			for(int i=xogadas.size()-1; i>=0 && xogadas.size()-1-i<VentanaRepeticion; i--) {
				repeticions[xogadas.get(i).xogadaCont]++;
			}
			
			int numRep = Math.min(VentanaRepeticion, xogadas.size());
			
			//Se detectamos que un número se repite mais do 80%, supoñemos que se xogará así.
			for(int i=0; i<tamMatriz; i++) {
				if(repeticions[i]>valorMax) {
					valorMax = repeticions[i];
					posMax = i;
				}
			}
			if((valorMax*100/numRep)>=porRep)
				return posMax;
		 }
		
		return -1;
	}//Fin numMaisRepetido
	
	
	
	public static int vecesPerdidasVentana(ArrayList<psi29_HistXogadas> xogadas) {
		int contador=0;
		for(int i=xogadas.size()-1; i>=0 && xogadas.size()-1-i<PartidasPerdidasParaXogoCiclico; i--) {
			if(xogadas.get(i).perdido) {
				contador++;
			}
		}
		return contador;
	}//fin vecesPerdidasEn
	
	public static int EstadoDeHistorico() {

		
		
		return -1;
	}
	
}
