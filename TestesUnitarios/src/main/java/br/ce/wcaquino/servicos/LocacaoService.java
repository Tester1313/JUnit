package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.excetion.FilmeSemEstoqueException;
import br.ce.wcaquino.excetion.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuario Vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme Vazio");
		}
		
		
		for( Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
		boolean negativado;
		try {
			 negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}
		
		if(negativado) {
			throw new LocadoraException("Usuário Negativado");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(Calendar.getInstance().getTime());
		
		locacao.setValor(calcularValorLocacao(filmes));

		//Entrega no dia seguinte
		Date dataEntrega = Calendar.getInstance().getTime();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		
		return locacao;
	}

	private Double calcularValorLocacao(List<Filme> filmes) {
		//System.out.println("calcularValorLocacao");
		Double valorTotal = 0d;
		for(int i =0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			
			switch (i) {
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.5; break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = 0d; break;
				//default: valorFilme;
			}
			
			valorTotal += valorFilme;
		}
		return valorTotal;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.ObterLocacoesPendentes();
		for(Locacao locacao: locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}			
		}
	}
	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		
		dao.salvar(novaLocacao);
	}
	/*public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	public void setSPCService(SPCService spc) {
		spcService = spc;
	}
	
	public void setEmailService(EmailService email) {
		emailService = email;
	}*/
}