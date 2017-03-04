package br.com.caelum.financas.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.caelum.financas.modelo.Categoria;

@Stateless
public class CategoriaDao {
	@PersistenceContext
	private EntityManager manager;
	
	public Categoria procura(Integer id)
	{
		return manager.find(Categoria.class, id);
	}
	
	public List<Categoria> lista()
	{
		Query query = manager.createQuery("select c from "+Categoria.class.getSimpleName()+" c", Categoria.class);
		return query.getResultList();
	}
}
