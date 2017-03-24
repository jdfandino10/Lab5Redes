/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: InterfazExposicionCanina.jaa,v 1.10 2010/03/29 20:47:53 lr.ruiz114 Exp $
 * Universidad de los Andes (Bogot� - Colombia)
 * Departamento de Ingenier�a de Sistemas y Computaci�n
 * Licenciado bajo el esquema Academic Free License version 2.1
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n7_exposicionCanina
 * Autor: Mario S�nchez - 26/08/2005
 * Modificado por: Daniel Romero- 30/06/2006
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package Interfaz;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;

import javax.swing.*;

import mundo.*;

/**
 * Es la clase principal de la interfaz
 */
public class InterfazCLiente extends JFrame implements DownloadListener{
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * La ruta del archivo con la informaci�n de los perros
     */
    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------
    /**
     * Es la ruta hasta el �ltimo directorio de donde se carg� o salv� un
     * archivo
     */
    private String ultimoDirectorio;
    /**
     * Es una referencia a la exposici�n de perros
     */
    private Cliente cliente;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------
    /**
     * Es el panel donde se muestra la lista de fincas
     */
    private PanelListaArchivos panelArchivos;


    /**
     * panel lista animales en fincas
     */
    private PanelListaDescargas panelDescargas;

   
    /**
     * Es el panel donde est�n los botones para los puntos de extensi�n
     */
    private PanelExtension panelExtension;
    

 

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------
    /**
     * Construye la interfaz e inicializa todos sus componentes.
     *
     * @param archivoPerros es el nombre del archivo de propiedades que contiene
     * la informaci�n de los perros
     */
    public InterfazCLiente() {

        cliente = new Cliente(this);
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Cliente FTP");
        setSize(500, 700);
        setVisible(true);
        setResizable(true);

        setLayout(new BorderLayout());

        JPanel panelListas = new JPanel(new GridLayout(1, 2));

        panelArchivos = new PanelListaArchivos(this);
        panelListas.add(panelArchivos);

        panelDescargas = new PanelListaDescargas(this);
        panelListas.add(panelDescargas);

       

        add(panelListas, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new GridLayout(2, 1));
        panelExtension = new PanelExtension(this);
        inferior.add(panelExtension);
        add(inferior, BorderLayout.SOUTH);

        actualizarListaArchivos();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setSize(500, 700);
        setVisible(true);
        setResizable(true);

    }

    // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------
    /**
     * Actualiza la lista de perros mostrada.
     */
    private void actualizarListaArchivos() {
    	System.out.println("entro");
        try {
        	String[] files = cliente.requestFiles();
        	System.out.println(files);
        	panelArchivos.refrescarLista(files);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

    }
    
     public void startDownload()
     {
    	 try {
			if(!cliente.downloadHasStarted()) cliente.selectFile(panelArchivos.getArchivoSeleccionado());
			cliente.startDownload();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
     }
     
     
     public void stopDownload()
     {
    	 try {
			cliente.pauseDownload();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
     }
     
     

   
   

   
    // -----------------------------------------------------------------
    // Main
    // -----------------------------------------------------------------
    
    public static void main(String[] args) {
        InterfazCLiente interfaz = new InterfazCLiente();
      
    }

	@Override
	public void downloadCompleted() {
		// TODO Auto-generated method stub
		// AQUI: hacer lo necesario para deshabilitar el boton de pausa y habilitar el de descarga
	}
}
