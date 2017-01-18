

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.UIManager;


/**
 * 
 * Ventá principal do xogo, onde podemos ver e modificar os parámetros do mesmo.
 * Tamén contén funcións para mostrar diferentes diálogos para confirmación e inserción de parámetros.
 * 
 * @author Bruno Nogareda Da Cruz, brunonogareda@gmail.com
 * @version 0.2
 *
 */
@SuppressWarnings("serial")
public class psi29_Xanela extends Frame {
	
	//Datos da aplicación
	public final static String nomeAPP="NeoMatrix";
	public final static String autorAPP="Bruno Nogareda Da Cruz";
	public final static String mailAutorAPP="brunonogareda@gmail.com";
	public final static String versionAPP="0.2";
	
	//Parámetros fixos da venta
	private final static int xanelaAncho = 950;
	private final static int xanelaLongo = 600;
	
	//Texto para algúns labels.
	public final static String TextNRondasXog = "Nº de Rondas xogadas: ";
	public final static String TextNXogadores = "Nº de xogadores: ";
	public final static String TextNRondas = "Nº de rondas por partida: ";
	public final static String TextNPartidas = "Nº de partidas: ";
	public final static String TextNCamMatriz = "Nº de rondas cambio matriz: ";
	public final static String TextPCamMatriz = "Cambio matriz: ";
	public final static Color ColorTablaPVP = Color.DARK_GRAY;
	
	private psi29_Listener escoita; //Obxecto listener
	
	//Obxectos da ventá que serán modificados dende outras clases.
	public List ListaXog;
	public Button ButNovo, ButStartStop, ButLog;
	public Label EtiqNRondasXog, EtiqNXogadores, EtiqNRondas, EtiqNPartidas, EtiqNCamMatriz, EtiqPCamMatriz;
	public Label xog1_pvp, xog2_pvp, xog1_g_pvp, xog1_p_pvp, xog1_pag_pvp, xog2_g_pvp, xog2_p_pvp, xog2_pag_pvp;
	public TextArea taMatriz;
	public TextArea taLog;
	public MenuItem MenStartStop;
	public psi29_Xogo xogo;
	public psi29_MainAg arbitro;
	
