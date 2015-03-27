package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import br.jus.tjpb.fta.utils.CSVUtils;
import br.jus.tjpb.fta.utils.Config;

public class UC100CadastrarAdvogadoTest extends SiscomTest {

	private CSVUtils cadAdvFile = new CSVUtils("siscomw", "advogado.csv");
		
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.setBaseUrl(Config.getString("siscomw.url.publica.cadastroAdvogado"));
		this.getDriver().get(this.getBaseUrl());
	}

	@Test
	public void oabNaoEncontradaTest() throws Exception {
		int id = 1; //id do elemento no arquivo CSV
		
		inserirDadosIniciais(cadAdvFile.getDado(id, "oab"), 
				cadAdvFile.getDado(id, "letra"),
				cadAdvFile.getDado(id, "uf"));
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), 
				"O número OAB informado não foi encontrado.");
	}
	
	@Test
	public void advogadoJaCadastradoTest() throws Exception {
		int id = 2; //id do elemento no arquivo CSV
		
		inserirDadosIniciais(cadAdvFile.getDado(id, "oab"), 
				cadAdvFile.getDado(id, "letra"),
				cadAdvFile.getDado(id, "uf"));
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), 
				"Já existe um advogado com o número OAB informado.");
	}
	
	private void inserirDadosIniciais(String oab, String letra, String uf) {
		this.getDriver().findElement(By.id("form_oab:oab")).clear();
		this.getDriver().findElement(By.id("form_oab:oab")).sendKeys(oab);
		new Select(this.getDriver().findElement(By.id("form_oab:letra_oab"))).selectByVisibleText(letra);
		new Select(this.getDriver().findElement(By.id("form_oab:uf_oab"))).selectByVisibleText(uf);
		this.getDriver().findElement(By.id("form_oab:btn_consultar_oab")).click();
	}
	
	
}
