package TFG_Ejecutable;
	
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
	
public class Ventana extends JFrame{
	
/***************** ELEMENTOS DE LA GUI:	********************************************************************/
	
	// BARRA SUPERIOR
	JMenuBar barraSuperior;
	JButton botonInicio, botonStop,
			botonRutaMT4, botonInstruccionesAPP;
	
	
	// PANEL CENTRAL DE NOTICIAS
	boolean variableControlHTML=true;	//Por defecto leo HTML
	ClasePanelNoticias objetoPanelNoticias;
	JLabel 	etiquetaPresentacion, etiquetaNoticia1, etiquetaNoticia2, etiquetaNoticia3, etiquetaNoticia4, etiquetaNoticia5, etiquetaNoticia6,
			etiquetaNoticia7, etiquetaNoticia8, etiquetaNoticia9, etiquetaNoticia10, etiquetaNoticia11, etiquetaNoticia12, etiquetaNoticia13,
			etiquetaNoticia14, etiquetaNoticia15, etiquetaNoticia16, etiquetaNoticia17, etiquetaNoticia18, etiquetaNoticia19,
			etiquetaNoticia20, etiquetaNoticia21, etiquetaNoticia22, etiquetaNoticia23, etiquetaNoticia24, etiquetaNoticia25;
	String 	sEtiquetaPresentacion, sEtiquetaNoticia1, sEtiquetaNoticia2, sEtiquetaNoticia3, sEtiquetaNoticia4, sEtiquetaNoticia5, sEtiquetaNoticia6,
			sEtiquetaNoticia7, sEtiquetaNoticia8, sEtiquetaNoticia9, sEtiquetaNoticia10, sEtiquetaNoticia11, sEtiquetaNoticia12, sEtiquetaNoticia13,
			sEtiquetaNoticia14, sEtiquetaNoticia15, sEtiquetaNoticia16, sEtiquetaNoticia17, sEtiquetaNoticia18, sEtiquetaNoticia19,
			sEtiquetaNoticia20, sEtiquetaNoticia21, sEtiquetaNoticia22, sEtiquetaNoticia23, sEtiquetaNoticia24, sEtiquetaNoticia25;
	
	// PANEL ESTE
	JLabel etiquetaOn_Off;			//Por defecto lanzo aviso
	String stringEtiquetaOn_Off;
	static boolean ejecucion=false;
	
	JButton botonOn_Off;
	boolean variableControlAvisos=true;
	PanelInformacionEste objetoPanelEste;
	int variableOn_Off_MT4_definitivo=-1;//Si se recibe -1 es OFF (cualquier otro valor entero es ON)
	static DecidirOperaciones objetoDecidirOperaciones; 
	
	
	//RUTA DE SINCRONIZACION ENTRE CLIENTE JAVA Y TERMINAL METATRADER4
	static String RutaSincronizacion = "C:/Users/juan antonio/AppData/Roaming/MetaQuotes/Terminal/93231E64ACB41DD159EEC4E406D1AD88/MQL4/Files/";
	static boolean iniciadoElTrading = false;
	
	
	
/***************** CONSTRUCTOR DE LA CLASE: ******************************************************************/
	public Ventana() throws FileNotFoundException, InterruptedException, IOException{
		super("JobTrade");

		VentanaInicio objeto_ventanaAuxiliar = new VentanaInicio();
		
		//CREAMOS BARRA NORTE, PANEL CENTRAL Y PANEL ESTE
		etiquetaOn_Off = new JLabel("");
		stringEtiquetaOn_Off="";
		construirBarraMenus();
		objetoPanelNoticias = new ClasePanelNoticias();
		objetoPanelEste = new PanelInformacionEste();
		
		//POSICIONAMOS ELEMENTOS EN LA VENTANA  	
	    getContentPane().setLayout(new BorderLayout());
	    getContentPane().add(objetoPanelNoticias);     
	    getContentPane().add(barraSuperior, BorderLayout.NORTH);  
	    getContentPane().add(objetoPanelEste, BorderLayout.EAST);  
	   
	    //COMANDOS RESPECTO A LA VENTANA      
	    setSize (1160, 700);
	    setVisible(true);    
	    setResizable(true);   
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    
	    objeto_ventanaAuxiliar.metodoInvisibilizarVentanaAuxiliar();
	}
	
