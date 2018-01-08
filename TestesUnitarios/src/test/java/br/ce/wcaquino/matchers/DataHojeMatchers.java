package br.ce.wcaquino.matchers;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

// vem do AssumeThat <Date>
public class DataHojeMatchers extends TypeSafeMatcher<Date> {

	//qtdDias vem do metodo que esta em matches proprios
	private Integer qtdDias;
	
	public DataHojeMatchers(int qtdDias) {
		this.qtdDias = qtdDias;
	}
	
	public void describeTo(Description description) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean matchesSafely(Date data) {
		return isMesmaData(data, obterDataComDiferencaDias(qtdDias));
	}

}
