// Interessante criar a estrutra de pacotes, porque embora elas estejam separadas fisicamentes
// em tempo de execucao o Java entende que elas estarao no mesmo pacote, sendo assim juntas logicamente.
package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.excetion.FilmeSemEstoqueException;
import br.ce.wcaquino.excetion.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

public class LocacaoServiceTest {

	//Quase todos os teste utilizam essa instancia entao ela se tornou
	// global e se encontra no Before
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	
	@Mock
	private LocacaoDAO  dao;
	
	@Mock
	private EmailService email;

	@Rule
	public ErrorCollector  error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// Caso adicionado aqui ser� iniciado antes dos testes abaixo
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		/*service = new LocacaoService();

		dao = mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);

		spc = mock(SPCService.class);
		service.setSPCService(spc);

		email = mock(EmailService.class);
		service.setEmailService(email);*/
	}

	//Caso adicionado aqui ser� executado apos os testes abaixo
	/*@After
	public void teardown() {
		System.out.println("After");
	}


	// Caso adicionado aqui ser� iniciado antes da classes
	@BeforeClass
	public static void setupClass() {
		System.out.println("BeforeClass");
	}

	//Caso adicionado aqui ser� executado apos a classes
	@AfterClass
	public static void teardownClass() {
		System.out.println("AfterClass");
	}*/

	@Test
	public void deveAlugarFilme() throws Exception{
		//Garante que o teste nao ser� executado somente aos sabados
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//Cenario - O que eu preciso
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilme().comValor().agora());

		//Acao
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		//Verifica��o
		assertEquals(15.50, locacao.getValor(), 0.01);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

		// Assert That 
		// Verifique que o valor da locacao � igual a 15.50
		//assertThat(locacao.getValor(), is(equalTo(15.50))); 

		// Verifique que o valor da locacao n�o � igual a 6
		//assertThat(locacao.getValor(), is(not(6.0)));


		//assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		//assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

		// Executa todos os teste, mesmo que de falha no primeiro
		//error.checkThat(locacao.getValor(), is(15.50));
		//error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		//error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
	}

	// 3 Formas para tratamento de exce��es

	//Forma Elegante
	@Test(expected = FilmeSemEstoqueException.class) // Informa o teste que � esperado uma excecao
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//Cenario - O que eu preciso
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
		//filmes.add(filme2);

		//Acao
		service.alugarFilme(usuario, filmes);

		//System.out.println("Forma elegante");
	}

	//Mais robusta
	//Vantagem em cima da elegante eu consigo capturar e exception e exibir a mensagem dela
	/*@Test
	public void testLocacao_filmeSemEstoque2() {
		//Cenario - O que eu preciso
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		Filme filme = new Filme("Percy Jackson", 2, 15.50);

		//Acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado uma exception");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	//Forma nova
	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		//Cenario - O que eu preciso
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		Filme filme = new Filme("Percy Jackson", 0, 15.50);

		exception.expect(Exception.class); // Esperado que um exception seja lancado
		exception.expectMessage("Filme sem estoque");

		//Acao
		service.alugarFilme(usuario, filme);

	}*/

	@Test // Teste de verifica��o de usuario
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//Cenario
		List <Filme> filmes = Arrays.asList(umFilme().agora());

		//Acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario Vazio"));
		}

		//System.out.println("Forma robusta");
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");

		//Acao
		service.alugarFilme(usuario, null);

		//System.out.println("Forma nova");
	}

	/*@Test -- Teste foi refatorado e colocado na classe calculoValorLocacaotest
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = new Usuario("Thiago");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
											new Filme("Filme 2", 1, 4.0),
											new Filme("Filme 3", 1, 4.0));

		//Acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificacao
		assertThat(resultado.getValor(), is(11.0));
	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = new Usuario("Thiago");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
											new Filme("Filme 2", 1, 4.0),
											new Filme("Filme 3", 1, 4.0),
											new Filme("Filme 4", 1, 4.0));

		//Acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificacao
		assertThat(resultado.getValor(), is(13.0));
	}

	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = new Usuario("Thiago");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
											new Filme("Filme 2", 1, 4.0),
											new Filme("Filme 3", 1, 4.0),
											new Filme("Filme 4", 1, 4.0),
											new Filme("Filme 5", 1, 4.0));

		//Acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificacao
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = new Usuario("Thiago");
		List <Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
											new Filme("Filme 2", 1, 4.0),
											new Filme("Filme 3", 1, 4.0),
											new Filme("Filme 4", 1, 4.0),
											new Filme("Filme 5", 1, 4.0),
											new Filme("Filme 6", 1, 4.0));

		//Acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificacao
		assertThat(resultado.getValor(), is(14.0));
	}*/

	@Test
	//@Ignore Nota��o pula o teste abaixo
	public void DeveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		//Garante que o teste ser� executado somente aos sabados
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//Cenario
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilme().agora());

		//Acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		//verificacao
		//boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		//assertTrue(ehSegunda);
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}

	/*public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}*/

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// Resposta padrao do mock boolean � falso
		// o when � utilizado para alterar esse valor padrao

		// Quando spc.possuiNegativacao(usuario) entao retorne true
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		//acao
		try {
			service.alugarFilme(usuario, filmes);
			//Verifica��o
			Assert.fail(); // Garate que caso a exception nao seja lan�ada o retorno nao seja um falso positivo
		} catch (LocadoraException e) {
			assertThat(e.getMessage(),is("Usu�rio Negativado"));
		}

		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().comNome("Teste").agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = asList(
				umLocacao().atrasado().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasado().comUsuario(usuario3).agora(),
				umLocacao().atrasado().comUsuario(usuario3).agora());;

				//Quando dao.ObterLocacoesPendentes() entao retorne a lista locacoes
				when(dao.ObterLocacoesPendentes()).thenReturn(locacoes);

				//acao
				service.notificarAtrasos();

				//verificacao
				//Verifique que serao realizados 2 execu��es de email ao metodo de notificar atraso
				//passando como parametro qualquer usuario
				verify(email, times(3)).notificarAtraso(Mockito.any(Usuario.class));
				verify(email).notificarAtraso(usuario);
				verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3); // Times diz q teremos 2invocacoes
				verify(email, never()).notificarAtraso(usuario2);
				verifyNoMoreInteractions(email);
	}
	
	@Test
	public void deveTratarErronoSPC() throws Exception {
		//Cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");
		
		//Acao
		service.alugarFilme(usuario, filmes);
		
		//Verificacao
		
	}
	
	@Test
	public void deveProrrogarumaLocacao() {
		//Cenario
		Locacao locacao = umLocacao().agora();
		
		//Acao
		service.prorrogarLocacao(locacao, 3);
		
		//Verificacao
		// argumento que ira capturar parameros
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argCapt.capture()); // Captura o objeto que foi enviado para o salvar
		Locacao locacaoRetorno = argCapt.getValue(); // Retorna os valores capturados
		
		error.checkThat(locacaoRetorno.getValor(), is(12.0));
		error.checkThat(locacaoRetorno.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
}
