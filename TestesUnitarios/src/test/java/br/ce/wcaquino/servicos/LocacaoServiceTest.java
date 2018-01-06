// Interessante criar a estrutra de pacotes, porque embora elas estejam separadas fisicamentes
// em tempo de execucao o Java entende que elas estarao no mesmo pacote, sendo assim juntas logicamente.
package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.excetion.FilmeSemEstoqueException;
import br.ce.wcaquino.excetion.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector  error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testeLocacao() throws Exception{
		
		//Cenario - O que eu preciso
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		Filme filme = new Filme("Percy Jackson", 2, 15.50);
				
		//Acao
		Locacao locacao;
		
			locacao = service.alugarFilme(usuario, filme);
			
			//Verificação
			assertEquals(15.50, locacao.getValor(), 0.01);
			assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
			assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
			
			// Assert That 
			// Verifique que o valor da locacao é igual a 15.50
			assertThat(locacao.getValor(), is(equalTo(15.50))); 
			
			// Verifique que o valor da locacao não é igual a 6
			assertThat(locacao.getValor(), is(not(6.0)));
			
			
			assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
			
			// Executa todos os teste, mesmo que de falha no primeiro
			error.checkThat(locacao.getValor(), is(15.50));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	
	}
	
	// 3 Formas para tratamento de exceções
	
	//Forma Elegante
	@Test(expected = FilmeSemEstoqueException.class) // Informa o teste que é esperado uma excecao
	public void testLocacao_filmeSemEstoque() throws Exception {
		//Cenario - O que eu preciso
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		Filme filme = new Filme("Percy Jackson", 0, 15.50);
						
		//Acao
		service.alugarFilme(usuario, filme);
		
		System.out.println("Forma elegante");
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
	public void testLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
		//Cenario
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Percy Jackson", 2, 15.50);
		
			//Acao
			try {
				service.alugarFilme(null, filme);
				Assert.fail();
			} catch (LocadoraException e) {
				assertThat(e.getMessage(), is("Usuario Vazio"));
			}
			
			System.out.println("Forma robusta");
	}
	
	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio");
		
		//Acao
		service.alugarFilme(usuario, null);
		
		System.out.println("Forma robusta");
	}
}
