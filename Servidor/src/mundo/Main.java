package mundo;

import java.io.IOException;

public class Main {

	private static final int BAJA = 0;
	private static final int MEDIA = 1;
	private static final int ALTA = 2;

	private static int T_CONEXION = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int BAJA_LATENCIA = MEDIA; //importancia ALTA, MEDIA o BAJA
	private static int ALTO_ANCHO = ALTA; //importancia ALTA, MEDIA o BAJA
	
	private static boolean test10Buffer = false;
	private static boolean test500Buffer = false;
	
	private static boolean test10Tam = false;
	private static boolean test200Tam = false;

	public static void main(String[] args) throws IOException {
		int maxConexiones=500, buffer=40000, tPaquetes=2000, minutos=5;
		if (T_CONEXION==BAJA){
			minutos = 10;
		} else if (T_CONEXION==ALTA){
			minutos = 1;
		}
		if (test10Buffer) buffer*=0.1; //utiliza el 10%
		else if (test500Buffer) buffer*=5; //utiliza el 500%
		if (test10Tam) tPaquetes*=0.1; //utiliza el 10%
		else if (test200Tam) tPaquetes*=2; //utiliza el 200%
		//Servidor(maxConexiones, buffer[en bytes], tamaño paquetes, minutos conexión inactiva)
		Servidor s = new Servidor(maxConexiones, buffer, tPaquetes, minutos, T_CONEXION, BAJA_LATENCIA, ALTO_ANCHO);
	}

}
