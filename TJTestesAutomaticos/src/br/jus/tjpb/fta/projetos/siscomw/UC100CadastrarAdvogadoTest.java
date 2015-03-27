package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;

import br.jus.tjpb.fta.utils.CSVUtils;
import br.jus.tjpb.fta.utils.Config;
import br.jus.tjpb.fta.utils.DadosUtils;

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
		
		realizarConsultaInicialOAB(id, false);
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), 
				"O número OAB informado não foi encontrado.");
	}
	
	@Test
	public void advogadoJaCadastradoTest() throws Exception {
		int id = 2; //id do elemento no arquivo CSV
		
		realizarConsultaInicialOAB(id, true);
		
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), 
				"Já existe um advogado com o número OAB informado.");
	}
	
	@Test
	public void camposObrigatoriosNaoPreenchidos() throws Exception {
		int id = 3; //id do elemento no arquivo CSV
		realizarConsultaInicialOAB(id, true);
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		assertEquals(this.getDriver().findElement(By.xpath("//*[@id='messages']/div/ul/li/span")).getText(), 
				"Campo obrigatório não preenchido.");
	}
	
	@Test
	public void cadastrarAdvogadoSucessoTest() throws Exception {
		int id = 4; //id do elemento no arquivo CSV
		
		realizarConsultaInicialOAB(id, true);
		
		inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.getDriver().findElement(By.id("form_cadastro_advogado:btn_pesquisar_endereco")).click();
		inserirEndereco(id,true);
		this.getDriver().findElement(By.id("formInputEndereco:btIncluir")).click();
		this.getDriver().findElement(By.id("form_cadastro_advogado:btnFinalizarCadastro")).click();
		
		assertEquals(this.getDriver().findElement(By.xpath("//h3")).getText(),
				"Seu cadastro foi realizado com sucesso! Dentro de instantes você receberá um email de confirmação.");
		
	}

	private void realizarConsultaInicialOAB(int id, boolean valido) {
		String oab, letra, ufoab;
		
		if(valido) {
			oab = cadAdvFile.getDado(id, "oab");
			letra = cadAdvFile.getDado(id, "letra");
			ufoab = cadAdvFile.getDado(id, "uf");
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
		String rg, emissor, uf, genero, cpf, telefone, nascimento;
		((JavascriptExecutor)this.getDriver()).executeScript("document.getElementById('form_cadastro_advogado:data_nascimento_input').removeAttribute('readonly');");
		
		if(valido) {
			rg = cadAdvFile.getDado(id, "rg");
			uf = cadAdvFile.getDado(id, "uf");
			genero = cadAdvFile.getDado(id, "genero");
			cpf = cadAdvFile.getDado(id, "cpf");
		} else {
			rg = DadosUtils.getNumero(1111111, 999999999);
			emissor = "SSP";
			uf = "DF";
			genero = "Masculino";
			cpf = DadosUtils.getNumero(11111111111l, 99999999999l);
			email = DadosUtils.getEmail(25);
		}
		emissor = "SSP";
		telefone = DadosUtils.getNumero(1111111111l, 9999999999l);
		nascimento = DadosUtils.getData(1980, 2000);
		
		if(defensor) {
			this.getDriver().findElement(By.id("form_cadastro_advogado:defensor_publico")).click();
		}
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

		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).clear();
		this.getDriver().findElement(By.id("formPesquisaEndereco:autocompleteCep_input")).sendKeys(cep);
		this.getDriver().findElement(By.id("formPesquisaEndereco:linkPesquisaEndereco")).click();
		if(cep != null && cep != "") {
			this.getDriver().findElement(By.id("formPesquisaEnderecoTabela:tabela:0:link1")).click();
		}
		
		this.getDriver().findElement(By.id("formInputEndereco:numero")).clear();
		this.getDriver().findElement(By.id("formInputEndereco:numero")).sendKeys(numero);
		
		this.getDriver().findElement(By.id("formInputEndereco:complemento")).clear();
		this.getDriver().findElement(By.id("formInputEndereco:complemento")).sendKeys(complemento);
		
	}
}
