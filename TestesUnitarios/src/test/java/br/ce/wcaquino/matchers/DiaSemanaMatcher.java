package br.ce.wcaquino.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {
	
	//Valor esperado
	private Integer diaSemana;

	public DiaSemanaMatcher(Integer diaSemana) { // Receber� o diada classe Calendar
		this.diaSemana = diaSemana;
	}
	
	//Mensagem de erro no Teste
	public void describeTo(Description desc) {
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_WEEK, diaSemana);
		String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
		desc.appendText(dataExtenso);
	}

	@Override
	protected boolean matchesSafely(Date arg0) { // Data para compara��o
		return DataUtils.verificarDiaSemana(arg0, diaSemana);
	}

}
