package mundo;

import java.io.IOException;

public class Main {

	private static final int BAJA = 0;
	private static final int MEDIA = 1;
	private static final int ALTA = 2;

	private static int T_CONEXION = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int BAJA_LATENCIA = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int ALTO_ANCHO = MEDIA; //importancia ALTA, MEDIA o BAJA
	
	private static boolean test10Buffer = false;
	private static boolean test500Buffer = false;
	
	private static boolean test10Tam = false;
	private static boolean test200Tam = true;

	public static void main(String[] args) throws IOException {
		int maxConexiones=500, buffer=8000, tPaquetes=8000, minutos=5; //condiciones medias
		if (T_CONEXION==BAJA){
			minutos = 1;
		} else if (T_CONEXION==ALTA){
			minutos = 10;
		}
		if (BAJA_LATENCIA==BAJA){ //no importa que haya baja latencia
			buffer = 16000;
		} else if (BAJA_LATENCIA==ALTA){
			buffer = 2000;
		}
		if (ALTO_ANCHO==BAJA){  //no importa que use poco ancho de banda
			tPaquetes = 2000;
		} else if (ALTO_ANCHO==ALTA){
			tPaquetes = 1600;
		}
		if (test10Buffer) buffer*=0.1; //utiliza el 10%
		else if (test500Buffer) buffer*=5; //utiliza el 500%
		if (test10Tam) tPaquetes*=0.1; //utiliza el 10%
		else if (test200Tam) tPaquetes*=2; //utiliza el 200%
		//Servidor(maxConexiones, buffer[en bytes], tama�o paquetes, minutos conexi�n inactiva)
		Servidor s = new Servidor(maxConexiones, buffer, tPaquetes, minutos);
	}

}
