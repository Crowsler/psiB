import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
public class psi29_Arbitro extends Agent {

	
	class EmisorComportaminento extends SimpleBehaviour
    {
        boolean fin = false;
        public void action()
        {
            System.out.println(getLocalName() +": Preparandose para enviar un mensaje a receptor");
            AID id = new AID();
            id.setLocalName("receptor");
 
        // Creación del objeto ACLMessage
            ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
 
        //Rellenar los campos necesarios del mensaje
            mensaje.setSender(getAID());
            mensaje.setLanguage("Español");
            mensaje.addReceiver(id);
            mensaje.setContent("Hola, que tal receptor ?");
 
        //Envia el mensaje a los destinatarios
            send(mensaje);
 
            System.out.println(getLocalName() +": Enviando hola a receptor");
            System.out.println(mensaje.toString());
            fin = true;
        }
 
        public boolean done()
        {
            return fin;
        }
    }
    protected void setup()
    {
        addBehaviour(new EmisorComportaminento());
    }
	
	
	
}
