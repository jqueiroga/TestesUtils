package br.jus.tjpb.fta.projetos.siscomw.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import br.jus.tjpb.fta.modelo.FtaPageObject;
import br.jus.tjpb.fta.utils.CSVUtils;
import br.jus.tjpb.fta.utils.DadosUtils;

public class CadastroAdvogadoPageObject extends FtaPageObject {

	/**
	 * Utilitário de manipulação do arquivo CSV
	 */
	private CSVUtils cadAdvFile = new CSVUtils("siscomw", "advogados.csv",';');
	
	/**
	 * Constates com dados e mensagem de configuração fixa na página
	 */
	private int ultimoIdUtilizadoCSV = 20;
	public static final String MSG_OAB_NAOENCONTRADO = "O número OAB informado não foi encontrado.";
	public static String MSG_OAB_JACADASTRATO = "Já existe um advogado com o número OAB informado.";
	public static String MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS = "Campo obrigatório não preenchido.";
	public static String MSG_OAB_SITUACAOIRREGULAR = "Este número não pode ser cadastrado. Verifique sua situação junto à OAB.";
	public static String MSG_FORMATODASENHAINVALIDO = "A senha de cadastro de advogado precisa ter, obrigatoriamente, no mínimo 8 caracteres, entre letras e números.";
	public static String MSG_EMAISDIGITADOSDIFERENTES = "Os emails digitados são diferentes.";
	public static String MSG_SENHASDIGITADASDIFERENTES = "As senhas digitadas são diferentes.";
	public static String MSG_CPFDIGITADODIFERENTE = "O CPF digitado não confere com a OAB informada.";
	//public static String MSG_CPFINVALIDODIGITADODIFERENTE = "Campos informados no formato inválido!";
	public static String MSG_CEP_NAOENCONTRADO = "CEP não encontrado.";
	public static String MSG_SUCESSO = "Seu cadastro foi realizado com sucesso! Dentro de instantes você receberá um email de confirmação.";
		
	public CadastroAdvogadoPageObject(WebDriver driver, String baseURL) {
		super(driver, baseURL);
	}

