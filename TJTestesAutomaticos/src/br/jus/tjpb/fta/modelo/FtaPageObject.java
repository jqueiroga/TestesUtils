package br.jus.tjpb.fta.modelo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FtaPageObject {

	/**
	 * Referência para o WebDriver
	 */
	private WebDriver driver;
	
	/**
	 * URL Base de acesso à funcionalidade
	 */
	private String baseURL;

	public FtaPageObject(WebDriver driver, String baseURL) {
		super();
		this.driver = driver;
		this.baseURL = baseURL;
	}

	public FtaPageObject(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
		
	public void acessarBaseURL() {
		this.getDriver().get(this.baseURL);
	}
	
	public WebElement getElementById(String id) {
		return null;
	}
	
	public WebElement getElementByXpath(String id) {
		return null;
	}
}
