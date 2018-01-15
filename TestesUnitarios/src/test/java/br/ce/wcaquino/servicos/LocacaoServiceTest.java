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
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.excetion.FilmeSemEstoqueException;
import br.ce.wcaquino.excetion.LocadoraException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class })
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

	// Caso adicionado aqui será iniciado antes dos testes abaixo
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
	}

	//Caso adicionado aqui será executado apos os testes abaixo
	/*@After
	public void teardown() {
		System.out.println("After");
	}


	// Caso adicionado aqui será iniciado antes da classes
	@BeforeClass
	public static void setupClass() {
		System.out.println("BeforeClass");
	}

	//Caso adicionado aqui será executado apos a classes
	@AfterClass
	public static void teardownClass() {
		System.out.println("AfterClass");
	}*/

	@Test
	public void deveAlugarFilme() throws Exception{
		//Garante que o teste nao será executado somente aos sabados
		//Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//Cenario - O que eu preciso
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilme().comValor().agora());

		//werMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 28);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//Acao
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		//Verificação

		// Executa todos os teste, mesmo que de falha no primeiro
		error.checkThat(locacao.getValor(), is(15.50));
		//error.checkThat(locacao.getDataLocacao(), ehHoje());
		//error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(28, 4, 2017)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(29, 4, 2017)), is(true));
	}

	// 3 Formas para tratamento de exceções

	//Forma Elegante
	@Test(expected = FilmeSemEstoqueException.class) // Informa o teste que é esperado uma excecao
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

	@Test // Teste de verificação de usuario
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
	//@Ignore Notação pula o teste abaixo
	public void DeveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		//Garante que o teste será executado somente aos sabados
		//Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//Cenario
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilme().agora());
		
		
		// Nesse ponto esta Mockando o Construtor do Date que nao tem nenhum paramentro
		// Traducao: Quando eu solicitar uma nova instancia da classe date sem nenhum argumento
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 29);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//Acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		//verificacao
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		
		//Verificando se o construtor mockado foi chamado
		//PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
		
		//Verificar se o metodo statico foi chamado
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
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

		// Resposta padrao do mock boolean é falso
		// o when é utilizado para alterar esse valor padrao

		// Quando spc.possuiNegativacao(usuario) entao retorne true
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		//acao
		try {
			service.alugarFilme(usuario, filmes);
			//Verificação
			Assert.fail(); // Garate que caso a exception nao seja lançada o retorno nao seja um falso positivo
		} catch (LocadoraException e) {
			assertThat(e.getMessage(),is("Usuário Negativado"));
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
				//Verifique que serao realizados 2 execuções de email ao metodo de notificar atraso
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
	
	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		//Cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//Mockando Metodo privado -  Onde os filmes sao parametros, service é a instancia da classe e 
		// string é o nome do metodo privado que desejamos mockar
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		//Acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		//verificacao
		Assert.assertThat(locacao.getValor(), is(1.0));
		
		//Verificando se o metodo foi chamado
		PowerMockito.verifyPrivate(service).invoke( "calcularValorLocacao", filmes);
		
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//Acao
		// WhiteBox consegue invocar metodos privador diretamente
		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		//Verificacao
		Assert.assertThat(valor,is(4.0));
	}
}
