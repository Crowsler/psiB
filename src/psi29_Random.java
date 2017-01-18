import java.util.Random;

@SuppressWarnings("serial")
/**
 * Hereda da clase psi29_Fixed, que se conten as funcións báscas,
 * rexistra o axente no DF e intercambia mensaxes co MainAgent.
 * Este implementará unha función para devolver un valor aleatorio a unha xogada.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */
public class psi29_Random extends psi29_Fixed {


	Random rand;
	/**
	 * Constructor do axente Random, simplemente chama o constructor da clase Fixed e modifica o seu tipo por Aleatorio.
	 */
	public psi29_Random() {
		super();
		Tipo="Aleatorio";
		rand = new Random();
	}
	
	
	/**
	 * Escolle a xogada (En este caso un random entre 0 e tamaño da matriz-1)
	 * @param tamMatiz tamaño da matriz do xogo
	 * @return Devolve un int coa (columna/fila) que escolliu
	 */
	public int xogada() {
		return rand.nextInt(tamMatriz);
	}//xogada
	
}//Clase psi29_Random
