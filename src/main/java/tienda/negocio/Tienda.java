package tienda.negocio;

import java.util.Set;

import tienda.modelo.Fabricante;
import tienda.modelo.Producto;

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
	 * Agrega un nuevo producto enn la persitencia
	 * @param p producto a agregar
	 */
	
	void crearProducto(Producto p);
}
