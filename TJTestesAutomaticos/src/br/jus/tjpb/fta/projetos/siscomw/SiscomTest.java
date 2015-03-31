package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import br.jus.tjpb.fta.utils.Config;

public class SiscomTest {

	/**
	 * Driver: inicializado por padrão para o Firefox
	 */
	private WebDriver driver;
	
	/**
	 * URL básica do teste 
	 */
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		//Inicializa o driver padrão do Firefox
		driver = new FirefoxDriver();
		//Inicializa a URL básica de acesso
		baseUrl = Config.getString("siscomw.url");
		//Inicializa o timeout padrão (5 segundos) e maximiza a tela do browser
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		//Faz as inicializações adicionais
		init();
	}
	
	/**
	 * Inicialização da suite de testes.
	 * Deve ser utilizado para: 
	 * (1) Inicializar uma outra suite de teste (ex: login) na suite de testes
	 *     atual a fim de que outros testes e métodos sejam reaproveitados
	 * (2) Executar passos como setup() dos testes na suite de teste atual.
	 */
	public void init() {
		// Nenhuma inicialização de outro caso de teste requerida aqui: classe pai;
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) { //$NON-NLS-1$
			fail(verificationErrorString);
		}
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public boolean isAcceptNextAlert() {
		return acceptNextAlert;
	}

	public void setAcceptNextAlert(boolean acceptNextAlert) {
		this.acceptNextAlert = acceptNextAlert;
	}

	public StringBuffer getVerificationErrors() {
		return verificationErrors;
	}

	public void setVerificationErrors(StringBuffer verificationErrors) {
		this.verificationErrors = verificationErrors;
	}

}
