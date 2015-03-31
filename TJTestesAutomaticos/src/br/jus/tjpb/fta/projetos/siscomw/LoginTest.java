package br.jus.tjpb.fta.projetos.siscomw;

import org.junit.Test;
import org.openqa.selenium.By;

import br.jus.tjpb.fta.utils.CSVUtils;

public class LoginTest extends SiscomTest {

	private CSVUtils loginFile = new CSVUtils("siscomw", "login.csv");
	
	@Override
	public void init() {
		this.getDriver().get(this.getBaseUrl());
	}

	@Test
	public void loginTest() throws Exception {
			
		String[] perfis = {"atendimento", "distribuidor", "serventuario"};
		this.loginsConsecutivos(perfis);
		
	}
	
	/**
	 * Uma vez logado, clica no item "Sair" para deslogar
	 */
	public void logout() {
		this.getDriver().findElement(By.id("user-data-logout")).click();
		this.getDriver().findElement(By.linkText("Sim, quero encerrar a sessão!")).click();
	}
	
	/**
	 * Faz o login e em seguida o logout para um "perfil" informado (válido)
	 * @param perfis
	 */
	public void loginsConsecutivos(String[] perfis) {
		for(String perfil: perfis) {
			login(perfil);
			logout();
		}
	}
	
	/**
	 * Realiza o login para um "perfil" informado (válido)
	 * @param perfil
	 */
	public void login(String perfil) {
		int i = 0;
		switch (perfil) {
			case "atendimento":	i = 1; break;
			case "distribuidor": i = 2; break;
			case "serventuario": i = 3; break;
		}
		this.getDriver().findElement(By.id("username")).clear();
		this.getDriver().findElement(By.id("username")).sendKeys(loginFile.getDado(i, "login"));
		this.getDriver().findElement(By.id("password")).clear();
		this.getDriver().findElement(By.id("password")).sendKeys(loginFile.getDado(i, "senha"));
		this.getDriver().findElement(By.xpath("//input[@value='Entrar']")).click();
	}
	
}
