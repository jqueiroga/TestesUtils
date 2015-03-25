package br.jus.tjpb.fta.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

public class CSVUtils {

	private String basePath = (new File("")).getAbsolutePath() + "/src/br/jus/tjpb/fta/projetos/";
	
	private String arquivo;
	
	private char separador;
	
	private List<String[]> csv;

	public CSVUtils(String sistema, String arquivo, char separador) {
		super();
		init(sistema, arquivo, separador);
	}
	
	public CSVUtils(String sistema, String arquivo){
		super();
		init(sistema, arquivo, ',');
	}
	
	public void init(String sistema, String arquivo, char separador) {
		this.arquivo = sistema + "/dados/"+ arquivo;
		this.separador = separador;
		CSVReader csv = null;
		try {
			csv = new CSVReader(new FileReader(this.basePath+this.arquivo), this.separador);
			this.csv = csv.readAll();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String[]> getCsv() {
		return csv;
	}

	public void setCsv(List<String[]> csv) {
		this.csv = csv;
	}
	
	public String[] getCabecalho() {
		return this.getCsv().get(0);
	}
	
	public String getDado(int linha, String coluna) {
		String[] cabecalho = this.getCabecalho();
		for(int i=0; i<cabecalho.length;i++) {
			if(cabecalho[i].equalsIgnoreCase(coluna)) {
				return this.getCsv().get(linha)[i];
			}
		}
		return "";
	}
	
}
