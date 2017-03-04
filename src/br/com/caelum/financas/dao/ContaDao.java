package br.com.caelum.financas.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import br.com.caelum.financas.modelo.Conta;

@Stateless
//@TransactionManagement(TransactionManagementType.BEAN)
public class ContaDao {
	@PersistenceContext
	EntityManager manager;
	//@Resource
	//UserTransaction ut;
	
	//@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void adiciona(Conta conta) {
		/*try {
			ut.begin();
			this.manager.persist(conta);
			ut.commit();
		} catch (Exception e) {
			throw new EJBException(e);
		}*/
		this.manager.persist(conta);
	}

	public Conta busca(Integer id) {
		return this.manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		return this.manager.createQuery("select c from Conta c", Conta.class)
				.getResultList();
	}

	public void remove(Conta conta) {
		Conta contaParaRemover = this.manager.find(Conta.class, conta.getId());
		this.manager.remove(contaParaRemover);
	}
	
	public Conta altera(Conta conta)
	{
		return this.manager.merge(conta);
	}

}




