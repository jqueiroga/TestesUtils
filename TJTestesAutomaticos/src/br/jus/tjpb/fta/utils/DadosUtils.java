package br.jus.tjpb.fta.utils;

import java.util.GregorianCalendar;

public class DadosUtils {

	private static String letras = " ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
	private static String letrasSemEspaco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String letrasNumeros = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static String numeros = "1234567890";
	private static String caracteresEspeciais = "!@#$%¨&*()-+/";
	
	public static int LETRAS = 0;
	public static int LETRASSEMESPACO = 1;
	public static int LETRASNUMEROS = 2;
	public static int LETRASNUMEROSCARACTERESESPECIAIS = 3;
	public static int LETRASCARACTERESESPECIAIS = 4;
	public static int CARACTERESESPECIAIS = 5;
	public static int CARACTERESESPECIAISNUMEROS = 6;
	
	public static String getTexto(int tipo, int tamanho) {
		String str = "";
		switch (tipo) {
		case 0: //LETRAS
			for(int i=0; i<tamanho; i++) {
				int pos = Integer.parseInt(getNumero(0, 27));
				str += letras.toCharArray()[pos];
			}
			break;
		case 1: //LETRASSEMESPACO
			for(int i=0; i<tamanho; i++) {
				int pos = Integer.parseInt(getNumero(0, 25));
				str += letrasSemEspaco.toCharArray()[pos];
			}
			break;
		case 2: //LETRASNUMEROS
			for(int i=0; i<tamanho; i++) {
				int pos = Integer.parseInt(getNumero(0, 35));
				str += letrasNumeros.toCharArray()[pos];
			}
			break;
		case 3: //LETRASNUMEROSCARACTERESESPECIAIS
			for(int i=0; i<tamanho/2; i++) {
				int pos1 = Integer.parseInt(getNumero(0, 35));
				int pos2 = Integer.parseInt(getNumero(0, 13));
				str += letrasNumeros.toCharArray()[pos1];
				str += caracteresEspeciais.toCharArray()[pos2];
			}
			break;
		case 4: //LETRASCARACTERESESPECIAIS
			for(int i=0; i<tamanho/2; i++) {
				int pos1 = Integer.parseInt(getNumero(0, 27));
				int pos2 = Integer.parseInt(getNumero(0, 13));
				str += letras.toCharArray()[pos1];
				str += caracteresEspeciais.toCharArray()[pos2];
			}
			break;
		case 5: //CARACTERESESPECIAIS
			for(int i=0; i<tamanho; i++) {
				int pos = Integer.parseInt(getNumero(0, 13));
				str += caracteresEspeciais.toCharArray()[pos];
			}
			break;
		case 6: //CARACTERESESPECIAISNUMEROS
			for(int i=0; i<tamanho/2; i++) {
				int pos1 = Integer.parseInt(getNumero(0, 9));
				int pos2 = Integer.parseInt(getNumero(0, 13));
				str += numeros.toCharArray()[pos1];
				str += caracteresEspeciais.toCharArray()[pos2];
			}
			break;
		default:
			str += "Nenhum tipo de texto válido selecionado";
			break;
		}
		return str;
	}
	
	public static String getNumero(double min, double max, int casasDecimais) {
		double dbl = (double) ((Math.random()*(max-min))+min);
		return String.format("%."+casasDecimais+"f", dbl);
	}
	
	public static String getNumero(int min, int max) {
		return "" + (int) ((Math.random()*(max-min))+min);
	}
	
	public static String getData() {
		return getData(1900,2010);
	}
	
	@SuppressWarnings("static-access")
	public static String getData(int anoMin, int anoMax) {
		GregorianCalendar gc = new GregorianCalendar();
        int year = Integer.parseInt(getNumero(anoMin, anoMax));
        gc.set(gc.YEAR, year);
        int dayOfYear = Integer.parseInt(getNumero(1, gc.getActualMaximum(gc.DAY_OF_YEAR)));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);
		return "" + gc.get(gc.DAY_OF_MONTH) + "/" + (gc.get(gc.MONTH)+1) + "/" + gc.get(gc.YEAR);
	}
	
	public static String getEmail(int tamanhoLogin) {
		String str = "";
		for(int i=0; i<tamanhoLogin; i++) {
			int pos = Integer.parseInt(getNumero(0, 25));
			str += letrasSemEspaco.toCharArray()[pos];
		}
		str += "@TJPB.JUS.BR";
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(getNumero(1.1, 10.5, 3));
		System.out.println(getNumero(1, 10));
		System.out.println(getNumero(1, 20.5, 1));
		System.out.println(getNumero(1.1, 20, 2));
		System.out.println(DadosUtils.getTexto(DadosUtils.LETRAS, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.LETRASSEMESPACO, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.LETRASNUMEROS, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.LETRASNUMEROSCARACTERESESPECIAIS, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.LETRASCARACTERESESPECIAIS, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.CARACTERESESPECIAIS, 20));
		System.out.println(DadosUtils.getTexto(DadosUtils.CARACTERESESPECIAISNUMEROS, 20));
		System.out.println(getData());
		System.out.println(getData(2000,2010));
		System.out.println(getEmail(15));
	}
}
