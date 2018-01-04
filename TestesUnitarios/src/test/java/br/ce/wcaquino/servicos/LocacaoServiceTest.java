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

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector  error = new ErrorCollector();
	
	@Test
	public void testeLocacao(){
		
		//Cenario - O que eu preciso
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Thiago");
		Filme filme = new Filme("Percy Jackson", 2, 15.50);
				
		//Acao
		Locacao locacao = service.alugarFilme(usuario, filme);
				
		//Verificação
		assertEquals(15.50, locacao.getValor(), 0.01);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
		
		// Assert That 
		// Verifique que o valor da locacao é igual a 15.50
		assertThat(locacao.getValor(), is(equalTo(15.50))); 
		
		// Verifique que o valor da locacao não é igual a 6
		assertThat(locacao.getValor(), is(not(6)));
		
		
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
		// Executa todos os teste, mesmo que de falha no primeiro
		error.checkThat(locacao.getValor(), is(15.50));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}
}
