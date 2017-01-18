import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * 
 * Xogador, será cada axente que xogue o xogo.
 * Este será de varios tipos. (Fixo, Random, Intelixente)
 * Calculará o movemento adecuado e intercambiará os mensaxes co arbitro do xogo.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */

@SuppressWarnings("serial")
public class psi29_Fixed extends Agent {

	//Parámetros de xogador
	protected int Id;
	protected String Tipo="Fixo";
	protected int RondasGanhadas=0;
	protected int RondasPerdidas=0;
	protected int PartidasGanhadas=0;
	protected int PartidasPerdidas=0;
	protected int PagoParcial=0;
	protected int PagoTotal=0;
	
	//Parámetros de opoñente
	protected int Id_o;
	protected int RondasGanhadas_o=0;
	protected int RondasPerdidas_o=0;
	protected int PartidasGanhadas_o=0;
	protected int PartidasPerdidas_o=0;
	protected int PagoParcial_o=0;
	protected int PagoTotal_o=0;
	
	protected static final String tipoPublicado = "Player";
	
	//Datos de Xogo
	protected int numRondas=0;											//Número de rondas por cada partida
	protected int numCambMatriz=0;										//Número de rondas que pasan para o cambio da matriz
	protected int tamMatriz=0;											//Tamaño da matriz
	protected int porCambMatriz=0;										//Pocentaxe de renovación da Matriz.
	protected int[][][] Matriz = new int[2][tamMatriz][tamMatriz];		//Matriz do xogo
	protected int numXogadores=0;										//Número de xogadores

	
	/**
	 * Constructor do xogador.
	 */
	public psi29_Fixed()
	 {
		//Id = this.hashCode();
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

	public int getRondasGanhadas() {
		return RondasGanhadas;
	}


	public void setRondasGanhadas(int rondasGanhadas) {
		RondasGanhadas = rondasGanhadas;
	}


	public int getRondasPerdidas() {
		return RondasPerdidas;
	}


	public void setRondasPerdidas(int rondasPerdidas) {
		RondasPerdidas = rondasPerdidas;
	}


	public int getPartidasGanhadas() {
		return PartidasGanhadas;
	}


	public void setPartidasGanhadas(int partidasGanhadas) {
		PartidasGanhadas = partidasGanhadas;
	}


	public int getPartidasPerdidas() {
		return PartidasPerdidas;
	}


	public void setPartidasPerdidas(int partidasPerdidas) {
		PartidasPerdidas = partidasPerdidas;
	}	

    public int getNumRondas() {
		return numRondas;
	}


	public void setNumRondas(int numRondas) {
		this.numRondas = numRondas;
	}


	public int getNumCambMatriz() {
		return numCambMatriz;
	}


	public void setNumCambMatriz(int numCambMatriz) {
		this.numCambMatriz = numCambMatriz;
	}


	public int getTamMatriz() {
		return tamMatriz;
	}


	public void setTamMatriz(int tamMatriz) {
		this.tamMatriz = tamMatriz;
	}


	public int getPorCambMatriz() {
		return porCambMatriz;
	}


	public void setPorCambMatriz(int porCambMatriz) {
		this.porCambMatriz = porCambMatriz;
	}


	public int getNumXogadores() {
		return numXogadores;
	}


	public void setNumXogadores(int numXogadores) {
		this.numXogadores = numXogadores;
	}
	
	
	/**
	 * Pon a cero todas as estadisticas do xogador
	 */
	public void reiniciarXogador()
	 {
		RondasGanhadas=0;
		RondasPerdidas=0;
		PagoParcial=0;
		PagoTotal=0;
	 }//reiniciarXogador
	
	/**
	 * Pon a cero todas as estadisticas do Opoñente
	 */
	public void reiniciarOponente()
	 {
		RondasGanhadas_o=0;
		RondasPerdidas_o=0;
		PagoParcial_o=0;
		PagoTotal_o=0;
	 }//reiniciarOponente

	/**
	 * Escolle a xogada (En este caso un fixo)
	 * @param tamMatiz tamaño da matriz do xogo
	 * @return Devolve un int coa (columna/fila) que escolliu
	 */
	public int xogada()
	 {
		return 0;
	 }//xogada
	
	
	/**
	 * Función que actualizará os parámetros xogados na última ronda
	 * Só é util para o xogador intelixente.
	 * @param xogada1 Fila seleccionada polo xogador de menor id
	 * @param xogada2 Columna seleccionada polo xogador de maior id
	 * @param payoff1 Payoff do xogador de menor id
	 * @param payoff2 Payoff do xogador de maior id
	 */
	public void resultado(int xogada1, int xogada2, int payoff1, int payoff2) {
	}//resultado
	
	
	/**
	 * Función para actualizar os parámetros de un novo xogo.
	 * @param numXogadores  Número de xogadores no xogo.
	 * @param tamMatriz Tamaño da matriz no xogo.
	 * @param numRondas Número de rondas en cada partida no xogo actual.
	 * @param numCambMatriz Número de rondas para que cambie a matriz.
	 * @param porCambMatriz Porcentaxe de cambio de matriz.
	 */
	public void inicioXogo(int numXogadores, int tamMatriz, int numRondas, int numCambMatriz, int porCambMatriz) {
		this.numXogadores = numXogadores;
		this.tamMatriz = tamMatriz;
		this.numRondas = numRondas;
		this.numCambMatriz = numCambMatriz;
		this.porCambMatriz = porCambMatriz;
	}//inicioXogo
	
	/**
	 * Función para informar dos xogadores que intervirán na nova partida
	 * @param idXog1 Id do xogador con menor id (Xogador de filas)
	 * @param idXog2 Id do xogador con maior id (Xogador de columnas)
	 */
	public void inicioPartida(int idXog1, int idXog2) {
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
	}//cambioMatriz
	
	/**
	 * Rexistra o axente no rexitro DF co servicio Player.
	 * Ademais tamén se publica como servicio do tipo que lle corresponde
	 */
    public void register()
    {
    	// Descripción do axente
        DFAgentDescription descripcion = new DFAgentDescription();
        descripcion.setName(getAID());
        
        // Descripcion do servicio principal que se proporciona
        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(tipoPublicado);
        servicio.setName(this.getLocalName());
        
        //Descripción do servicio tipo (Só se utiliza para un MainAgent adecuado)
        ServiceDescription servicioTipo = new ServiceDescription();
        servicioTipo.setType(Tipo);
        servicioTipo.setName(this.getLocalName());
 
        // Engade os servicios a descripción
        descripcion.addServices(servicio);
        descripcion.addServices(servicioTipo);
 
        //Rexitrase no DF
        try {
            DFService.register(this, descripcion);
         }
	    catch (FIPAException e) {
            e.printStackTrace();
         }
    }//register
 
    /**
     * Dase de baixa o servicio no DF
     */
    public void takeDown() {
        try
        {
            DFService.deregister(this);
        }
        catch (FIPAException fe)
        {
            fe.printStackTrace();
        }
    }//takeDown
	

    /**
     * Behaviour cíclico para a recepción de mensaxes
     * Segundo o mensaxe recivido realiza unhas acción ou outras
     * 
     * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
     * @version 0.2
     *
     */
	class xogadorProcesaMensaxes extends CyclicBehaviour
    {
		
        public void action() {
            //Obten a mensaxe
            ACLMessage mensaje = blockingReceive();
                        
            if (mensaje!= null) {
            	
            	//Colle a primeira parte do mensaxe, que indicará que acción se debe realizar.
                String mTipo = mensaje.getContent().split("#")[0];

                //En caso de Id indica un novo xogo, spliteamos a mensaxe e chamamos a función inicioXogo que actualizará os parámetros do mesmo
            	if(mTipo.equals("Id")) {
            		int id = Integer.parseInt(mensaje.getContent().split("#")[1]);
            		setId(id);
            		
            		String parametros = mensaje.getContent().split("#")[2];
            		String[] param = parametros.split(",");
            		
            		inicioXogo(Integer.parseInt(param[0]), Integer.parseInt(param[1]), Integer.parseInt(param[2]), Integer.parseInt(param[3]), Integer.parseInt(param[4]));
            	}
            	//En caso de NewGame chamamos a splitamos os ids dos xogadores que interveñen na partida e informamos a función para nova partida.
            	else if(mTipo.equals("NewGame")) {
            		String[] param = mensaje.getContent().split("#")[1].split(",");
            		inicioPartida(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
            	}
            	//En caso de Position chamamos a función para obter a xogada e respondemos con esta.
            	else if(mTipo.equals("Position")) {
            		// Creación do mensaxe
            		ACLMessage mPosicion = new ACLMessage(ACLMessage.INFORM);
            		mPosicion.setSender(getAID());
            		mPosicion.addReceiver(mensaje.getSender());
            		//ACLMessage mPosicion = mensaje.createReply();
        		    mPosicion.setContent("Position#"+xogada());
        		    send(mPosicion);
            	}
            	//En caso de Results spliteamos a mensaxe e pasámoslle os resultados da ronda a función.
            	else if(mTipo.equals("Results")) {
            		String[] xogadas = mensaje.getContent().split("#")[1].split(",");
            		String[] payoffs = mensaje.getContent().split("#")[2].split(",");
            		resultado(Integer.parseInt(xogadas[0]), Integer.parseInt(xogadas[1]), Integer.parseInt(payoffs[0]), Integer.parseInt(payoffs[1]));
            	}
            	//En caso de EndGame, chama a función de finPartida.
            	else if(mTipo.equals("EndGame")) {
            		finPartida();
            	}
            	//En caso de Changed, chama a función de cambioMatriz.
            	else if(mTipo.equals("Changed")) {
            		cambioMatriz(Integer.parseInt(mensaje.getContent().split("#")[1]));
            	}
             }
         }//action
        
    }//Clase xogadorProcesaMensaxes
	
	/**
	 * Función que inicia o axente. Rexistrase e engade o Behaviour para procesar os mensaxes.
	 */
    protected void setup()
    {
    	register();
    	addBehaviour(new xogadorProcesaMensaxes());
    }//setup
	
}//Clase Xogador