	/**
	 * Constructor principal da clase
	 * Crea a xanela cos botóns, parámetros, etc.
	 */
	public psi29_Xanela(psi29_MainAg arbitro)
	 {
		super(nomeAPP);
		
		this.arbitro = arbitro;
		
		//Inicializamos o xogo. (Parámetros iniciais do xogo e lóxica do mesmo)
		xogo = new psi29_Xogo(this, arbitro);
		
		//Iniciamos o listener.
		escoita = new psi29_Listener(this, xogo);
		addWindowListener(escoita);
				
		
		
		/*------------------------------------------------------
		   --------------------   Barra de Menú   --------------
		   -----------------------------------------------------*/
		MenuBar oMB = new MenuBar();
		
		//Menu Ficheiro
		Menu oMenu = new Menu("Ficheiro");
		MenuItem oMI;
		oMI = new MenuItem ("Sair", new MenuShortcut('X'));
		oMI.setActionCommand("exit");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		oMB.add (oMenu);

		//Menu de edición de xogadores
		oMenu = new Menu("Xogadores");
		oMI = new MenuItem ("Renomear");
		oMI.setActionCommand("rnPlayer");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
	 	oMI = new MenuItem ("Reiniciar");
	 	oMI.setActionCommand("rsPlayer");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		oMI = new MenuItem ("Reiniciar Todos");
	 	oMI.setActionCommand("rsAllPlayer");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		oMI = new MenuItem ("Borrar");
		oMI.setActionCommand("rmPlayer");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		oMB.add (oMenu);

		//Menú de execución de xogo
		oMenu = new Menu("Run");
		oMI = new MenuItem ("Novo Xogo", new MenuShortcut('N'));
		oMI.setActionCommand("new");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		MenStartStop = new MenuItem ("Start/Stop", new MenuShortcut('S'));
		MenStartStop.setActionCommand("start");
		MenStartStop.addActionListener (escoita);
		oMenu.add(MenStartStop);
		oMB.add(oMenu);
		
		//Menú de Edición do xogo
		oMenu = new Menu("Editar Xogo");
		oMI = new MenuItem ("Número Filas/Columnas");
		oMI.setActionCommand("setTamMatriz");
		oMI.addActionListener(escoita);
	  	oMenu.add(oMI);
	  	oMI = new MenuItem ("Número Rondas");
		oMI.setActionCommand("setRondas");
		oMI.addActionListener(escoita);
	  	oMenu.add(oMI);
	  	oMI = new MenuItem ("Rondas cambio Matriz");
		oMI.setActionCommand("setRondasMatriz");
		oMI.addActionListener(escoita);
	  	oMenu.add(oMI);
	  	oMI = new MenuItem ("Porcentaje cambio Matriz");
		oMI.setActionCommand("setPorcentajeMatriz");
		oMI.addActionListener(escoita);
	  	oMenu.add(oMI);
		oMB.add(oMenu);

		//Menu de Xanela
		oMenu = new Menu("Xanela");
		CheckboxMenuItem oCBMI = new CheckboxMenuItem ("Comentarios On/Off", xogo.comentarios);
		oCBMI.addItemListener(escoita);
	  	oMenu.add(oCBMI);
		oCBMI = new CheckboxMenuItem ("Comentarios Curtos/Longos", xogo.comentarios_long);
		oCBMI.addItemListener(escoita);
	  	oMenu.add(oCBMI);
		oMB.add (oMenu);
		oMI = new MenuItem ("Limpar Log", new MenuShortcut('C'));
		oMI.setActionCommand("cleanLog");
		oMI.addActionListener(escoita);
	  	oMenu.add(oMI);
		oMB.add(oMenu);

		
		//Menu de axuda
		oMenu = new Menu("Axuda");
		oMI = new MenuItem ("About");
		oMI.setActionCommand("about");
		oMI.addActionListener (escoita);
		oMenu.add(oMI);
		oMB.add (oMenu);
		oMB.setHelpMenu (oMenu);
		setMenuBar(oMB);

		
		//FIN DA BARRA DE MENÚS
		
		
		//Estructura de cadros da ventá
		setLayout(new GridBagLayout());
		GridBagConstraints gBC;
		
		
		
		/*------------------------------------------------------
		   ----------------- Botoneras superiores --------------
		   -----------------------------------------------------*/
		Panel oPanel = new Panel();
		oPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,0));
	
		//Botón de Novo
		ButNovo = new Button ("Novo Xogo");
		ButNovo.setActionCommand("new");
		ButNovo.setBackground(Color.ORANGE);
		ButNovo.setPreferredSize(new Dimension(120, 25));
		ButNovo.addActionListener (escoita);
		oPanel.add (ButNovo);
		
		//Botón de stop
		ButStartStop = new Button("Start");
		ButStartStop.setActionCommand("start");
		ButStartStop.setBackground(Color.ORANGE);
		ButStartStop.setPreferredSize(new Dimension(120, 25));
		ButStartStop.addActionListener (escoita);
		oPanel.add (ButStartStop);
		
		//Botón de borrado de comentarios
		ButLog = new Button("Limpar Log");
		ButLog.setActionCommand("cleanLog");
		ButLog.setBackground(Color.ORANGE);
		ButLog.setPreferredSize(new Dimension(120, 25));
		ButLog.addActionListener (escoita);
		oPanel.add (ButLog);

		//Parámetros para a colocación do panel das botoneiras.
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		gBC.gridx = 0;
		gBC.gridy = 0;
		gBC.weightx = 1;
		gBC.weighty = 0;
		gBC.gridheight = 1;
		gBC.gridwidth = 2;
		gBC.insets = new Insets (10,50,5,50);
		add(oPanel, gBC);
		
		
		
		/*------------------------------------------------------
		   ----------------- Listado de xogadores --------------
		   -----------------------------------------------------*/
		oPanel = new Panel();
		
		//Label co título das columnas cos datos dos xogadores
		Label l = new Label();
		l.setText("        Tipo               Nome               ID          Gañadas    Perdidas      Payoff        ");
		l.setForeground(Color.WHITE);
		
		//Lista cos xogadores
		ListaXog = new List ();
		ListaXog.setFont(new Font("Monospaced", Font.PLAIN, 12));
