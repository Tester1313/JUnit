package br.ce.wcaquino.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

/* Suite de testes

 Desta maneira a cada nova classe de teste deve ser acrescentada a annotation
@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})*/

public class SuiteExcecucao {
	/*Remova se puder

	 Before e after serao executados apos a bateria de testes
	@BeforeClass
	public static void before() {
		System.out.println("before");
	}

	@AfterClass
	public static void after() {
		System.out.println("after");
	}*/
}
