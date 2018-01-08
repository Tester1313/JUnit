package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataHojeMatchers ehHojeComDiferencaDias(Integer dias) {
		return new DataHojeMatchers(dias);
	}
	
	public static DataHojeMatchers ehHoje() {
		return new DataHojeMatchers(0);
	}
}
