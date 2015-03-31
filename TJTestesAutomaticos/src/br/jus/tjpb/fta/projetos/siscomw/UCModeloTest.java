package br.jus.tjpb.fta.projetos.siscomw;

import org.junit.Test;

import br.jus.tjpb.fta.utils.CSVUtils;

public class UCModeloTest extends SiscomTest {

	//OtherTestClasses
	private LoginTest login;
	//ArquivosCSV
	private CSVUtils ucModeloTestCSV = new CSVUtils("siscomw", "teste.csv");

	@Override
	public void init() {
		login = new LoginTest();
		login.setDriver(this.getDriver());
		login.setBaseUrl(this.getBaseUrl());
	}
	
	@Test
	public void ucModeloTest() {
		this.getDriver().get(this.getBaseUrl());
		
		login.login("atendimento");
		login.logout();
	}
	
}
