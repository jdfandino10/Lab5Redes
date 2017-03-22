
package Interfaz;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mundo.*;

/**
 * Este es el panel donde se encuentran los botones para realizar los ordenamientos por distintos criterios y las b�squedas.
 */
public class PanelListaArchivos extends JPanel implements ListSelectionListener
{
	// -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Es una referencia a la clase principal de la interfaz
     */
    private InterfazCLiente principal;

    // -----------------------------------------------------------------
    // Atributos de la Interfaz
    // -----------------------------------------------------------------

    /**
     * Es la lista que se muestra
     * 
     */
    
    private JButton boton;
    private JList listaArchivos;

    /**
     * Componente de desplazamiento para contener la lista gr�fica
     */
    private JScrollPane scroll;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Construye el panel e inicializa todos sus componentes
     * @param ventanaPrincipal es una referencia a la clase principal de la interfaz - ventanaPrincipal != null
     */
    public PanelListaArchivos( InterfazCLiente ventanaPrincipal )
    {
        principal = ventanaPrincipal;
        setLayout( new BorderLayout( ) );
        setBorder( new CompoundBorder( new EmptyBorder( 4, 3, 3, 3 ), new TitledBorder( "Lista de Archivos" ) ) );
       
        listaArchivos = new JList( );
        listaArchivos.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listaArchivos.addListSelectionListener( this );
        listaArchivos.setFont((new Font("Arial",Font.BOLD,32)));
        scroll = new JScrollPane( listaArchivos );
        scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scroll.setBorder( new CompoundBorder( new EmptyBorder( 3, 3, 3, 3 ), new LineBorder( Color.BLACK, 1 ) ) );

        add( scroll, BorderLayout.CENTER );
    }

    // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------

    /**
     * Actualiza la lista de perros que se est� mostrando
     * @param nuevaLista es una lista con los perros que deben mostrarse
     */
    public void refrescarLista(String[] archivos  )
    {
        listaArchivos.setListData( archivos );
        listaArchivos.setSelectedIndex( 0 );
    }
     public void ordenarPor( ArrayList lista )
    {
        listaArchivos.setListData( lista.toArray() );
        listaArchivos.setSelectedIndex( 0 );
    }

    /**
     * Selecciona un elemento de la lista
     * @param seleccionado es la posici�n del elemento que se debe seleccionar
     */
    public void seleccionar( int seleccionado )
    {
        listaArchivos.setSelectedIndex( seleccionado );
        listaArchivos.ensureIndexIsVisible( seleccionado );
    }

    /**
     * Cambia la informaci�n del perro que se est� mostrando de acuerdo al nuevo perro seleccionado
     * @param e es el evento de cambio el �tem seleccionado en la lista
     */
    public void valueChanged( ListSelectionEvent e )
    {
        if( listaArchivos.getSelectedValue( ) != null )
        {
            String p = ( String )listaArchivos.getSelectedValue( );
        }
    }
    public String getArchivoSeleccionado()
    {
    	if( listaArchivos.getSelectedValue( ) != null )
        {
            String p = ( String )listaArchivos.getSelectedValue( );
            return p;
           
        }
    	return null;
    	
    }
}