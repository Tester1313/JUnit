package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.excetion.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	// TDD seria o desenvolvimento baseados em teste
	// 1° Codifica o teste
	// 2° Codifica o programa
	// 3° Refatora o codigo
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//Cenario
		int a = 5;
		int b = 3;
		
		//acao
		int resultado = calc.somar(a,b);
		
		//Verificacao
		assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//Cenario
		int a = 5;
		int b = 3;
		
		//acao
		int resultado = calc.subtrair(a, b);
		
		//Verificacao
		assertEquals(2, resultado);
	}
	
	@Test
	public void deveDividirDoisValore() throws NaoPodeDividirPorZeroException {
		//Cenario
		int a = 6;
		int b = 3;
		
		//Acao
		int resultado = calc.dividir(a ,b);
		
		//Verificacao
		assertEquals(2, resultado);
	}
	
	
	//Test expera que seja lancado a exception abaixo
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarEcecaoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//Cenario
		int a = 10;
		int b = 0;
		
		calc.dividir(a, b);
		
	}
}
