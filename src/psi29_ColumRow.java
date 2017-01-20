import java.util.ArrayList;
import java.util.Comparator;

/**
 * 
 * Parámetros de cada Fila/Columna da matriz
 * Cada vez que se actualiza a matriz, debemos actualizar estos parámetros
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
public class psi29_ColumRow{

	public int diffPayoff=0;			//Diferencia total de payoff en toda a Fila/Columna
	public int posganhadas=0;			//Número de posicións da Fila/Columna na que gaña o xogador
	public int posPerdidas=0;			//Número de posicións da Fila/Columna na que perde o xogador
	public int posComplete=0;			//Número de posicións xa coñecidas da matriz
	public int posColRow=0;				//Posición da Fila/Columna na matriz, comezando en 0 hasta o tamaño da matriz 
	public int minDiffPayoff=0;			//Diferencia de payoff mínimo da Fila/Columna
	public int posMinDiffPayoff=0;		//Posición na Fila/Columna onde se encontra o minDiffPayoff
	public int maxDiffPayoff=0;			//Difereencia de payoff máximo da Fila/Columna
	public int posMaxDiffPayoff=0;		//Posición na Fila/Columna onde se enconra o maxDiffPayoff
	public int minCobro=0;				//Cobro mínimo
	public int maxPago=0;				//Pago máximo
	
	public psi29_ColumRow() {}
	
	public psi29_ColumRow(int pos) {
		posColRow=pos;
	}
	
	/**
	 * Devolve un ArrayList coas Filas/Columnas que están imcompletas.
	 * @param ColumRow ArrayList dos parámetros Filas/Columnas
	 * @return ArrayList de Filas/Columnas imcompletas
	 */
	public static ArrayList<psi29_ColumRow> ColumRowInComplete(ArrayList<psi29_ColumRow> ColumRow) {
		
		ArrayList<psi29_ColumRow> ret = new ArrayList<psi29_ColumRow>();
		
		for(int i=0; i<ColumRow.size(); i++) {
			if(!ColumRow.get(i).isComplete(ColumRow.size()))
				ret.add(ColumRow.get(i));
		}
		return ret;
	}
	
	/**
	 * Devolve un ArrayList coas Filas/Columnas que xa están completas.
	 * @param ColumRow ArrayList dos parámetros Filas/Columnas
	 * @return ArrayList de Filas/Columnas completas
	 */
	public static ArrayList<psi29_ColumRow> ColumRowComplete(ArrayList<psi29_ColumRow> ColumRow) {
		
		ArrayList<psi29_ColumRow> ret = new ArrayList<psi29_ColumRow>();
		
		for(int i=0; i<ColumRow.size(); i++) {
			if(ColumRow.get(i).isComplete(ColumRow.size()))
				ret.add(ColumRow.get(i));
		}
		return ret;
	}//ColumRowComplete
	
	/**
	 * Actualiza os parámetros da lista de Filas/Columnas
	 * @param ColumRow	Lista de parámetros a actualizar
	 * @param tamMatriz	Tamaño da matriz
	 * @param Matriz	Matriz
	 */
	public static void actualizaColuRowParametros(ArrayList<psi29_ColumRow> ColumRow, int tamMatriz, int[][][] Matriz) {
		
		for(int i=0; i<ColumRow.size(); i++) {
			ColumRow.get(i).actualizaParametros(tamMatriz, Matriz);
		}
		
	}//actualizaColumRowParametros
	
	/**
	 * Obtén os parámetros de unha Fila/Columna a partir da súa posición
	 * @param ColumRow	ArrayList de Filas/Columnas
	 * @param position	Posición da Fila/Columna que estamos buscando
	 * @return	Fila/Columna que pertence a posición pasada
	 */
	public static psi29_ColumRow getColumRowByPosition(ArrayList<psi29_ColumRow> ColumRow, int position) {
		
		for(int i=0; i<ColumRow.size(); i++) {
			if(ColumRow.get(i).posColRow==position) {
				return ColumRow.get(i);
			}
		}
		return null;
	}//getColumRowByPosition
	
	
	/**
	 * Actualiza os parámetros da Fila/Columna en concreto
	 * @param tamMatriz	Tamaño da matriz
	 * @param Matriz	Matriz
	 */
	public void actualizaParametros(int tamMatriz, int[][][] Matriz) {
		int miPayoff, tuPayoff, tempMinCobro=9, tempMaxPago=0;
		
		restart();
		
		for(int i=0; i<tamMatriz; i++) { 
		
			miPayoff = Matriz[0][posColRow][i];
			tuPayoff = Matriz[1][posColRow][i];
			
			if(miPayoff-tuPayoff>maxDiffPayoff) {
				maxDiffPayoff=miPayoff-tuPayoff;
				posMaxDiffPayoff=i;
			}
			
			if(miPayoff-tuPayoff<minDiffPayoff) {
				minDiffPayoff=miPayoff-tuPayoff;
				posMinDiffPayoff=i;
			}
			
			
			if(miPayoff>=0) {
				posComplete++;
				diffPayoff+=miPayoff-tuPayoff;
				
				if(miPayoff>tuPayoff){
					posganhadas++;
				}
				else if(miPayoff<tuPayoff) {
					posPerdidas++;
				}
			}
			
			if(Matriz[0][posColRow][i]!=-1 && Matriz[0][posColRow][i]<tempMinCobro) {
				tempMinCobro=Matriz[0][posColRow][i];
			}
			if(Matriz[1][posColRow][i]!=-1 && Matriz[1][posColRow][i]>tempMaxPago) {
				tempMaxPago=Matriz[1][posColRow][i];
			}
			
		}
		minCobro=tempMinCobro;
		maxPago=tempMaxPago;
	}//actualizaParametros

	public int getDiffPayoff() {
		return diffPayoff;
	}

	public void setDiffPayoff(int diffPayoff) {
		this.diffPayoff = diffPayoff;
	}

	public int getPosganhadas() {
		return posganhadas;
	}

	public void setPosganhadas(int posganhadas) {
		this.posganhadas = posganhadas;
	}

	public int getPosPerdidas() {
		return posPerdidas;
	}

	public void setPosPerdidas(int posPerdidas) {
		this.posPerdidas = posPerdidas;
	}

	public int getPosComplete() {
		return posComplete;
	}

	public void setPosComplete(int complete) {
		this.posComplete = complete;
	}
	
	/**
	 * Comprueba se esta Fila/Columna está completa
	 * @param tamMatriz Tamaño de la matriz
	 * @return	True si está completa, False si está incompleta
	 */
	public boolean isComplete(int tamMatriz) {
		if(tamMatriz==posComplete)
			return true;
		else
			return false;
	}//isComplete

	/**
	 * Pon os parámetros básicos da Fila/Columna a 0.
	 */
	public void restart() {
		diffPayoff=0;
		posganhadas=0;
		posPerdidas=0;
		posComplete=0;
		minDiffPayoff=10;
		posMinDiffPayoff=0;
		maxDiffPayoff=-10;
		posMaxDiffPayoff=0;
		minCobro=0;
		maxPago=0;
	}//restart
	
	/**
	 * Retorna un String visible cos parámetros da Fila/Columna entre paréntesis.
	 */
	public String toString() {
		String ret="";
		
		ret+="(";
		ret+=diffPayoff;
		ret+=", ";
		ret+=posganhadas;
		ret+=", ";
		ret+=posPerdidas;
		ret+=", ";
		ret+=maxDiffPayoff;
		ret+=", ";
		ret+=minDiffPayoff;
		ret+=", ";
		ret+=minCobro;
		ret+=", ";
		ret+=maxPago;
		ret+=")";
				
		return ret;
	}//toString	
	
}//Fin Clase psi29_ColumRow


