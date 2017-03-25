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
public class InterfazCLiente extends JFrame implements DownloadListener, ConnectionStateListener{
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
    
    private JLabel isConnected;
    

 

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
        JPanel label = new JPanel(new GridLayout(1, 2));
        JLabel connected = new JLabel("Connected: ");
        isConnected = new JLabel("NO");
        label.add(connected);
        label.add(isConnected);
        inferior.add(panelExtension);
        inferior.add(label);
        add(inferior, BorderLayout.SOUTH);
        
        cliente = new Cliente(this, this);
        actualizarListaArchivos();
        actualizarListaArchivosDescargados();
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
    
    private void actualizarListaArchivosDescargados() {
    	System.out.println("entro");
        try {
        
        	panelDescargas.refrescarLista(cliente.getFiles());
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
     
     public void changeConnectionState(boolean isCon){
    	 if(isCon){
    		 isConnected.setText("SI");
    	 }
    	 else {
    		 isConnected.setText("NO");
    	 }
     }
     
     public void openFile(File file)
     {
    	 try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
     
     
    @Override
	public void downloadCompleted() {
    	
		actualizarListaArchivosDescargados();
		panelExtension.changeSateButtons();
	}
   
    public void dispose() {
    	cliente.closeConnection();
    	super.dispose();
    }
   

   
    // -----------------------------------------------------------------
    // Main
    // -----------------------------------------------------------------
    
    public static void main(String[] args) {
        InterfazCLiente interfaz = new InterfazCLiente();
      
    }

	
}
