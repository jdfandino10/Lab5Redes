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
    
    private JLabel fileDownloading;
    

 

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
        
        JPanel fileInfo = new JPanel(new GridLayout(1, 2));
        fileDownloading = new JLabel("seleccione un archivo");
        fileInfo.add(new JLabel("Estado descarga: "));
        fileInfo.add(fileDownloading);
        JPanel inferior = new JPanel(new GridLayout(3, 1));
        panelExtension = new PanelExtension(this);
        JPanel label = new JPanel(new GridLayout(1, 2));
        JLabel connected = new JLabel("Connected: ");
        isConnected = new JLabel("NO");
        label.add(connected);
        label.add(isConnected);
        inferior.add(fileInfo);
        inferior.add(panelExtension);
        inferior.add(label);
        add(inferior, BorderLayout.SOUTH);
        
        cliente = new Cliente(this, this);
        if(conectar()==1) panelExtension.changeSateButtons();
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
        try {
        	String[] files = cliente.requestFiles();
        	panelArchivos.refrescarLista(files);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

    }
    
    private void actualizarListaArchivosDescargados() {
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
			fileDownloading.setText("Descargando "+cliente.getSelectedFile());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
     }
     
     
     public void stopDownload()
     {
    	 try {
			cliente.pauseDownload();
			fileDownloading.setText("pausa archivo "+cliente.getSelectedFile());
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
    		 panelExtension.diableButtons();
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
		fileDownloading.setText("seleccione un archivo");
	}
   
    public void dispose() {
    	if(!cliente.downloadHasStarted() || cliente.isPaused()){
    		cliente.closeConnection();
    		super.dispose();
    	}else {
    		JOptionPane.showMessageDialog(this, "Detener la descarga antes de cerrar", "Alerta", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    public int conectar() {
    	String msg = "";
    	int ans;
    	if (cliente.goodConnection()){
    		msg = "Ya esta conectado";
    		ans = 0;
    	}
    	else if(cliente.connect()){
    		msg = "Conexion establecida";
    		ans=1;
    	}else {
    		msg = "No se ha podido establecer la conexion";
    		ans=-1;
    	}
    	JOptionPane.showMessageDialog(this, msg, "Conexion", JOptionPane.INFORMATION_MESSAGE);
    	return ans;
    }
   

   
    // -----------------------------------------------------------------
    // Main
    // -----------------------------------------------------------------
    
    public static void main(String[] args) {
        InterfazCLiente interfaz = new InterfazCLiente();
    }

	
}