	public CadastroAdvogadoPageObject(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Este método preenche os dados da consulta inicial por OAB de 
	 * acordo com os dados contidos na linha "id" do arquivo CSV OU
	 * informando dados inválidos (caso "valido" = false)
	 * @param id
	 * @param valido
	 */
	public void realizarConsultaInicialOAB(int id, boolean valido) {
		String oab, letra, ufoab;
		
		if(valido) {
			oab = cadAdvFile.getDado(id, "oab");
			letra = cadAdvFile.getDado(id, "letra");
			ufoab = cadAdvFile.getDado(id, "uf_oab");
		} else {
			oab = DadosUtils.getNumero(3333, 9999);
			letra = "A";
			ufoab = "DF";
		}
		
		this.getDriver().findElement(By.id("form_oab:oab")).clear();
		this.getDriver().findElement(By.id("form_oab:oab")).sendKeys(oab);
		new Select(this.getDriver().findElement(By.id("form_oab:letra_oab"))).selectByVisibleText(letra);
		new Select(this.getDriver().findElement(By.id("form_oab:uf_oab"))).selectByVisibleText(ufoab);
		this.getDriver().findElement(By.id("form_oab:btn_consultar_oab")).click();
	}

	/**
	 * Este método auxilia no encontro de um registro no arquivo CSV
	 * que esteja disponível para cadastro, consultando sequencialmente
	 * os advogados (pela OAB) a partir do "primeiroId" informado.
	 * @param primeiroId
	 * @return
	 */
	public int realizarConsultaPrimeiroAcessoOAB(int primeiroId) {
		int id = primeiroId; //id do elemento no arquivo CSV
		boolean oabjacadastrada = true;
		while(oabjacadastrada) {
			realizarConsultaInicialOAB(id, true);
			try {
				WebElement element = this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span"));
				if( element.getText().equalsIgnoreCase(MSG_OAB_JACADASTRATO) || 
					element.getText().equalsIgnoreCase(MSG_OAB_NAOENCONTRADO) ||
					element.getText().equalsIgnoreCase(MSG_OAB_SITUACAOIRREGULAR)) {
					id += 1;
					this.getDriver().findElement(By.id("close-error-msg")).click();
				} else {
					oabjacadastrada = false;
					this.ultimoIdUtilizadoCSV = id;
				}
			} catch(NoSuchElementException e) {
				oabjacadastrada = false;
				this.ultimoIdUtilizadoCSV = id;
			}
		}
		return id;
	}
	
	/**
	 * Este método preenche os dados do frame "Dados Pesoais" de 
	 * acordo com os dados contidos na linha "id" do arquivo CSV,
	 * informando se o advogado que está sendo cadastrado é "defensor" 
	 * (caso "defensor"=true) e o respectivo "email" OU informando 
	 * dados inválidos (caso "valido" = false)
	 * @param id
	 * @param valido
	 * @param defensor
	 * @param email
	 */
	public void inserirDadosPessoais(int id, boolean valido, boolean defensor, String email) {
		String rg, emissor, uf, genero, cpf, telefone, nascimento, senha;
		((JavascriptExecutor)this.getDriver()).executeScript("document.getElementById('form_cadastro_advogado:data_nascimento_input').removeAttribute('readonly');");
		
		if(valido) {
			rg = cadAdvFile.getDado(id, "rg");
			uf = cadAdvFile.getDado(id, "uf_rg");
			genero = cadAdvFile.getDado(id, "genero");
			cpf = DadosUtils.completarComCaracteresAEsquerda(cadAdvFile.getDado(id, "cpf"), 11, "0");
			senha = cadAdvFile.getDado(id, "senha");
		} else {
			rg = DadosUtils.getNumero(1111111, 999999999);
			emissor = "SSP";
			uf = "DF";
			genero = "Masculino";
			cpf = DadosUtils.getNumero(11111111111l, 99999999999l);
			email = DadosUtils.getEmail(25);
			senha = "123456ab";
		}
		emissor = "SSP";
		telefone = DadosUtils.getNumero(1111111111l, 9999999999l);
		nascimento = DadosUtils.getData(1980, 2000);
		
		if(defensor) {
			this.getDriver().findElement(By.id("form_cadastro_advogado:defensor_publico")).click();
		}
		this.getDriver().findElement(By.id("form_cadastro_advogado:rg")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:rg")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:rg")).sendKeys(rg);
		new Select(this.getDriver().findElement(By.id("form_cadastro_advogado:emissor"))).selectByVisibleText(emissor);
		new Select(this.getDriver().findElement(By.id("form_cadastro_advogado:uf_emissor"))).selectByVisibleText(uf);
		new Select(this.getDriver().findElement(By.id("form_cadastro_advogado:genero"))).selectByVisibleText(genero);
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).sendKeys(cpf);
		this.getDriver().findElement(By.id("form_cadastro_advogado:telefone")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:telefone")).sendKeys(telefone);
		this.getDriver().findElement(By.id("form_cadastro_advogado:data_nascimento_input")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:data_nascimento_input")).sendKeys(nascimento);
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).sendKeys(email);
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).sendKeys(email);
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).sendKeys(senha);
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).sendKeys(senha);
	}
	
	/**
	 * Este método preenche os dados do modal de endereço
	 * a partir dos dados presentes na linha "id" do CSV OU
	 * informando dados inválidos (caso "valido" = false)
	 * @param id
	 * @param valido
	 */
	public void inserirEndereco(int id, boolean valido) {
		String cep, numero, complemento;
		
		if(valido) {
			cep = cadAdvFile.getDado(id, "cep");
		} else {
			cep = "";
		}
		numero = DadosUtils.getNumero(1, 9999);
		complemento = DadosUtils.getTexto(DadosUtils.LETRAS, 200);

		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).click();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).clear();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).sendKeys(cep);
		if(cep != null && cep != "") {
			this.getDriver().findElement(By.id("formPesquisaEnderecoTabela:tabela:0:link1")).click();
		}
		
		this.getDriver().findElement(By.id("formInputEndereco:numero")).click();
		this.getDriver().findElement(By.id("formInputEndereco:numero")).clear();
		this.getDriver().findElement(By.id("formInputEndereco:numero")).sendKeys(numero);
		
		this.getDriver().findElement(By.id("formInputEndereco:complemento")).click();
		this.getDriver().findElement(By.id("formInputEndereco:complemento")).clear();
		this.getDriver().findElement(By.id("formInputEndereco:complemento")).sendKeys(complemento);
	}
	
	public String getTextoMensagemErro() {
		return this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText();
	}
	
	public String getTextoMensagemPaginaSucesso() {
		return this.getDriver().findElement(By.xpath("//*[@class='text-success']")).getText();
	}

	public void clicarNoBotaoPesquisarEndereco() {
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
	}

	public void clicarNoBotaoIncluirEndereco() {
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
	}

	public void clicarNoBotaoFinalizarCadastro() {
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
	}
	
	/**
	 * Preenche o CEP com os dados do CSV OU 
	 * preenche o CEP com 00.000-00 (caso 'valido' = false)
	 * @param id
	 * @param valido
	 */
	public void preencherCEP(int id, boolean valido) {
		String cep;
		
		if(valido) {
			cep = cadAdvFile.getDado(id, "cep");
		} else {
			cep = "00000000";
		}
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).click();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).clear();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).sendKeys(cep);	
	}
	
	/**
	 * Preenche senha e repetir senha com os dados do CSV OU
	 * Preenche senha e repetir senha com dados inválidos (caso 'valido' = false)
	 * sejam eles dados inválidos porém iguais (formato de senha, caso 'iguais' = true)
	 * ou dados inválidos porém diferentes (caso 'iguais' = false).
	 * @param id
	 * @param valido
	 * @param iguais
	 */
	public void preencherSenhaRepetirSenha(int id, boolean valido, boolean iguais) {
		String senha, confirmacao_senha;
		if(valido) {
			senha = cadAdvFile.getDado(id, "senha");
			confirmacao_senha = senha;
		} else {
			if(iguais) {
				senha = "123456789";
				confirmacao_senha = "123456789";
			} else {
				senha = "123456789abCD";
				confirmacao_senha = "123456789ABc";
			}
		}
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).sendKeys(senha);
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).sendKeys(confirmacao_senha);
	}

	/**
	 * Preenche o email e repetir email com os dados do CSV OU 
	 * preenche o email e repetir email com dados divergentes (caso 'valido' = false)
	 * @param id
	 * @param valido
	 */
	public void preencherEmailRepetirEmail(String email, boolean valido) {
		String confirmacao_email;
		if(valido) {
			confirmacao_email = email;
		} else {
			confirmacao_email = DadosUtils.getEmail(20);
		}
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).sendKeys(email);
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).sendKeys(confirmacao_email);
		
	}

	/**
	 * Preenche CPF com os dados do CSV OU
	 * Preenche CPF inválido (caso 'valido' = false)
	 * seja ele CPF inválido (não pertence ao advogado) porém existe (formato de senha, caso 'existe' = true)
	 * ou CPF inválido porém é inexistente  (caso 'existe' = false).
	 * @param id
	 * @param valido
	 * @param existe
	 */
	public void preencherCPF(int id, boolean valido, boolean existe) {
		String cpf;
		if(valido) {
			cpf = cadAdvFile.getDado(id, "cpf");
		} else {
			if(existe) {
				cpf = "01374919489";
			} else {
				cpf = DadosUtils.getNumero(11);
			}
		}
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).sendKeys(cpf);
	}
	
}
