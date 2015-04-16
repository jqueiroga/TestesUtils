package br.jus.tjpb.fta.projetos.siscomw;

import org.junit.Test;

import br.jus.tjpb.fta.projetos.siscomw.pages.LoginPageObject;

public class St_UC000LoginTest extends SiscomTest {
	
	private LoginPageObject loginPO;
	
	@Override
	public void init() {
		/**
		 * Cria��o dos objetos que ser�o utilizados na p�gina
		 */
		this.loginPO = new LoginPageObject(this.getDriver(), this.getBaseUrl());
		/**
		 * Acessar BaseURL
		 */
		this.loginPO.acessarBaseURL();
	}

	@Test
	public void loginTest() throws Exception {
		String[] perfis = {"atendimento", "distribuidor", "serventuario"};
		this.loginPO.loginsConsecutivos(perfis);
	}
		
	
}
