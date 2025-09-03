package tienda.persistencia;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Producto> findAll() {
		// TODO Auto-generated method stub
		return null;
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
