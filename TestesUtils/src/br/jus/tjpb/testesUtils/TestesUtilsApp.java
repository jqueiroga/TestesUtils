package br.jus.tjpb.testesUtils;

public class TestesUtilsApp {

	private static final int KB = 1024; //bytes
	
	private static final int MB = KB*1024; //bytes
	
	
	public static void main(String[] args) {
		int t1 = 1 * MB;
		int t2 = 2 * MB;
		int t3 = 3 * MB;
		int t4 = 1 * KB;
		int t5 = 512 * KB;
		int t6 = 2047 * KB;
		int t7 = 2049 * KB;
		String txtInicio = "<html><head><title>Teste Modelo Documento 2MB</title></head><body>Teste";
		String txtFim = "..FimTeste</body>";
		GeradorDeArquivosHelper garq = new GeradorDeArquivosHelper(t1, txtInicio, txtFim);
		garq.gerarArquivo("01modeloDocumento1MB.html");
		garq.setTamanho(t2);
		garq.gerarArquivo("02modeloDocumento2MB.html");
		garq.setTamanho(t3);
		garq.gerarArquivo("03modeloDocumento3MB.html");
		garq.setTamanho(t4);
		garq.gerarArquivo("04modeloDocumento1KB.html");
		garq.setTamanho(t5);
		garq.gerarArquivo("05modeloDocumento512KB.html");
		garq.setTamanho(t6);
		garq.gerarArquivo("06modeloDocumento2047KB.html");
		garq.setTamanho(t7);
		garq.gerarArquivo("07modeloDocumento2049KB.html");
	}

}
