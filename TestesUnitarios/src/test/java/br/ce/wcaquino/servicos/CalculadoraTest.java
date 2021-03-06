package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.ce.wcaquino.excetion.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;

public class CalculadoraTest {

	// TDD seria o desenvolvimento baseados em teste
	// 1� Codifica o teste
	// 2� Codifica o programa
	// 3� Refatora o codigo
	
	public static StringBuffer ordem = new StringBuffer();
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando");
		ordem.append("1");
	}
	
	@After
	public void tearDown() {
		System.out.println("finalizando");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(ordem.toString());
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
	
	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";
		
		int resultado = calc.divide(a, b);
		
		assertEquals(2, resultado);
	}
	
	/*public static void main(String[] args) {
		new Calculadora().divide("5", "0");
	}*/
}
