package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.caelum.financas.exceptions.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao {
	@Inject
	EntityManager manager;
	

	public void adiciona(Movimentacao movimentacao) {
		manager.joinTransaction();
		this.manager.persist(movimentacao);
		
		movimentacao.getConta().getMovimentacoes().add(movimentacao);
		if(movimentacao.getValor().signum() == -1)
		{
			//throw new RuntimeException("Movimentação Negativa");
			throw new ValorInvalidoException("Movimentação Negativa");
		}		
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}
	
	public List<Movimentacao> getMovimentacaoByConta(Conta conta)
	{
		Query query = this.manager.createQuery("select m from "+Movimentacao.class.getSimpleName()+" m "
				+ "where m.conta = :conta"
				+ " order by m.valor desc");
		query.setParameter("conta", conta);
		return query.getResultList();
	}
	
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo)
	{
		Query query = this.manager.createQuery("select m from "+Movimentacao.class.getSimpleName()+" m where "
				+ "m.valor <= :valor and m.tipoMovimentacao = :tipo", Movimentacao.class);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		return query.getResultList();
		
	}
	
	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo)
	{
		Query query = this.manager.createQuery("select sum(m.valor) from Movimentacao m where "
				+ "m.conta = :conta and m.tipoMovimentacao =:tipo", BigDecimal.class);
		 query.setParameter("conta", conta);
		 query.setParameter("tipo", tipo);
		return (BigDecimal) query.getSingleResult();
	}
	
	public List<Movimentacao> buscaMovimentacoesDoTitular(String titular)
	{
		TypedQuery<Movimentacao> query = this.manager.createQuery("select m from Movimentacao m "
				+ "where m.conta.titular like :titular", Movimentacao.class);
		query.setParameter("titular", "%"+ titular+"%");
		return query.getResultList();
	}
	
	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo)
	{
		Query query = this.manager.createQuery("select new "+ValorPorMesEAno.class.getName()+""
				+ "(month(m.data),year(m.data),sum(m.valor))"
				+ " from Movimentacao m "
				+ "where m.conta = :conta"
				+ "	and m.tipoMovimentacao = :tipo"
				+ " group by year(m.data), month(m.data) "
				+ " order by sum(m.valor) desc", ValorPorMesEAno.class);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
	
	public List<Movimentacao> listaComCategorias()
	{
		Query query = this.manager.createQuery("select distinct m from "+Movimentacao.class.getSimpleName()+""
				+ " m left join fetch m.categorias", Movimentacao.class);
		return query.getResultList();
	}

}
