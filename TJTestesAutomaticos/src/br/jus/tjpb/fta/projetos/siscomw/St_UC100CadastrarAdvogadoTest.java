package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import br.jus.tjpb.fta.utils.CSVUtils;
import br.jus.tjpb.fta.utils.Config;
import br.jus.tjpb.fta.utils.DadosUtils;

public class St_UC100CadastrarAdvogadoTest extends SiscomTest {

	/**
	 * Utilitário de manipulação do arquivo CSV
	 */
	private CSVUtils cadAdvFile = new CSVUtils("siscomw", "advogados.csv",';');
	
	/**
	 * Constates com dados e mensagem de configuração fixa na
	 * suite de testes
	 */
	private int ultimoIdUtilizadoCSV = 10;
	private final String MSG_OAB_NAOENCONTRADO = "O número OAB informado não foi encontrado.";
	private final String MSG_OAB_JACADASTRATO = "Já existe um advogado com o número OAB informado.";
	private final String MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS = "Campo obrigatório não preenchido.";
	private final String MSG_OAB_SITUACAOIRREGULAR = "Este número não pode ser cadastrado. Verifique sua situação junto à OAB.";
	private final String MSG_FORMATODASENHAINVALIDO = "A senha de cadastro de advogado precisa ter, obrigatoriamente, no mínimo 8 caracteres, entre letras e números.";
	private final String MSG_EMAISDIGITADOSDIFERENTES = "Os emails digitados são diferentes.";
	private final String MSG_SENHASDIGITADASDIFERENTES = "As senhas digitadas são diferentes.";
	private final String MSG_CPFDIGITADODIFERENTE = "O CPF digitado não confere com a OAB informada.";
	private final String MSG_CPFINVALIDODIGITADODIFERENTE = "Campos informados no formato inválido!";
	private final String MSG_CEP_NAOENCONTRADO = "CEP não encontrado.";
	private final String MSG_SUCESSO = "Seu cadastro foi realizado com sucesso! Dentro de instantes você receberá um email de confirmação.";
		
	@Override
	public void init() {
		this.setBaseUrl(Config.getString("siscomw.url.publica.cadastroAdvogado"));
		this.getDriver().get(this.getBaseUrl());
	}

	// SISCW-183:OAB não encontrada
	@Test
	public void ct_OABNaoEncontrada() throws Exception {
		int id = 1; //id do elemento no arquivo CSV
		
		realizarConsultaInicialOAB(id, false);
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_OAB_NAOENCONTRADO);
	}
	
	//SISCW-182:Cadastrar Advogado com OAB já cadastrada no Siscom-w
	@Test
	public void ct_advogadoJaCadastrado() throws Exception {
		int id = 1; //id do elemento no arquivo CSV
		
		realizarConsultaInicialOAB(id, true);
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_OAB_JACADASTRATO);
	}
	
	//SISCW-186:Não preencher campos obrigatórios
	@Test
	public void ct_camposObrigatoriosNaoPreenchidos() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV); //id do elemento no arquivo CSV
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS);
	}
	
	//SISCW-186:Não preencher campos obrigatórios
	@Test
	public void ct_camposObrigatoriosNaoPreenchidosModalEndereco() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV); //id do elemento no arquivo CSV
		
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");

		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		//Endereço//
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).click();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).clear();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).sendKeys("00000000");
		////////////		
		/*this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		*/
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_CEP_NAOENCONTRADO);
	}
	
	// SISCW-181: Cadastrar Advogado com OAB válida
	@Test
	public void ct_cadastrarAdvogadoSucessoTest() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@class='text-success']")).getText(), MSG_SUCESSO);
		
	}
	
	// SISCW-185:Senha fora do Padrão
	@Test
	public void ct_senhaForaDoPadrao() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).sendKeys("123456789");
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).sendKeys("123456789");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_FORMATODASENHAINVALIDO);
		
	}
	
	//SISCW-1174:Dados divergentes em Email e Senha (foco email)
	@Test
	public void ct_emailDiferenteDaConfirmacao() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:email")).sendKeys("diego.quirino@tjpb.jus.br");
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_email")).sendKeys("rogerio.nibon@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_EMAISDIGITADOSDIFERENTES);
		
	}
	
	//SISCW-1174:Dados divergentes em Email e Senha (foco senha)
	@Test
	public void ct_senhaDiferenteDaConfirmacao() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:senha")).sendKeys("12345678abc");
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:confirmacao_senha")).sendKeys("123456789AbCd");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_SENHASDIGITADASDIFERENTES);
		
	}
	
	//SISCW-1175:Status da OAB não permitidoID 21 SITUAÇÃO IRREGULAR OAB
	@Test
	public void ct_oabNaoPermitidaSituacaoIrregular() throws Exception {
		int id = 21; //id do elemento no arquivo CSV
		
		realizarConsultaInicialOAB(id, true);
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_OAB_SITUACAOIRREGULAR);
	}
	
	//SISCW-198:CPF de advogado digitado é diferente do recuperado
	@Test
	public void ct_advogadoOABCPFDiferenteRecuperado() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
		
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).sendKeys("01374919489");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_CPFDIGITADODIFERENTE);
		
	}
	
	//SISCW-198:CPF de advogado digitado é diferente do recuperado -> CPF INVÁLIDO
	@Test
	public void ct_advogadoOABCPFInvalidoDiferenteRecuperado() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);

		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");

		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).clear();
		this.getDriver().findElement(By.id("form_cadastro_advogado:cpf")).sendKeys(DadosUtils.getNumero(11111111111l, 99999999999l));

		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();

		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();

		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_CPFINVALIDODIGITADODIFERENTE);

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
}
