import java.util.ArrayList;
import java.util.Iterator;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * Axente árbitro que se encarga de recibir, enviar mensaxes os diferentes axentes xogadores.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class psi29_MainAg extends Agent {
	
	private static final String tipoPublicado = "Player";
	private ArrayList<AID> players = new ArrayList<AID>();		//Listado de axentes ("Player") xa engadidos a lista de xogadores
	private psi29_Xanela xanela;
	private psi29_Xogo xogo;
	
	/**
	 * Inicia o axente, lanza a xanela e obten o xogo da xanela.
	 */
	public psi29_MainAg(){
		xanela = new psi29_Xanela(this);
		xogo = xanela.xogo;
	}
	
	/**
	 * Busca novos "Player" no listado DF
	 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
	 * @version 0.2
	 *
	 */
	class buscarPlayers extends CyclicBehaviour
    {

		@Override
		public void action() {
			
			//Se o xogo está iniciado non busca
			if(xogo.xogoIniciado)
				return;
			
			//Buscamos os servicios de tipo Player
			ServiceDescription servicio = new ServiceDescription();
	        servicio.setType(tipoPublicado);
	        DFAgentDescription descripcion = new DFAgentDescription();
	        descripcion.addServices(servicio);
	        
	        try {
	            DFAgentDescription[] resultados = DFService.search(this.myAgent,descripcion);
	            //Se encontramos un xogador que non estea no listado, engádese.
	            for (int i=0; i<resultados.length; i++) {
	            	if(players.contains(resultados[i].getName()))
	            			continue;
	            	
	            	String tipo_descrito, tipo="";
	            	
	            	//Se algún servicio está publicado con outro tipo ademáis de "Player", e utiliza este como tipo de xogador (Fixo, aleatorio, ...)
	            	@SuppressWarnings("unchecked")
					Iterator<ServiceDescription> it = resultados[i].getAllServices();
	            	while(it.hasNext()) {
	            		tipo_descrito = it.next().getType();
	            		if(!tipo_descrito.equals(tipoPublicado))
	            			tipo=tipo_descrito;
	            	}
	            	
	            	//Creamos un novo Xogador e engadimolo na lista.
	            	xogo.xogadores.add(new psi29_DatosXogador(resultados[i].getName(), tipo));
	            	players.add(resultados[i].getName());
	            	
	            	//Engadimos o xogador na lista da xanela e actualizamos datos de esta.
	            	xanela.ListaXog.add(xogo.xogadores.get(xogo.xogadores.size()-1).toString());
	            	xanela.EtiqNPartidas.setText(psi29_Xanela.TextNPartidas+psi29_Xogo.coefbin(xogo.xogadores.size(), 2));
	            	xanela.EtiqNXogadores.setText(psi29_Xanela.TextNXogadores+xogo.xogadores.size());
	            }
	            
	            //Facemos unha pausa de 1 segundo, para non estar comprobando constantemente no DF
	            try {
	            	Thread.sleep(1000);
	            }catch(Exception e) {
	            	
	            }
	            
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		}
		
    }
	
	/**
	 * Envia o mensaxe de novo Xogo a todos os xogadores involucrados no mesmo
	 * Envia datos como o id que lle corresponde, número de xogadores, tamaño da matriz, número de rondas, rondas de cambio matriz e porcentaxe de cambio de matriz.
	 */
    public void mensaxeNovoXogo()
     {
       	for(int i=0; i<xogo.xogadores.size(); i++) {

       		psi29_DatosXogador xogador = xogo.xogadores.get(i);
		    // Creación do mensaxe
		    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		    mensaje.setSender(getAID());
		    mensaje.addReceiver(xogador.getAxente());
		    mensaje.setContent("Id#"+xogador.getId()+"#"+xogo.xogadores.size()+","+xogo.tamMatriz+","+xogo.numRondas+","+xogo.numCambMatriz+","+xogo.porCambMatriz);
		    send(mensaje);
         }
      }

    /**
     * Envia un mensaxe de nova partida (NewGame) os dous xogadores involucrados en esta, informando o id de ambos xogadores.
     * @param xog1 Xogador con menor id da partida.
     * @param xog2 Xogador con maior id da partida.
     */
    public void mensaxeNovaPartida(psi29_DatosXogador xog1, psi29_DatosXogador xog2) {
    	
    	// Creación do mensaxe
	    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
	    mensaje.setSender(getAID());
	    mensaje.addReceiver(xog1.getAxente());
	    mensaje.addReceiver(xog2.getAxente());
	    mensaje.setContent("NewGame#"+xog1.getId()+","+xog2.getId());
	    send(mensaje);
    }
    
    /**
     * Mensaxe de REQUEST (Position) para solicitar a un xogador a posición da matriz.
     * @param xog Xogador para solicitar a posición na matriz
     * @return Número de fila ou columna seleccionada polo xogador
     */
    public int escollaXog(psi29_DatosXogador xog) {
    	
    	// Creación do mensaxe
	    ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
	    mensaje.setSender(getAID());
	    mensaje.addReceiver(xog.getAxente());
	    mensaje.setContent("Position");
	    send(mensaje);
	    
	    //Espera recivir a resposta nun mensaxe coa posición.
	    ACLMessage resposta=null;
	    
	    while(resposta==null || !resposta.getContent().contains("Position#")) {
	    	resposta =  this.blockingReceive();
	    }

	   	return Integer.parseInt(resposta.getContent().split("#")[1]);
    }
    
    /**
     * Envia unha mensaxe os xogadores cos resultados da ronda anterior.
     * @param xog1 Xogador co menor id
     * @param xog2 Xogador co maior id
     * @param escollaXog1 Fila escollida polo xogador 1
     * @param escollaXog2 Columna escollida polo xogador 2
     * @param payoff1 Payoff da ronda anterior do xogador 1
     * @param payoff2 Payoff da ronda anterior do xogador 2
     */
    public void informarResultados(psi29_DatosXogador xog1, psi29_DatosXogador xog2, int escollaXog1, int escollaXog2, int payoff1, int payoff2) {
    	
    	// Creación do mensaxe
	    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
	    mensaje.setSender(getAID());
	    mensaje.addReceiver(xog1.getAxente());
	    mensaje.addReceiver(xog2.getAxente());
	    mensaje.setContent("Results#"+escollaXog1+","+escollaXog2+"#"+payoff1+","+payoff2);
	    send(mensaje);
	    
    }
    
    /**
     * Envia unha mensaxe os xogadores da partida informando do fin da mesma (EndGame)
     * @param xog1 Xogador co menor id
     * @param xog2 Xogador co maior id
     */
    public void finPartida(psi29_DatosXogador xog1, psi29_DatosXogador xog2) {
    	
    	// Creación do mensaxe
	    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
	    mensaje.setSender(getAID());
	    mensaje.addReceiver(xog1.getAxente());
	    mensaje.addReceiver(xog2.getAxente());
	    mensaje.setContent("EndGame");
	    send(mensaje);
    }
    
    /**
     * Envia unha mensaxe informando que se vai renovar a matriz en un porcentaxe
     * @param xog1 Xogador co menor id
     * @param xog2 Xogador co maior id
     * @param porCamMatriz Porcentaxe de cambio da matriz.
     */
    public void menCambiaMatriz(psi29_DatosXogador xog1, psi29_DatosXogador xog2, int porCamMatriz) {
    	
    	// Creación do mensaxe
	    ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
	    mensaje.setSender(getAID());
	    mensaje.addReceiver(xog1.getAxente());
	    mensaje.addReceiver(xog2.getAxente());
	    mensaje.setContent("Changed#"+porCamMatriz);
	    send(mensaje);
    }
	
    /**
     * Función que inicia o axente. Engade un Behaviour par buscar os player de forma cíclica.
     */
    protected void setup()
    {
        addBehaviour(new buscarPlayers());
    }
	
	
	
}