	public static void main(String [] args) throws FileNotFoundException, InterruptedException, IOException{
		Ventana objeto = new Ventana();
		
		while(true){
			while(iniciadoElTrading == true){
				objetoDecidirOperaciones.metodoPrincipal(RutaSincronizacion, ejecucion);
				Thread.currentThread().sleep(65*1000);	//Los indicadores MT4 escriben cada 60 segundos, 
														//yo cada 65 segundos los EAs desde Java.
			}	
		}	
	}
	
	
/***************** CREAMOS BARRA NORTE: **********************************************************/
	private void construirBarraMenus(){
	  	
	  	botonInicio=new JButton("Inciar Trading");
	  	botonInicio.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//pongo variables a TRUE para ejecutar en el While(){...} del metodo MAIN
					objetoDecidirOperaciones.metodoInciarTrading();
					iniciadoElTrading=true;
		}});

	  	botonStop=new JButton("Stop");
	  	botonStop.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//pongo variables a FALSE para ejecutar en el While(){...} del metodo MAIN
					objetoDecidirOperaciones.metodoStopTrading();
					iniciadoElTrading=false;
		}});
	  	
	  	botonRutaMT4=new JButton("Seleccionar Ruta de Sincronizacion con MT4");
	  	botonRutaMT4.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					VentanaRutaSincronizacion objetoVentanaRutaSincronizacion = new VentanaRutaSincronizacion();
					objetoVentanaRutaSincronizacion.metodoPrincipal();
					String variableAux="";
					while(variableAux==""){
						variableAux=objetoVentanaRutaSincronizacion.metodoRutaSincronizacion();
					}
					RutaSincronizacion=variableAux;	//Guardo nueva Ruta
		}});
	  	
	  	botonInstruccionesAPP=new JButton("Instrucciones de la APP");
	  	botonInstruccionesAPP.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						VentanaInstrucciones ventana2 = new VentanaInstrucciones();
					} 
					catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		}});
	  	
	barraSuperior = new JMenuBar();
	barraSuperior.add(botonInicio);
	barraSuperior.add(botonStop);  	
	barraSuperior.add(botonRutaMT4);
	barraSuperior.add(botonInstruccionesAPP);
}


	
/***************** CREAMOS PANEL CENTRAL: **********************************************************/
	class ClasePanelNoticias extends JPanel{
		
		private static final long serialVersionUI=2L;
		JPanel pInfo;
		
		public void keyPressed(KeyEvent e){										}
		
		//Puntos del juego
		public ClasePanelNoticias() throws FileNotFoundException, InterruptedException, IOException{
			//Le decimos que este contenedor será un contenedor inferior de un "BorderLayout()"
			setLayout( new BorderLayout());
			
			//Obtenemos Noticias Fundamentales Actualizadas
			Parse_HTML objetoNoticiasFundamentales= new Parse_HTML();
			String [] arrayNoticiasFundamentales = objetoNoticiasFundamentales.metodoPrincipal();
			String 	cadenaNoticias="";
			String 	cantidadNoticiasString="";
			int 	cantidadNoticiasInt=0;
			//Vamos  sacar cant. de noticias y las noticias en sí mismas del array devuelto
			for(int i=0; i<2; i++){
				if(i==0){
					cantidadNoticiasString	= 	arrayNoticiasFundamentales[0];
				} 
				else {
					if(i==1){
						cadenaNoticias 		= 	arrayNoticiasFundamentales[1];
					}
				}				
			}
			cantidadNoticiasInt = Integer.parseInt(cantidadNoticiasString);
			
			//CREAMOS EL CONTENEDOR GRIDLAYOUT "GridLaout" de (Nx1) dimnensiones
			int N =cantidadNoticiasInt + 1;
			//pInfo= new JPanel ( new GridLayout(N, 1));
			pInfo= new JPanel ( new GridLayout(26, 1));
			
			//INICIALIZO TODAS LAS ETIQUETAS POSIBLES DE NOTICIAS
			etiquetaNoticia1=new JLabel ("");
			etiquetaNoticia2=new JLabel ("");
			etiquetaNoticia3=new JLabel ("");
			etiquetaNoticia4=new JLabel ("");
			etiquetaNoticia5=new JLabel ("");
			etiquetaNoticia6=new JLabel ("");
			etiquetaNoticia7=new JLabel ("");
			etiquetaNoticia8=new JLabel ("");
			etiquetaNoticia9=new JLabel ("");
			etiquetaNoticia10=new JLabel ("");
			etiquetaNoticia11=new JLabel ("");
			etiquetaNoticia12=new JLabel ("");
			etiquetaNoticia13=new JLabel ("");
			etiquetaNoticia14=new JLabel ("");
			etiquetaNoticia15=new JLabel ("");
			etiquetaNoticia16=new JLabel ("");
			etiquetaNoticia17=new JLabel ("");
			etiquetaNoticia18=new JLabel ("");
			etiquetaNoticia19=new JLabel ("");
			etiquetaNoticia20=new JLabel ("");
			etiquetaNoticia21=new JLabel ("");
			etiquetaNoticia22=new JLabel ("");
			etiquetaNoticia23=new JLabel ("");
			etiquetaNoticia24=new JLabel ("");
			etiquetaNoticia25=new JLabel ("");
			
			//DESERIALIZAR EL CHURRO DE NOTICIAS SEPARADO POR "," EN EL STRING
			String separadoArray[] = cadenaNoticias.split(",");
			int longitudArray = separadoArray.length;
			
			//ESCRITURA DE ETIQUETAS
			for(int i=0; i<longitudArray; i++){
				switch (i){
					case 0:
						sEtiquetaNoticia1=separadoArray[0];
						break;
					case 1:
						sEtiquetaNoticia2=separadoArray[1];
						break;
					case 2:
						sEtiquetaNoticia3=separadoArray[2];
						break;
					case 3:
						sEtiquetaNoticia4=separadoArray[3];
						break;
					case 4:
						sEtiquetaNoticia5=separadoArray[4];
						break;
					case 5:
						sEtiquetaNoticia6=separadoArray[5];
						break;
					case 6:
						sEtiquetaNoticia7=separadoArray[6];
						break;
					case 7:
						sEtiquetaNoticia8=separadoArray[7];
						break;
					case 8:
						sEtiquetaNoticia9=separadoArray[8];
						break;
					case 9:
						sEtiquetaNoticia10=separadoArray[9];
						break;
					case 10:
						sEtiquetaNoticia11=separadoArray[10];
						break;
					case 11:
						sEtiquetaNoticia12=separadoArray[11];
						break;
					case 12:
						sEtiquetaNoticia13=separadoArray[12];
						break;
					case 13:
						sEtiquetaNoticia14=separadoArray[13];
						break;
					case 14:
						sEtiquetaNoticia15=separadoArray[14];
						break;
					case 15:
						sEtiquetaNoticia16=separadoArray[15];
						break;
					case 16:
						sEtiquetaNoticia17=separadoArray[16];
						break;
					case 17:
						sEtiquetaNoticia18=separadoArray[17];
						break;
					case 18:
						sEtiquetaNoticia19=separadoArray[18];
						break;
					case 19:
						sEtiquetaNoticia20=separadoArray[19];
						break;
					case 20:
						sEtiquetaNoticia21=separadoArray[20];
						break;
					case 21:
						sEtiquetaNoticia22=separadoArray[21];
						break;
					case 22:
						sEtiquetaNoticia23=separadoArray[22];
						break;
					case 23:
						sEtiquetaNoticia24=separadoArray[23];
						break;
					case 24:
						sEtiquetaNoticia25=separadoArray[24];
						break;
				}
			}
			
			//VACIADO DE ETIQUETAS RESTANTES
			for(int i=longitudArray; i<25; i++){
				switch (i){
					case 0:
						sEtiquetaNoticia1="";
						break;
					case 1:
						sEtiquetaNoticia2="";
						break;
					case 2:
						sEtiquetaNoticia3="";
						break;
					case 3:
						sEtiquetaNoticia4="";
						break;
					case 4:
						sEtiquetaNoticia5="";
						break;
					case 5:
						sEtiquetaNoticia6="";
						break;
					case 6:
						sEtiquetaNoticia7="";
						break;
					case 7:
						sEtiquetaNoticia8="";
						break;
					case 8:
						sEtiquetaNoticia9="";
						break;
					case 9:
						sEtiquetaNoticia10="";
						break;
					case 10:
						sEtiquetaNoticia11="";
						break;
					case 11:
						sEtiquetaNoticia12="";
						break;
					case 12:
						sEtiquetaNoticia13="";
						break;
					case 13:
						sEtiquetaNoticia14="";
						break;
					case 14:
						sEtiquetaNoticia15="";
						break;
					case 15:
						sEtiquetaNoticia16="";
						break;
					case 16:
						sEtiquetaNoticia17="";
						break;
					case 17:
						sEtiquetaNoticia18="";
						break;
					case 18:
						sEtiquetaNoticia19="";
						break;
					case 19:
						sEtiquetaNoticia20="";
						break;
					case 20:
						sEtiquetaNoticia21="";
						break;
					case 21:
						sEtiquetaNoticia22="";
						break;
					case 22:
						sEtiquetaNoticia23="";
						break;
					case 23:
						sEtiquetaNoticia24="";
						break;
					case 24:
						sEtiquetaNoticia25="";
						break;
				}
			}
			
			//CREAMOS N ETIQUETAS DE NOMBRE DIFERENTE, Y ASIGNAMOS SU TEXTO INDEPENDIENTEMENTE
			//TAMBIEN LAS COLOCAMOS EN EL GridLayout
			for(int i=0; i<N; i++){
				//Escribimos la etiqueta de presentacion
				if(i==0){
					etiquetaPresentacion=new JLabel("LAS NOTICIAS FUNDAMENTALES DE HOY SON: ");
					etiquetaPresentacion.setForeground(Color.BLUE);
					pInfo.add(etiquetaPresentacion);
				}
				else{
					switch (i){
						case 1:
							etiquetaNoticia1.setText(sEtiquetaNoticia1);
							pInfo.add(etiquetaNoticia1);
							break;
						case 2:
							etiquetaNoticia2.setText(sEtiquetaNoticia2);
							pInfo.add(etiquetaNoticia2);
							break;
						case 3:
							etiquetaNoticia3.setText(sEtiquetaNoticia3);
							pInfo.add(etiquetaNoticia3);
							break;
						case 4:
							etiquetaNoticia4.setText(sEtiquetaNoticia4);
							pInfo.add(etiquetaNoticia4);
							break;
						case 5:
							etiquetaNoticia5.setText(sEtiquetaNoticia5);
							pInfo.add(etiquetaNoticia5);
							break;
						case 6:
							etiquetaNoticia6.setText(sEtiquetaNoticia6);
							pInfo.add(etiquetaNoticia6);
							break;
						case 7:
							etiquetaNoticia7.setText(sEtiquetaNoticia7);
							pInfo.add(etiquetaNoticia7);
							break;
						case 8:
							etiquetaNoticia8.setText(sEtiquetaNoticia8);
							pInfo.add(etiquetaNoticia8);
							break;
						case 9:
							etiquetaNoticia9.setText(sEtiquetaNoticia9);
							pInfo.add(etiquetaNoticia9);
							break;
						case 10:
							etiquetaNoticia10.setText(sEtiquetaNoticia10);
							pInfo.add(etiquetaNoticia10);
							break;
						case 11:
							etiquetaNoticia11.setText(sEtiquetaNoticia11);
							pInfo.add(etiquetaNoticia11);
							break;
						case 12:
							pInfo.add(etiquetaNoticia12);
							break;
						case 13:
							etiquetaNoticia13.setText(sEtiquetaNoticia13);
							pInfo.add(etiquetaNoticia13);
							break;
						case 14:
							etiquetaNoticia14.setText(sEtiquetaNoticia14);
							pInfo.add(etiquetaNoticia14);
							break;
						case 15:
							etiquetaNoticia15.setText(sEtiquetaNoticia15);
							pInfo.add(etiquetaNoticia15);
							break;
						case 16:
							etiquetaNoticia16.setText(sEtiquetaNoticia16);
							pInfo.add(etiquetaNoticia16);
							break;
						case 17:
							etiquetaNoticia17.setText(sEtiquetaNoticia17);
							pInfo.add(etiquetaNoticia17);
							break;
						case 18:
							etiquetaNoticia18.setText(sEtiquetaNoticia18);
							pInfo.add(etiquetaNoticia18);
							break;
						case 19:
							etiquetaNoticia19.setText(sEtiquetaNoticia19);
							pInfo.add(etiquetaNoticia19);
							break;
						case 20:
							etiquetaNoticia20.setText(sEtiquetaNoticia20);
							pInfo.add(etiquetaNoticia20);
							break;
						case 21:
							etiquetaNoticia21.setText(sEtiquetaNoticia21);
							pInfo.add(etiquetaNoticia21);
							break;
						case 22:
							etiquetaNoticia22.setText(sEtiquetaNoticia22);
							pInfo.add(etiquetaNoticia22);
							break;
						case 23:
							etiquetaNoticia23.setText(sEtiquetaNoticia23);
							pInfo.add(etiquetaNoticia23);
							break;
						case 24:
							etiquetaNoticia24.setText(sEtiquetaNoticia24);
							pInfo.add(etiquetaNoticia24);
							break;
						case 25:
							etiquetaNoticia25.setText(sEtiquetaNoticia25);
							pInfo.add(etiquetaNoticia25);
							break;
					}//salgo del switch/case
				}//salgo del else
			}//salgo del for
			
			add(pInfo, BorderLayout.CENTER);//pinto el GridLayout 
		}//salgo del constructor de la subclase
	}//salgo de la subclase
	
	

	
	/***************** CREAMOS PANEL ESTE **********************************************************/	
	class PanelInformacionEste extends JPanel{
			
		private static final long serialVersionUI=2L;
		JPanel pInfo;
		
		DecidirOperaciones objetoDecidirOperaciones =new DecidirOperaciones();
		int variableOn_Off_MT4_Auxiliar ;	
			
		public PanelInformacionEste (){
			
			setLayout(new BorderLayout());
			
			// GridLaout
			pInfo= new JPanel(new GridLayout(2,1));
			
			int variableAux=-1;
			
			//Creamos las etiquetas que en esta calse se definen, y se les da nombre.
			variableOn_Off_MT4_Auxiliar=objetoDecidirOperaciones.getOn_Off(RutaSincronizacion);
			if(variableOn_Off_MT4_Auxiliar == -1){
				 String etiquetita="MT4 no conectado correctamente.";
				 etiquetaOn_Off=new JLabel(etiquetita);
				 etiquetaOn_Off.setForeground(Color.red);
				 ejecucion=false;
			}
			 else{
				 String etiquetita="MT4 conectado correctamente.";
				 etiquetaOn_Off=new JLabel(etiquetita);
				 etiquetaOn_Off.setForeground(Color.green);
				 ejecucion=true;
			 }
			
			botonOn_Off= new JButton("Actualizar Conexion con MT4");			
			botonOn_Off.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						variableOn_Off_MT4_Auxiliar=objetoDecidirOperaciones.getOn_Off(RutaSincronizacion);
						if(variableOn_Off_MT4_Auxiliar == -1){
							 etiquetaOn_Off.setText("MT4 no conectado correctamente.");
							 etiquetaOn_Off.setForeground(Color.red);
						 }
						 else{
							 etiquetaOn_Off.setText("MT4 conectado correctamente.");
							 etiquetaOn_Off.setForeground(Color.green);
						 }
						
			}});
		  			
			//Asignamos cada etiqueta a una posicion del contendor de tipo GridLayout.
			pInfo.add(botonOn_Off);
			pInfo.add(etiquetaOn_Off);
			
			//Añadimos el panel "pInfo" al contenedor "PanelControl":
			add(pInfo, BorderLayout.CENTER);
		}
		
	}

	
	
	
	
	/***************** MÉTODOS ACTUADORES DE TECLADO: **********************************************************/	
	public void keyReleased(KeyEvent arg0) {}
	//MOSTRAS TECLA PULSADA
	public void keyTyped(KeyEvent arg0) {
		System.out.println("keyTyped: " + arg0.getKeyCode());
	}
	

	
}
