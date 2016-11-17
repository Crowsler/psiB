
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
@SuppressWarnings("serial")
public class Xogador extends Agent {

	private int Id;
	private static String Tipo="Fijo";
	private int Gañadas=0;
	private int Perdidas=0;
	private int PagoParcial=0;
	private int PagoTotal=0;
	
	
	public Xogador()
	 {
		Id = this.hashCode();
	 }	

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
	
	public void reiniciarXogador()
	 {
		Gañadas=0;
		Perdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }

	
	public int xogada(int tamMatiz)
	 {
		return 0;
	 }
	
	
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
    }

	
}
