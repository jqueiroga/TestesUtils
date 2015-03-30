package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

import br.jus.tjpb.fta.utils.CSVUtils;
import br.jus.tjpb.fta.utils.Config;
import br.jus.tjpb.fta.utils.DadosUtils;

public class St_UC100CadastrarAdvogadoTest extends SiscomTest {

	private CSVUtils cadAdvFile = new CSVUtils("siscomw", "advogados.csv",';');
	
	private final int PRIMEIRABUSCA = 6;
	private final String MSG_OAB_NAOENCONTRADO = "O número OAB informado não foi encontrado.";
	private final String MSG_OAB_JACADASTRATO = "Já existe um advogado com o número OAB informado.";
	private final String MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS = "Campo obrigatório não preenchido.";
		
	@Override
	public void setUp() throws Exception {
		super.setUp();
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
		int id = realizarConsultaPrimeiroAcessoOAB(PRIMEIRABUSCA); //id do elemento no arquivo CSV
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS);
	}
	
	@Test
	public void cadastrarAdvogadoSucessoTest() throws Exception {
		int id = realizarConsultaPrimeiroAcessoOAB(PRIMEIRABUSCA);
				
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		System.out.println("Texto: "+this.getDriver().findElement(By.xpath("//*[@class='text-success']")).getText());
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@class='text-success']")).getText(),
				"Seu cadastro foi realizado com sucesso! Dentro de instantes você receberá um email de confirmação.");
		
	}
	
	private int realizarConsultaPrimeiroAcessoOAB(int primeiroId) {
		int id = primeiroId; //id do elemento no arquivo CSV
		boolean oabjacadastrada = true;
		while(oabjacadastrada) {
			realizarConsultaInicialOAB(id, true);
			try {
				if(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText().equalsIgnoreCase(MSG_OAB_JACADASTRATO) || 
						this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText().equalsIgnoreCase(MSG_OAB_NAOENCONTRADO)) {
					id += 1;
					this.getDriver().findElement(By.id("close-error-msg")).click();
				} else {
					oabjacadastrada = false;
				}
			} catch(NoSuchElementException e) {
				oabjacadastrada = false;
			}
		}
		return id;
	}

	private void realizarConsultaInicialOAB(int id, boolean valido) {
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
	
	private void inserirDadosPessoais(int id, boolean valido, boolean defensor, String email) {
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
	
	private void inserirEndereco(int id, boolean valido) {
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
		//this.getDriver().findElement(By.id("formPesquisaEndereco:linkPesquisaEndereco")).click();
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