//		for(int i=0; i<xogo.estadisticas.size(); i++) {
//			xogo.estadisticas.get(i).setPosInList(i);
//			ListaXog.add(xogo.estadisticas.get(i).toString());
//		}
		ListaXog.setMultipleMode(true);
		ListaXog.setForeground(Color.BLACK);
		
		oPanel.setLayout(new BorderLayout());
		oPanel.add(l, BorderLayout.PAGE_START);
		oPanel.add(ListaXog, BorderLayout.CENTER);
		
		//Parámetros para a colocación da lista de xogadores
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.BOTH;
		gBC.gridx = 0;
		gBC.gridy = 1;
		gBC.weightx = 0;
		gBC.weighty = 1;
		gBC.gridheight = 1;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (5,20,5,10);
		
		add(oPanel, gBC);
		
		
		
		
		/*------------------------------------------------------
		   -----------------------   PVP   ---------------------
		   -----------------------------------------------------*/
		oPanel = new Panel();
		oPanel.setLayout(new GridBagLayout());
		
		//Título PVP
		Label titulo_pvp = new Label("Estadisticas da partida actual (Rondas)");
		titulo_pvp.setAlignment(Label.CENTER);
		titulo_pvp.setBackground(ColorTablaPVP);
		titulo_pvp.setPreferredSize(new Dimension(605,20));		
		
		//Parámetros para a colocación do titulo pvp
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		gBC.gridx = 0;
		gBC.gridy = 0;
		gBC.weightx = 1;
		gBC.weighty = 0;
		gBC.gridheight = 1;
		gBC.gridwidth = 4;
		gBC.insets = new Insets (2,2,1,2);
		oPanel.add(titulo_pvp, gBC);
		
		
		//Nome Xogador 1
		xog1_pvp = new Label("-----------");
		xog1_pvp.setAlignment(Label.CENTER);
		xog1_pvp.setBackground(ColorTablaPVP);
		
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (1,2,1,1);
		oPanel.add(xog1_pvp, gBC);
	
		
		//Nome Xogador 2
		xog2_pvp = new Label("-----------");
		xog2_pvp.setAlignment(Label.CENTER);
		xog2_pvp.setBackground(ColorTablaPVP);
		
		gBC.gridx = 0;
		gBC.gridy = 3;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (1,2,2,1);
		oPanel.add(xog2_pvp, gBC);
		

		//Titulos
		l = new Label("");
		l.setBackground(ColorTablaPVP);
		gBC.gridx = 0;
		gBC.gridy = 1;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (1,2,1,1);
		oPanel.add(l, gBC);
		
		l = new Label("Gañadas");
		l.setAlignment(Label.CENTER);
		l.setBackground(ColorTablaPVP);
		gBC.gridx = 1;
		gBC.gridy = 1;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(l, gBC);
		
		l = new Label("Perdidas");
		l.setAlignment(Label.CENTER);
		l.setBackground(ColorTablaPVP);
		gBC.gridx = 2;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(l, gBC);

		l = new Label("Pagado");
		l.setAlignment(Label.CENTER);
		l.setBackground(ColorTablaPVP);
		gBC.gridx = 3;
		gBC.insets = new Insets (1,1,1,2);
		oPanel.add(l, gBC);
		
		//Datos xogadores
		xog1_g_pvp = new Label("0");
		xog1_g_pvp.setAlignment(Label.CENTER);
		xog1_g_pvp.setBackground(ColorTablaPVP);
		gBC.gridx = 1;
		gBC.gridy = 2;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(xog1_g_pvp, gBC);
		
		xog1_p_pvp = new Label("0");
		xog1_p_pvp.setAlignment(Label.CENTER);
		xog1_p_pvp.setBackground(ColorTablaPVP);
		gBC.gridx = 2;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(xog1_p_pvp, gBC);

		xog1_pag_pvp = new Label("0");
		xog1_pag_pvp.setAlignment(Label.CENTER);
		xog1_pag_pvp.setBackground(ColorTablaPVP);
		gBC.gridx = 3;
		gBC.insets = new Insets (1,1,1,2);
		oPanel.add(xog1_pag_pvp, gBC);
		
		xog2_g_pvp = new Label("0");
		xog2_g_pvp.setAlignment(Label.CENTER);
		xog2_g_pvp.setBackground(ColorTablaPVP);
		gBC.gridx = 1;
		gBC.gridy = 3;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(xog2_g_pvp, gBC);
		
		xog2_p_pvp = new Label("0");
		xog2_p_pvp.setAlignment(Label.CENTER);
		xog2_p_pvp.setBackground(ColorTablaPVP);		
		gBC.gridx = 2;
		gBC.insets = new Insets (1,1,1,1);
		oPanel.add(xog2_p_pvp, gBC);

		xog2_pag_pvp = new Label("0");
		xog2_pag_pvp.setAlignment(Label.CENTER);
		xog2_pag_pvp.setBackground(ColorTablaPVP);
		gBC.gridx = 3;
		gBC.insets = new Insets (1,1,2,2);
		oPanel.add(xog2_pag_pvp, gBC);
		
		
		oPanel.setForeground(Color.WHITE);
		oPanel.setBackground(Color.ORANGE);
		
		//Parámetros para a colocación do panel de datos
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.NONE;
		gBC.gridx = 0;
		gBC.gridy = 2;
		gBC.weightx = 0;
		gBC.weighty = 0;
		gBC.gridheight = 1;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (5,20,10,10);
		add(oPanel, gBC);
		
		
		

		
		/*------------------------------------------------------
		   ------------------------ Matriz ---------------------
		   -----------------------------------------------------*/
		oPanel = new Panel();
		l = new Label("Matriz de xogo");
		l.setForeground(Color.WHITE);
		l.setAlignment(Label.CENTER);
		
		taMatriz = new TextArea(10,10);
		taMatriz.setEditable(false);
		xogo.cambiarMatriz(100);
		taMatriz.setBackground(Color.WHITE);
		
		//Parámetros para a colocación da matriz
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.BOTH;
		gBC.gridx = 1;
		gBC.gridy = 1;
		gBC.weightx = 1;
		gBC.weighty = 1;
		gBC.gridheight = 2;
		gBC.gridwidth = 1;
		gBC.insets = new Insets (4,10,10,20);
		
		oPanel.setLayout(new BorderLayout());
		oPanel.add(l, BorderLayout.PAGE_START);
		oPanel.add(taMatriz, BorderLayout.CENTER);
		
		add(oPanel, gBC);

		
		/*------------------------------------------------------
		   -------------------- Panel e datos ------------------
		   -----------------------------------------------------*/
		oPanel = new Panel();
		oPanel.setLayout(new GridLayout(2,3));
		
		EtiqNRondasXog = new Label(TextNRondasXog+xogo.numRondasXog);
		oPanel.add(EtiqNRondasXog);
		EtiqNXogadores = new Label(TextNXogadores+xogo.xogadores.size());
		oPanel.add(EtiqNXogadores);
		EtiqNRondas = new Label(TextNRondas+xogo.numRondas);
		oPanel.add(EtiqNRondas);
		EtiqNPartidas = new Label(TextNPartidas+psi29_Xogo.coefbin(xogo.xogadores.size(), 2));
		oPanel.add(EtiqNPartidas);
		EtiqPCamMatriz = new Label(TextPCamMatriz+xogo.porCambMatriz+"%");
		oPanel.add(EtiqPCamMatriz);
		EtiqNCamMatriz = new Label(TextNCamMatriz+xogo.numCambMatriz);
		oPanel.add(EtiqNCamMatriz);
		
		
		oPanel.setForeground(Color.WHITE);
		oPanel.setBackground(Color.DARK_GRAY);
		
		//Parámetros para a colocación do panel de datos
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;
		gBC.gridx = 0;
		gBC.gridy = 3;
		gBC.weightx = 1;
		gBC.weighty = 0.1;
		gBC.gridheight = 1;
		gBC.gridwidth = 2;
		gBC.insets = new Insets (5,20,5,20);
		add(oPanel, gBC);
		
		
		/*------------------------------------------------------
		   ------------------------- Log -----------------------
		   -----------------------------------------------------*/
		
		taLog = new TextArea ("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		taLog.setEditable(false);
		taLog.setBackground(Color.white);
		taLog.setFont(new Font("", Font.PLAIN, 13));
		
		//Parámetros para a colocación do log
		gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.BOTH;
		gBC.gridx = 0;
		gBC.gridy = 4;
		gBC.weightx = 1;
		gBC.weighty = 0.4;
		gBC.gridheight = 1;
		gBC.gridwidth = 2;
		gBC.insets = new Insets(10,20,5,20);
		add(taLog, gBC);
		

		
		/*------------------------------------------------------
		   ----------- Modificación da ventá -------------------
		   -----------------------------------------------------*/
		
		//Cor de fondo da venta e texto
		setBackground(Color.DARK_GRAY);
		setFont (new Font ("System", Font.BOLD, 14));
		
		//Dimensións da ventá
		setSize(new Dimension(xanelaAncho, xanelaLongo));
		setResizable(true);
		
		//Accedemos as resolución do monitor
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Localización da ventá
		setLocation (new Point ((screenSize.width-xanelaAncho)/2, (screenSize.height-xanelaLongo)/2));
		
		//Facemos que aparezca a ventá na pantalla
		this.setVisible(true);
		
		
		//Poñemos a mesma cor no Jpanel de swing que na ventá principal.
 		UIManager.put("OptionPane.background", Color.DARK_GRAY);
 		UIManager.put("Panel.background", Color.DARK_GRAY);
 		
	 }//main
	
	/**
	 * Mostra unha ventá con un mensaxe de erro por pantalla.
	 * @param msx: mensaxe que se quere mostrar
	 */
	public void mostrarMensaxeError(String msx)
	 {
		JOptionPane.showMessageDialog(this, "<html><body color='white'>"+msx+"</body></html>", "Error", JOptionPane.ERROR_MESSAGE);
	 }//mostrarMensaxeError
	
	public void mostrarMensaxeInfo(String msx, String titulo)
	 {
		JOptionPane.showMessageDialog(this, "<html><body color='white'>"+msx+"</body></html>", titulo, JOptionPane.INFORMATION_MESSAGE);
	 }//mostrarMensaxeError
	
	/**
	 * Mostra unha ventá con un mensaxe e un textBox, onde se pode escribir.
	 * @param msx: mensaxe que se quere mostrar
	 * @param titulo: título da ventá que se mostrará
	 * @param valor_ant: Valor anterior do dato que se quere solicitar (O que se mostra por defecto no textBox)
	 * @return Retorna unha cadena de texto co que se escribiu no textBox
	 */
	
	public String getValor(String msx, String titulo, String valor_ant)
	 {
		return (String)JOptionPane.showInputDialog(this, "<html><body color='white'>"+msx+"</body></html>", titulo, JOptionPane.DEFAULT_OPTION, null, null, valor_ant);
	 }//getValor
	
	
	/**
	 * Mostra unha ventá con unha mesaxe de confirmación.
	 * @param msx: mensaxe que se quere mostrar
	 * @param titulo: título da ventá que se mostrará
	 * @return Retorna un número según a opción que foi seleccionada.
	 */
	public int mostrarConfirmacion(String msx, String titulo)
	 {
		return JOptionPane.showConfirmDialog(this, "<html><body color='white'>"+msx+"</body></html>", titulo, JOptionPane.YES_NO_CANCEL_OPTION);
	 }//mostrarConfirmacion
	
	
}//Clase Xanela