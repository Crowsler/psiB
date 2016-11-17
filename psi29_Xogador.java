/*
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
*/

/**
 * 
 * Xogador, será cada axente que xogue o xogo.
 * Este será de varios tipos. (Fixo, Random, Intelixente)
 * Calculará o movemento adecuado e intercambiará os mensaxes co arbitro do xogo.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */

public class psi29_Xogador {

	private int Id;
	private static String Tipo="Fijo";
	private int Gañadas=0;
	private int Perdidas=0;
	private int PagoParcial=0;
	private int PagoTotal=0;
	
	/**
	 * Constructor do xogador. O id de este calculase do hash do obxecto
	 */
	public psi29_Xogador()
	 {
		Id = this.hashCode();
	 }//Xogador

	
	/*------------------------------------------------------
	   ------------  Getters e setters da clase ------------
	   -----------------------------------------------------*/
	public int getPagoParcial() {
		return PagoParcial;
	}

	public void setPagoParcial(int pagoParcial) {
		PagoParcial = pagoParcial;
	}

	public int getPagoTotal() {
		return PagoTotal;
	}

	public void setPagoTotal(int pagoTotal) {
		PagoTotal = pagoTotal;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTipo() {
		return Tipo;
	}

	public int getGañadas() {
		return Gañadas;
	}

	public void setGañadas(int gañadas) {
		Gañadas = gañadas;
	}

	public int getPerdidas() {
		return Perdidas;
	}

	public void setPerdidas(int perdidas) {
		Perdidas = perdidas;
	}
	
	/**
	 * Pon a cero todas as estadisticas do xogador
	 */
	public void reiniciarXogador()
	 {
		Gañadas=0;
		Perdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }//reiniciarXogador

	/**
	 * Escolle a xogada (En este caso un fixo)
	 * @param tamMatiz tamaño da matriz do xogo
	 * @return Devolve un int coa (columna/fila) que escolliu
	 */
	public int xogada(int tamMatiz)
	 {
		return 0;
	 }//xogada
	
	/*
	class ReceptorComportaminento extends SimpleBehaviour
    {
            private boolean fin = false;
            public void action()
            {
                System.out.println(" Preparandose para recibir");
 
            //Obtiene el primer mensaje de la cola de mensajes
                ACLMessage mensaje = receive();
 
                if (mensaje!= null)
                {
                    System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
                    System.out.println(mensaje.toString());
                    fin = true;
                }
            }
            public boolean done()
            {
                return fin;
            }
    }
	
    protected void setup()
    {
        addBehaviour(new ReceptorComportaminento());
    }*/
	
}//Clase Xogador
