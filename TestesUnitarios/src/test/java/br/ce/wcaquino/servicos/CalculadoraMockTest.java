package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.ce.wcaquino.entidades.Locacao;

public class CalculadoraMockTest {

	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		assertEquals(5, calc.somar(1, 5));
		System.out.println(argCapt.getAllValues());
	}
}
