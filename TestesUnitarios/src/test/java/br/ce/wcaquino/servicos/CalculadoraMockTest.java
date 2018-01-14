package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.entidades.Locacao;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;

	@Spy
	private Calculadora calcSpy;
	
	//@Spy
	//private EmailService email; // Interface
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devemostrarDiferencaEntreMockSpy() {
		when(calcMock.somar(1, 2)).thenReturn(5);// Nesse caso trabalha na camada de interface e nao executa o metodo
		
		//when(calcMock.somar(1, 2)).thenCallRealMethod();// Já aqui o Mock irá entrar na implemencao desse metodo e retornar o valor de resultado
		
		// Nesse caso o java executa em ordem 1° - calcSpy.somar(1, 2) depois executa o then
		//when(calcSpy.somar(1, 2)).thenReturn(5);
		
		// Define comportamento igual o when
		doReturn(5).when(calcSpy).somar(1, 2);
		
		doNothing().when(calcSpy).imprime();;
		
		// No mock caso ele nao saiba o que fazer irá retorna o valor padrao do mock
		/*System.out.println(calcMock.somar(1, 2));
		
		// Já o Spy caso nao saiba o que fazer irá realizar a execução real do metodo, e nao funciona com interfaces somente com classes concretas
		System.out.println(calcSpy.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();*/
	}
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		assertEquals(5, calc.somar(1, 5));
		//System.out.println(argCapt.getAllValues());
	}
}
