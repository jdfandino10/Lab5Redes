package mundo;

import java.io.IOException;

public class Main {

	private static final int BAJA = 0;
	private static final int MEDIA = 1;
	private static final int ALTA = 2;

	private static int T_CONEXION = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int BAJA_LATENCIA = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int ALTO_ANCHO = MEDIA; //importancia ALTA, MEDIA o BAJA

	public static void main(String[] args) throws IOException {
		int maxConexiones=200, buffer=8000, tPaquetes=8000, minutos=5; //condiciones medias
		if (T_CONEXION==BAJA){
			minutos = 1;
		} else if (T_CONEXION==ALTA){
			minutos = 10;
		}
		if (BAJA_LATENCIA==BAJA){

		} else if (BAJA_LATENCIA==ALTA){

		}
		if (ALTO_ANCHO==BAJA){
			
		} else if (ALTO_ANCHO==ALTA){

		}
		//Servidor(maxConexiones, buffer[en bytes], tamaño paquetes, minutos conexión inactiva)
		Servidor s = new Servidor(maxConexiones, buffer, tPaquetes, minutos);
	}

}
