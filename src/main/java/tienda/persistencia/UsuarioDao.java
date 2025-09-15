package tienda.persistencia;

import tienda.modelo.Usuario;

public interface UsuarioDao {

	Usuario findById(int id);
	
	boolean save(Usuario usuario);
	
	Usuario valida(String usuario, String password);
}
