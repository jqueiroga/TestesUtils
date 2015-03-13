package br.jus.tjpb.testesUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeradorDeArquivosHelper {

	private int tamanho;
	
	private String txtInicio;
	
	private String txtFim;
	
	private String basePath = (new File("")).getAbsolutePath() + "\\src\\br\\jus\\tjpb\\testesUtils\\arquivos\\";

	public GeradorDeArquivosHelper(int tamanho, String txtInicio,
			String txtFim) {
		super();
		this.tamanho = tamanho;
		this.txtInicio = txtInicio;
		this.txtFim = txtFim;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public String getTxtInicio() {
		return txtInicio;
	}

	public void setTxtInicio(String txtInicio) {
		this.txtInicio = txtInicio;
	}

	public String getTxtFim() {
		return txtFim;
	}

	public void setTxtFim(String txtFim) {
		this.txtFim = txtFim;
	}
	
	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void gerarArquivo(String nomeDoArquivo) {
		try {
			File arquivo = new File(this.basePath+nomeDoArquivo);
			//Escrever inicio do arquivo
			FileWriter fw = new FileWriter(arquivo);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.write(txtInicio);
			for(int i = 0; i<(tamanho-1024)/2;i++) {
				writer.write("A ");
			}
			writer.write(this.txtFim);
			writer.close();
			fw.close();
			System.out.println("Arquivo escrito com sucesso:\n"+this.basePath+nomeDoArquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
