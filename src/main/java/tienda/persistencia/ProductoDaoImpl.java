package tienda.persistencia;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import tienda.config.Config;
import tienda.modelo.Producto;

public class ProductoDaoImpl implements ProductoDao {


	private EntityManagerFactory emf;
	
	public ProductoDaoImpl () {
		this.emf = Config.getEmf();
	}
	
	@Override
	public Producto findById(int idProducto) {
		EntityManager em = emf.createEntityManager();
		Producto producto = em.find(Producto.class, idProducto);
		em.close();
		return producto;
	}
		

	@Override
	public List<Producto> findByDescripcion(String descripcion) {
		EntityManager em = emf.createEntityManager();
		String Consultajpql="select p from Producto where p.descripcion like :desc";
		
		TypedQuery<Producto> busDesc =em.createQuery(Consultajpql,Producto.class);
		busDesc.setParameter("desc", "%"+ descripcion + "%");
		
		List<Producto> lista = busDesc.getResultList();
		em.close();
		return lista;
	}

	@Override
	public List<Producto> findAll() {
		EntityManager em = emf.createEntityManager();
		
		String Consultajpql = "select p from Producto p";
		
		TypedQuery<Producto> resProdTodos =em.createQuery(Consultajpql,Producto.class);
		List<Producto> lista = resProdTodos.getResultList();
		em.close();
	return lista;
	}

	@Override
	public void save(Producto p) {
		EntityManager em= emf.createEntityManager();
		try(em){
			em.getTransaction().begin();
			em.merge(p);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			throw new PersistenceException();
		}
		
	}

}
