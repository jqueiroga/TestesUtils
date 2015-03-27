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

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = Config.getString("siscomw.url"); //$NON-NLS-1$
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		initOtherTestClasses();
	}
	
	public void initOtherTestClasses() {
		//Nenhuma inicialização requerida no pai;
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
