package TFG_Ejecutable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class VentanaRutaSincronizacion extends JFrame implements ActionListener {

    private JLabel texto;           // etiqueta o texto no editable
    private JTextField caja;        // caja de texto, para insertar datos
    private JButton boton;          // boton con una determinada accion
    String nombre;
    
    public VentanaRutaSincronizacion() {
        super("Seleccione Ruta de Sincronización");                    // usamos el contructor de la clase padre JFrame
        configurarVentana();        // configuramos la ventana
        inicializarComponentes();   // inicializamos los atributos o componentes
    }

    private void configurarVentana() {
        this.setTitle("Ventana de Seleccion de Ruta Sincronizacion MT4");                   // colocamos titulo a la ventana
        this.setSize(450, 210);                                 // colocamos tamanio a la ventana (ancho, alto)
        this.setLocationRelativeTo(null);                       // centramos la ventana en la pantalla
        this.setLayout(null);                                   // no usamos ningun layout, solo asi podremos dar posiciones a los componentes
        this.setResizable(false);                               // hacemos que la ventana no sea redimiensionable
    }

    private void inicializarComponentes() {
        // creamos los componentes
        texto = new JLabel();
        caja = new JTextField();
        boton = new JButton();
        // configuramos los componentes
        texto.setText("Inserte Ruta");    // colocamos un texto a la etiqueta
        texto.setBounds(50, 50, 100, 25);   // colocamos posicion y tamanio al texto (x, y, ancho, alto)
        caja.setBounds(150, 50, 100, 25);   // colocamos posicion y tamanio a la caja (x, y, ancho, alto)
        boton.setText("Guardar Ruta");   // colocamos un texto al boton
        boton.setBounds(50, 100, 200, 30);  // colocamos posicion y tamanio al boton (x, y, ancho, alto)
        boton.addActionListener(this);      // hacemos que el boton tenga una accion y esa accion estara en esta clase
        // adicionamos los componentes a la ventana
        this.add(texto);
        this.add(caja);
        this.add(boton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        nombre = caja.getText();                                 // obtenemos el contenido de la caja de texto
        JOptionPane.showMessageDialog(this, "Ruta Guardada ");    // mostramos un mensaje (frame, mensaje)
    }

    public String metodoRutaSincronizacion(){
    	String texto = nombre;
    	String subTexto= "";
    	String caracterBuscado = "\\" ;	//busca "\"
    	int indiceActual=0;
    	
    	return texto;
    }
    public static void metodoPrincipal() {
        VentanaRutaSincronizacion V = new VentanaRutaSincronizacion();      // creamos una ventana
        V.setVisible(true);             // hacemos visible la ventana creada
    }
    
    public static void main(String args[]) {
        VentanaRutaSincronizacion V = new VentanaRutaSincronizacion();      // creamos una ventana
        V.setVisible(true);             // hacemos visible la ventana creada
    }
}