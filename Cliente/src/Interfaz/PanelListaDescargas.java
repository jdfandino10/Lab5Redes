
package Interfaz;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import mundo.*;

/**
 * Es el panel donde se muestra la lista de perros y est�n los botones para interactuar con la lista
 */
public class PanelListaDescargas extends JPanel implements ListSelectionListener, MouseListener
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
     */
    private JList listaDescarga;

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
    public PanelListaDescargas( InterfazCLiente ventanaPrincipal )
    {
        principal = ventanaPrincipal;
        
        

        setLayout( new BorderLayout( ) );
        setBorder( new CompoundBorder( new EmptyBorder( 4, 3, 3, 3 ), new TitledBorder( "Lista de Descargas" ) ) );

        listaDescarga = new JList( );
        listaDescarga.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listaDescarga.addListSelectionListener( this );
        listaDescarga.setFont((new Font("Arial",Font.BOLD,32)));
        listaDescarga.addMouseListener(this);

        scroll = new JScrollPane( listaDescarga );
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
    public void refrescarLista( File[] files )
    {
        listaDescarga.setListData( files );
        listaDescarga.setSelectedIndex( 0 );
    }

    /**
     * Selecciona un elemento de la lista
     * @param seleccionado es la posici�n del elemento que se debe seleccionar
     */
    public void seleccionar( int seleccionado )
    {
        listaDescarga.setSelectedIndex( seleccionado );
        listaDescarga.ensureIndexIsVisible( seleccionado );
    }

    /**
     * Cambia la informaci�n del perro que se est� mostrando de acuerdo al nuevo perro seleccionado
     * @param e es el evento de cambio el �tem seleccionado en la lista
     */
    public void valueChanged( ListSelectionEvent e )
    {
//    	if( listaFinca.getSelectedValue( ) != null )
//        {
//              Finca p = ( Finca )listaFinca.getSelectedValue( );
//              principal.actualizarListaAnimal(p);
//              principal.verDatosFinca(p);
//              
//              
//         }
    }
    
    public File getArchivoSeleccionado()
    {
    	if( listaDescarga.getSelectedValue( ) != null )
        {
            File p = ( File )listaDescarga.getSelectedValue( );
            return p;
           
        }
    	return null;
    	
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			principal.openFile(getArchivoSeleccionado());
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
