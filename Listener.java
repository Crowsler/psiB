import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;



/**
 * 
 * Clase onde se localizan as diferentes funcións para "escoitar" as accions realizadas na ventá principal.
 * Algunha das funcións simples tamén se almacenan en esta clase, como a ventá de about.
 * Outras funcións mais própias do xogo chamarán a funcións da clase Xogo.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.1
 *
 */
public class Listener implements ActionListener, WindowListener, ItemListener {

	Xanela x;
	Xogo xogo;
	
	/**
	 * Constructor do listener
	 * @param x: Ventá principal.
	 * @param xogo: Xogo.
	 */
	public Listener(Xanela x, Xogo xogo)
	 {
		this.x=x;
		this.xogo=xogo;
	 }//Listener
	
	
	// Métodos da interfaz WindowsListener
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	/**
	 * Método que pecha a ventá cando se pulsa na "X"
	 */
	public void windowClosing(WindowEvent e)
	 {
		x.dispose(); //Pecha a ventá
		System.exit(0);
	 }

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand())
		 {
			//En caso de Sair chamamos a función correspondente.
		 	case "exit":
		 		windowClosing(null);
		 	break;
		 		
		 	//En caso de about utilizamos unha ventá de java swing para mostrar os datos da aplicación e o autor.
		 	case "about":
		 		String mensaje="<html><body color='white'><h1><center>"+GUI.nomeAPP+"</center></h1><h4 align='right'>Version "+GUI.versionAPP+"</h4><br>"
		 				+ "<center>Author: "+GUI.autorAPP+" <span color='green'>&nbsp;&nbsp;"+GUI.mailAutorAPP+"</span></center><br>"
		 				+ "<center>©  2016 &nbsp;&nbsp;&nbsp;&nbsp;Compuglobalhypermeganet, S.L.</center><br><br></body></html>";
		 		JOptionPane.showMessageDialog(x, mensaje, "About", JOptionPane.DEFAULT_OPTION);
		 	break;
		 	case "new":
		 		xogo.engadirLog("NOVO XOGO", 1);
		 	break;
		 	//En caso de stop cambiamos o listener e o label do botón a Start
		 	case "stop":
		 		x.ButStartStop.setLabel("Start");
		 		x.ButStartStop.setActionCommand("start");
		 		x.MenStartStop.setActionCommand("start");
		 		xogo.engadirLog("STOP", 1);
		 	break;
		 	//En caso de start cambiamos o listener e o label do botón a Stop
		 	case "start":
		 		x.ButStartStop.setLabel("Stop");
		 		x.ButStartStop.setActionCommand("stop");
		 		x.MenStartStop.setActionCommand("stop");
		 		xogo.engadirLog("START", 1);
		 	break;
		 	//No resto dos casos chamamos a función do xogo correspodente a cada listener.
		 	case "rnPlayer":
		 		xogo.renameXogadoresSelect();
		 	break;
		 	case "rsPlayer":
		 		xogo.resetXogadoresSelect();
			break;
		 	case "rmPlayer":
		 		xogo.delXogadoresSelect();
		 	break;
		 	case "setTamMatriz":
		 		xogo.resizeMatriz();
		 	break;
		 	case "setRondas":
		 		xogo.setRondas();
		 	break;
		 	case "setRondasMatriz":
		 		xogo.setRondasMatriz();
		 	break;
		 	case "setPorcentajeMatriz":
	 			xogo.setPorcentajeMatriz();
		 	break;
		 	case "cleanLog":
		 		x.taLog.setText("");
		 	break;
		 	//En outro caso mostramos que se está executando unha función que está por implementar.
		 	default:
		 		x.mostrarMensaxeError("Esta función todavia está por implementar");
		 	break;
		 }
	}//actionPerformed
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		switch(e.getItem().toString())
		 {
			//Cambia o estado do boolean dos comentarios
			case "Comentarios On/Off":
				if(e.getStateChange()==1)
					xogo.comentarios=true;
				else
					xogo.comentarios=false;
			break;
			case "Comentarios Curtos/Longos":
				if(e.getStateChange()==1)
					xogo.comentarios_long=true;
				else
					xogo.comentarios_long=false;
			break;
			default:
			break;
		 }
	}
	
}//Clase Listener
