/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: PanelExtension.java,v 1.4 2006/08/06 20:34:34 da-romer Exp $ 
 * Universidad de los Andes (Bogot� - Colombia)
 * Departamento de Ingenier�a de Sistemas y Computaci�n 
 * Licenciado bajo el esquema Academic Free License version 2.1 
 *
 * Proyecto Cupi2 (http://cupi2.uniandes.edu.co)
 * Ejercicio: n7_exposicionCanina 
 * Autor: Mario S�nchez - 25/08/2005
 * Modificado por: Daniel Romero- 30/06/2006  
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
 */

package Interfaz;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Es el panel que contiene los botones de extensi�n
 */
public class PanelExtension extends JPanel implements ActionListener
{
    // -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * El comando para el bot�n 1
     */
    private final String COMENZAR = "Comenzar";

    /**
     * El comando para el bot�n 2
     */
    private final String DETENER = "Detener";
    
   
     
    
 
    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Es la referencia a la interfaz de la aplicaci�n
     */
    private InterfazCLiente principal;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------

    /**
     * Es el bot�n 1
     */
    private JButton botonComenzarDescarga;
    
    private JButton botonDetenerDescarga;
   
    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Construye el panel con una referencia a la ventana principal de la aplicaci�n.
     * @param iec es una referencia a la ventana principal - ie!=null
     */
    public PanelExtension( InterfazCLiente iec )
    {
        principal = iec;
        inicializar( );
    }

    // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------

    /**
     * Inicializa los componentes del panel. <br>
     * <b>post: </b> Se inicializaron y se ubicaron los componentes del panel.
     */
    private void inicializar( )
    {
        setBorder( new TitledBorder( "Acciones" ) );

        setLayout( new GridLayout(1, 2) );
        botonComenzarDescarga = new JButton( "Descargar/Continuar");
        botonComenzarDescarga.setActionCommand( COMENZAR );
        botonComenzarDescarga.addActionListener( this );
        
        botonDetenerDescarga = new JButton( "Detener" );
        botonDetenerDescarga.setActionCommand( DETENER );
        botonDetenerDescarga.addActionListener( this );
        
       
        
        add( botonComenzarDescarga );
        add( botonDetenerDescarga );
       
    }
    
    public void changeSateButtons()
    {

    	botonComenzarDescarga.setEnabled(true);
    	botonDetenerDescarga.setEnabled(false);
    }

    /**
     * Este m�todo se llama cuando se presiona uno de los botones.
     * @param event es el evento del click en el bot�n
     */
    public void actionPerformed( ActionEvent event )
    {
        String comando = event.getActionCommand( );
        if( COMENZAR.equals( comando ) )
        {
        	botonComenzarDescarga.setEnabled(false);
        	botonDetenerDescarga.setEnabled(true);
        	principal.startDownload();
        }
        else if( DETENER.equals( comando ) )
        {
        	botonComenzarDescarga.setEnabled(true);
        	botonDetenerDescarga.setEnabled(false);
            principal.stopDownload();
        }
    
    }
    
}
