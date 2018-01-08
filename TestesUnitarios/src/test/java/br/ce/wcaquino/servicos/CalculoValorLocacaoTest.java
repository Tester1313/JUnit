package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.excetion.FilmeSemEstoqueException;
import br.ce.wcaquino.excetion.LocadoraException;

@RunWith(Parameterized.class) // Diz pro JUnit tratar o tesste de uma forma diferente
public class CalculoValorLocacaoTest {

	// DDT - Data Driven Test - Teste orientado a dados
	private LocacaoService service;

	// Notacoes que indicao a ordem do elementos do teste
	@Parameter
	public List<Filme> filmes;
	@Parameter(value = 1)
	public double valorLocacao;
	@Parameter(value = 2)
	public String cenario;

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	private static Filme filme1 = new Filme("Filme 1", 1, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 1, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 1, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 1, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 1, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 1, 4.0);
	private static Filme filme7 = new Filme("Filme 7", 1, 4.0);

	@Parameters (name="Teste{index} = {2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1,
					filme2), 8.0, "2 Filmes: Sem Desconto"},
			{Arrays.asList(filme1,
					filme2,
					filme3), 11.0, "3 Filmes: 25%"},

			{Arrays.asList(filme1,
					filme2,
					filme3,
					filme4), 13.0, "4 Filmes: 50%"},

			{Arrays.asList(filme1,
					filme2,
					filme3,
					filme4,
					filme5), 14.0, "5 Filmes: 75%"},

			{Arrays.asList(filme1,
					filme2,
					filme3,
					filme4,
					filme5,
					filme6), 14.0, "6 Filmes: 100%"},

			{Arrays.asList(filme1,
					filme2,
					filme3,
					filme4,
					filme5,
					filme6,
					filme7), 18.0, "7 Filmes: Sem Desconto"}
		});
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		//Cenario
		Usuario usuario = new Usuario("Thiago");

		//Acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		//Verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
	}

	@Test
	public void print() {
		System.out.println(valorLocacao);
	}
}
