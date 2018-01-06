package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

// DEFINE ORDEM DOS TESTE
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Executa teste em ordem alfabetica

public class OrdemTest {
	
	public static int contador = 0;
	
	@Test
	public void inicia() {
		contador = 1;
	}
	
	@Test
	public void verifica() {
		assertEquals(1, contador);
	}
}
