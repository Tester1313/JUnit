// Interessante criar a estrutra de pacotes, porque embora elas estejam separadas fisicamentes
// em tempo de execucao o Java entende que elas estarao no mesmo pacote, sendo assim juntas logicamente.
package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

import br.ce.wcaquino.builders.FilmeBuilder;
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
	private LocacaoService service;

	@Rule
	public ErrorCollector  error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// Caso adicionado aqui será iniciado antes dos testes abaixo
	@Before
	public void setup() {
		service = new LocacaoService();
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
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//Cenario - O que eu preciso
		Usuario usuario = umUsuario().agora();
		List <Filme> filmes = Arrays.asList(umFilme().comValor().agora());
		
		//Acao
		Locacao locacao;

		locacao = service.alugarFilme(usuario, filmes);

		//Verificação
		assertEquals(15.50, locacao.getValor(), 0.01);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

		// Assert That 
		// Verifique que o valor da locacao é igual a 15.50
		//assertThat(locacao.getValor(), is(equalTo(15.50))); 

		// Verifique que o valor da locacao não é igual a 6
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
	public void DeveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		//Garante que o teste será executado somente aos sabados
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
}
