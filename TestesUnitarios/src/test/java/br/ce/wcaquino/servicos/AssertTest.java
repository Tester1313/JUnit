package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void test(){
		Assert.assertTrue(true); //Verifica verdadeiro
		Assert.assertFalse(false); //Verifica falso
		
		Assert.assertEquals("Erro de comparação", 1, 1);
		Assert.assertEquals(0.51, 0.51, 0.01); // 0.01 é a margem de erro
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i = 5;
		Integer i2 = 5;
		// Duas maneiras para realizar a comparação
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i ,  i2.intValue());
		
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "Bola"); // Equals negado
		
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		// Comparação de objetos
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		
		//Neste caso foi implementado o metodo equal no objeto usuario para rodar
		Assert.assertEquals(u1, u2); 
		
		Assert.assertSame(u2, u2); // Compara as intancias do objeto
		Assert.assertNotSame(u2, u1); // Same negado
		
		Assert.assertNull(u3);
		Assert.assertNotNull(u1); // Null negado
	}
}
