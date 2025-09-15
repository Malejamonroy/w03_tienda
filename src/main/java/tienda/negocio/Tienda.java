package tienda.negocio;

import java.util.Set;

import tienda.modelo.Fabricante;
import tienda.modelo.Producto;
import tienda.modelo.Usuario;

public interface Tienda {
	/*
	 * Devuelve todos los productos ordenados por su descripcion
	 * @return Un Set de productos ordenados
	 * */
	Set<Producto> geProductos();

	/*
	 * Devuevle los productos que contienen descripcion ordenados por su descripcion
	 * @param descripcion Descripcion de los productos a buscar
	 * @return Un Set de productos ordenados
	 * */
	Set<Producto> getProductos(String descripcion);
	
	/**
	 * Devuelve todos los fabricantes ordenados por su nombre 
	 * @return Set de fabricantes ordenados
	 */

	Set<Fabricante> getFabricantes();
	
	
	/**
	 * Retorna el fabricante buscado
	 * @param idFabricante id del fabricante
	 * @return el fabricante si existe, null si no existe
	 */
	Fabricante getFabricante (int idFabricante);
	
	/**
	 * Devuelve todos los fabricantes que proporcionan productos. ordenados por su nombre
	 * @return Set de fabricantes ordenados.
	 */
	Set<Fabricante> getFabricantesActivos();
	
	/**
	 * Agrega un nuevo producto enn la persitencia
	 * @param p producto a agregar
	 */
	
	void crearProducto(Producto p);
	
	/**
	 * Agrega un nuevo Usuario
	 * @param u usuario a agregar
	 * @return
	 */
	boolean crearUsuario(Usuario u);
	
	
	/**
	 * Valida las credenciales de un login
	 * @param usr nombre de usuario
	 * @param pws password
	 * @return el usuario si las credenciales son correctas o null si no lo son
	 */
	Usuario validaUsuario(String usr, String pws);
}
