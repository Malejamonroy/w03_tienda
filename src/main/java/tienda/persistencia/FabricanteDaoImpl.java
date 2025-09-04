package tienda.persistencia;

import java.util.HashSet;

import java.util.Set;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import tienda.config.Config;
import tienda.modelo.Fabricante;

public class FabricanteDaoImpl implements FabricanteDao {
	
	private EntityManagerFactory emf;
	public FabricanteDaoImpl () {
		emf= Config.getEmf();
	}
	
	@Override
	public void save(Fabricante fabricante) {
		EntityManager em= emf.createEntityManager();
		try(em){
			em.getTransaction().begin();
			em.merge(fabricante);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			throw new PersistenceException();
		}
		
	}
		
	@Override
	public Fabricante findByIdLazy(int idFabricante) {
		EntityManager em = emf.createEntityManager();
		Fabricante fabricante = em.find(Fabricante.class, idFabricante);
		em.close();
		return fabricante;
	}
	@Override
	public Fabricante findById(int idFabricante) {
		EntityManager em = emf.createEntityManager();
		//select fabricante from fabricantes left join productos on fk_fabricante =  id_fabricante where id_fabricante = 4
		String consultajpql="select f from Fabricante f left join fetch f.productos where f.idFabricante = :id";
		TypedQuery<Fabricante> busFabId = em.createQuery(consultajpql,Fabricante.class);
		busFabId.setParameter("id", idFabricante);
		Fabricante resul = busFabId.getSingleResultOrNull();
		em.close();
		return resul;
	
	}
	@Override
	public Set<Fabricante> findOnlyActive() {
		EntityManager em= emf.createEntityManager();
		//select distinct fabricante,producto from fabricantes join productos on fk_fabricante = id_fabricante
		String consultajpql = "Select distinct f from Fabricante f join f.productos";
		TypedQuery<Fabricante> busFab = em.createQuery(consultajpql,Fabricante.class);
		Set<Fabricante> resuFab = new HashSet<Fabricante>(busFab.getResultList());
		em.close();
		return resuFab;
	}
	@Override
	public Set<Fabricante> findAll() {
	   EntityManager em= emf.createEntityManager();
	   //select distinct fabricante,producto from fabricantes
		String consultajpql = "Select f from Fabricante f ";
		TypedQuery<Fabricante> resConsultajpql= em.createQuery(consultajpql,Fabricante.class);
		Set<Fabricante> buscarTodosFabr = new HashSet<Fabricante>(resConsultajpql.getResultList());
		em.close();
		return buscarTodosFabr;
	}
	

}
