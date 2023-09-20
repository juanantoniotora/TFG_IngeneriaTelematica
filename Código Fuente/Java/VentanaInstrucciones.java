package TFG_Ejecutable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import TFG_Ejecutable.Ventana.ClasePanelNoticias;


/**Esta clase se encarga de colocar una 
 * imagen readactable al tamaño con las 
 * instrucciones...
 */

public class VentanaInstrucciones extends JFrame{

	public VentanaInstrucciones() throws FileNotFoundException, InterruptedException, IOException{
		super("Ventana de Instrucciones");
		add(new ClaseImagen());
		setSize(300,300);
		setVisible(true);
		
	}
	
	public class ClaseImagen extends JPanel{ 
	
		public void paintComponent(Graphics g){
			Dimension tamaño = getSize();
			
			ImageIcon imagen = new ImageIcon(new ImageIcon(getClass().getResource("/Captura_instrucciones.png")).getImage());
			g.drawImage(imagen.getImage(), 0, 0, tamaño.width, tamaño.height, this);
		}
	}
	
//	public static void main (String[] args) throws FileNotFoundException, InterruptedException, IOException{
//		VentanaInstrucciones objeto= new VentanaInstrucciones();
//	}
//	
	
}