/**
 * 
 * Clase que permite comparar as Filas/Columnas polo número de posiciones ganadas, en caso de igualdade comproba as posicións perdidas e finalmente a diferencia de payoff total
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareGanhadas implements Comparator<psi29_ColumRow> {
	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {
		if(o1.getPosganhadas()>o2.posganhadas) return 1;
		if(o1.getPosganhadas()<o2.posganhadas) return -1;
		if(o1.getPosPerdidas()<o2.posPerdidas) return 1;
		if(o1.getPosPerdidas()>o2.posPerdidas) return -1;
		if(o1.getDiffPayoff()>o2.diffPayoff) return 1;
		if(o1.getDiffPayoff()<o2.diffPayoff) return -1;
		return 0;
	}
}//Fin Clase psi29_CompareGanhadas

/**
 * 
 * Clase que permite comparar as Filas/Columnas pola diferencia total de payoff, en caso de igualdade plo número de posicións gañadas e finalmente polas posicións perdidas.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareDiffPayoff implements Comparator<psi29_ColumRow> {
	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {
		if(o1.getDiffPayoff()>o2.diffPayoff) return 1;
		if(o1.getDiffPayoff()<o2.diffPayoff) return -1;
		if(o1.getPosganhadas()>o2.posganhadas) return 1;
		if(o1.getPosganhadas()<o2.posganhadas) return -1;
		if(o1.getPosPerdidas()<o2.posPerdidas) return 1;
		if(o1.getPosPerdidas()>o2.posPerdidas) return -1;
		return 0;
	}
}//Fin Clase psi29_CompareDiffPayoff

/**
 * 
 * Clase que permite comparar as Filas/Columnas pola diferencia máxima de payoff que existe en cada un
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareMaxDiffPayoff implements Comparator<psi29_ColumRow> {

	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {
		if(o1.maxDiffPayoff > o2.maxDiffPayoff) return 1;
		if(o1.maxDiffPayoff < o2.maxDiffPayoff) return -1;
		if(o1.getPosganhadas()>o2.posganhadas) return 1;
		if(o1.getPosganhadas()<o2.posganhadas) return -1;
		if(o1.getPosPerdidas()<o2.posPerdidas) return 1;
		if(o1.getPosPerdidas()>o2.posPerdidas) return -1;
		return 0;
	}
}//Fin Clase psi29_CompareMaxDiffPayoff

/**
 * 
 * Clase que permite comparar as Filas/Columnas pola diferencia mínima de payoff que existe en cada un
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareMinDiffPayoff implements Comparator<psi29_ColumRow> {

	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {
		if(o1.minDiffPayoff > o2.minDiffPayoff) return 1;
		if(o1.minDiffPayoff < o2.minDiffPayoff) return -1;
		if(o1.getPosganhadas()>o2.posganhadas) return 1;
		if(o1.getPosganhadas()<o2.posganhadas) return -1;
		if(o1.getPosPerdidas()<o2.posPerdidas) return 1;
		if(o1.getPosPerdidas()>o2.posPerdidas) return -1;
		return 0;
	}
}//Fin Clase psi29_CompareMinDiffPayoff

/**
 * 
 * Clase que permite comparar as Filas/Columnas por o cobro mínimo que existe en cada un
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareMaxMin implements Comparator<psi29_ColumRow> {
	
	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {

		return o1.minCobro > o2.minCobro ? 1 : o2.minCobro==o1.minCobro ? 0 : -1;
	}
}//Fin Clase psi29_CompareMaxMin

/**
 * 
 * Clase que permite comparar as Filas/Columnas por o pago máximo que existe en cada un
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
class psi29_CompareMinMax implements Comparator<psi29_ColumRow> {

	
	@Override
	public int compare(psi29_ColumRow o1, psi29_ColumRow o2) {

		return o1.maxPago < o2.maxPago ? 1 : o2.maxPago==o1.maxPago ? 0 : -1;
	}
}//Fin Clase psi29_CompareMinMax