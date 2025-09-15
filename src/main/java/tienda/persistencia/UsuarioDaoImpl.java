package tienda.persistencia;


import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import tienda.config.Config;
import tienda.modelo.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public UsuarioDaoImpl () {
		emf = Config.getEmf();	
	}

	@Override
	public Usuario findById(int id) {
		em = emf.createEntityManager();
		Usuario buscado = em.find(Usuario.class,id);
		em.close();
		return buscado;
	}

	@Override
	public boolean save(Usuario usuario) {
		boolean ok = false;
		String pwd = BCrypt.withDefaults().hashToString(12, usuario.getPassword().toCharArray());//con esto encriptamos
		usuario.setPassword(pwd);
		
		EntityManager em = emf.createEntityManager();
		try (em) {
			em.getTransaction().begin();
			em.merge(usuario);
			em.getTransaction().commit();		
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PersistenceException();
		}
		return ok;
	}

	@Override
	public Usuario valida(String usuario, String password) {
		Usuario buscado = null;
		em = emf.createEntityManager();
		String jpql = "select u from Usuario u where u.usuario = :usr";
		TypedQuery<Usuario> q = em.createQuery(jpql,Usuario.class);
		q.setParameter("usr", usuario);
		buscado = q.getSingleResultOrNull();
		//chequeamos las claves
		if(buscado == null || !BCrypt.verifyer().verify(password.toCharArray(), buscado.getPassword()).verified) //busca la contraseña encriptada y la compara 
			buscado = null;
		
		em.close();
		return buscado;
	}

}
