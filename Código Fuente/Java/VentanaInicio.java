package TFG_Ejecutable;

import javax.swing.JFrame;

public class VentanaInicio {
	JFrame frame;
	public VentanaInicio (){
		frame = new JFrame("Iniciando...");
		frame.setSize(900, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	protected void metodoInvisibilizarVentanaAuxiliar(){
		frame.setVisible(false);
	}
}
