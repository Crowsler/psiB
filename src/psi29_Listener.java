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
 * @version 0.2
 *
 */
public class psi29_Listener implements ActionListener, WindowListener, ItemListener {

	psi29_Xanela x;
	psi29_Xogo xogo;
	
	/**
	 * Constructor do listener
	 * @param x: Ventá principal.
	 * @param xogo: Xogo.
	 */
	public psi29_Listener(psi29_Xanela x, psi29_Xogo xogo)
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
		 		String mensaje="<html><body color='white'><h1><center>"+psi29_Xanela.nomeAPP+"</center></h1><h4 align='right'>Version "+psi29_Xanela.versionAPP+"</h4><br>"
		 				+ "<center>Author: "+psi29_Xanela.autorAPP+" <span color='green'>&nbsp;&nbsp;"+psi29_Xanela.mailAutorAPP+"</span></center><br>"
		 				+ "<center>©  2016 &nbsp;&nbsp;&nbsp;&nbsp;Compuglobalhypermeganet, S.L.</center><br><br></body></html>";
		 		JOptionPane.showMessageDialog(x, mensaje, "About", JOptionPane.DEFAULT_OPTION);
		 	break;
		 	case "new":
		 		if(xogo.xogoIniciado) break;
		 		xogo.iniciarXogo();
		 	break;
		 	//En caso de stop cambiamos o listener e o label do botón a Start
		 	case "stop":
		 		if(!xogo.xogoIniciado) break;
		 		xogo.pause(true);
		 	break;
		 	//En caso de start cambiamos o listener e o label do botón a Stop
		 	case "start":
		 		if(!xogo.xogoIniciado) break;
		 		xogo.resume(true);
		 	break;
		 	//No resto dos casos chamamos a función do xogo correspodente a cada listener.
		 	case "rnPlayer":
		 		if(xogo.xogoIniciado) break;
		 		xogo.renameXogadoresSelect();
		 	break;
		 	case "rsPlayer":
		 		if(xogo.xogoIniciado) break;
		 		xogo.resetXogadoresSelect();
			break;
		 	case "rsAllPlayer":
		 		if(xogo.xogoIniciado) break;
		 		xogo.resetAllXogadores();
			break;
		 	case "rmPlayer":
		 		if(xogo.xogoIniciado) break;
		 		xogo.delXogadoresSelect();
		 	break;
		 	case "setTamMatriz":
		 		if(xogo.xogoIniciado) break;
		 		xogo.resizeMatriz();
		 	break;
		 	case "setRondas":
		 		if(xogo.xogoIniciado) break;
		 		xogo.setRondas();
		 	break;
		 	case "setRondasMatriz":
		 		if(xogo.xogoIniciado) break;
		 		xogo.setRondasMatriz();
		 	break;
		 	case "setPorcentajeMatriz":
		 		if(xogo.xogoIniciado) break;
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
	}//itemStateChanged
	
}//Clase Listener
