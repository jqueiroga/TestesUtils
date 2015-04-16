package br.jus.tjpb.fta.projetos.siscomw;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.jus.tjpb.fta.projetos.siscomw.pages.CadastroAdvogadoPageObject;
import br.jus.tjpb.fta.utils.Config;

@SuppressWarnings("static-access")
public class St_UC100CadastrarAdvogadoTest extends SiscomTest {
	
	CadastroAdvogadoPageObject cadAdvogadoPO;
	private int ultimoIdUtilizadoCSV = 20;
	
	@Override
	public void init() {
		/**
		 * Cria��o dos objetos que ser�o utilizados na p�gina
		 */
		this.cadAdvogadoPO = new CadastroAdvogadoPageObject(this.getDriver(), Config.getString("siscomw.url.publica.cadastroAdvogado"));
		/**
		 * Acessar BaseURL
		 */
		this.cadAdvogadoPO.acessarBaseURL();
	}

	// SISCW-183:OAB n�o encontrada
	@Test
	public void ct_OABNaoEncontrada() throws Exception {
		int id = 1; //linha do elemento no arquivo CSV
		
		this.cadAdvogadoPO.realizarConsultaInicialOAB(id, false);
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_OAB_NAOENCONTRADO);
		
	}
	
	//SISCW-182:Cadastrar Advogado com OAB j� cadastrada no Siscom-w
	@Test
	public void ct_advogadoJaCadastrado() throws Exception {
		int id = 1; //linha do elemento no arquivo CSV
		
		this.cadAdvogadoPO.realizarConsultaInicialOAB(id, true);
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_OAB_JACADASTRATO);
	}
	
	//SISCW-186:N�o preencher campos obrigat�rios
	@Test
	public void ct_camposObrigatoriosNaoPreenchidos() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV); //linha do elemento no arquivo CSV
		
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_CAMPOS_OBRIGATORIOS_NAOPREENCHIDOS);
	}
	
	//SISCW-186:N�o preencher campos obrigat�rios
	@Test
	public void ct_camposObrigatoriosNaoPreenchidosModalEndereco() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV); //linha do elemento no arquivo CSV
		
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");

		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.preencherCEP(id, false);
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_CEP_NAOENCONTRADO);
	}
	
	// SISCW-181: Cadastrar Advogado com OAB v�lida
	@Test
	public void ct_cadastrarAdvogadoSucessoTest() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemPaginaSucesso(), 
				this.cadAdvogadoPO.MSG_SUCESSO);
		
	}
	
	// SISCW-185:Senha fora do Padr�o
	@Test
	public void ct_senhaForaDoPadrao() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.preencherSenhaRepetirSenha(id, false, true);
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_FORMATODASENHAINVALIDO);
		
	}
	
	//SISCW-1174:Dados divergentes em Email e Senha (foco email)
	@Test
	public void ct_emailDiferenteDaConfirmacao() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
				
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.preencherEmailRepetirEmail("diego.quirino@tjpb.jus.br", false);
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_EMAISDIGITADOSDIFERENTES);
		
	}
	
	//SISCW-1174:Dados divergentes em Email e Senha (foco senha)
	@Test
	public void ct_senhaDiferenteDaConfirmacao() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
		
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.preencherSenhaRepetirSenha(id, false, false);
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_SENHASDIGITADASDIFERENTES);
		
	}
	
	//SISCW-1175:Status da OAB n�o permitidoID 21 SITUA��O IRREGULAR OAB
	@Test
	public void ct_oabNaoPermitidaSituacaoIrregular() throws Exception {
		int id = 21; //linha do elemento no arquivo CSV
		
		this.cadAdvogadoPO.realizarConsultaInicialOAB(id, true);
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_OAB_SITUACAOIRREGULAR);
	}
	
	//SISCW-198:CPF de advogado digitado � diferente do recuperado
	@Test
	public void ct_advogadoOABCPFDiferenteRecuperado() throws Exception {
		int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
		
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.preencherCPF(id, false, true);
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_CPFDIGITADODIFERENTE);
		
	}
	
	//SISCW-198:CPF de advogado digitado � diferente do recuperado -> CPF INV�LIDO
	@Test
	public void ct_advogadoOABCPFInvalidoDiferenteRecuperado() throws Exception {
int id = this.cadAdvogadoPO.realizarConsultaPrimeiroAcessoOAB(ultimoIdUtilizadoCSV);
		
		this.cadAdvogadoPO.inserirDadosPessoais(id, true, false, "diego.quirino@tjpb.jus.br");
		this.cadAdvogadoPO.preencherCPF(id, false, true);
		this.cadAdvogadoPO.clicarNoBotaoPesquisarEndereco();
		this.cadAdvogadoPO.inserirEndereco(id,true);
		this.cadAdvogadoPO.clicarNoBotaoIncluirEndereco();
		this.cadAdvogadoPO.clicarNoBotaoFinalizarCadastro();
		
		assertEquals(this.cadAdvogadoPO.getTextoMensagemErro(), 
				this.cadAdvogadoPO.MSG_CPFDIGITADODIFERENTE);

	}
	
}
