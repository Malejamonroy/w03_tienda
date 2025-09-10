package tienda.negocio;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import tienda.modelo.Fabricante;
import tienda.modelo.Producto;
import tienda.persistencia.FabricanteDao;
import tienda.persistencia.FabricanteDaoImpl;
import tienda.persistencia.ProductoDao;
import tienda.persistencia.ProductoDaoImpl;

public class TiendaImple implements Tienda {
	
	private ProductoDao pDao;
	private FabricanteDao fDao;
	
	public TiendaImple() {
		pDao = new ProductoDaoImpl();
		fDao = new FabricanteDaoImpl();
	}

	@Override
	public Set<Producto> geProductos() {
		Set<Producto> resu = new TreeSet<Producto>(getComparatorProductoDesc());
		resu.addAll(pDao.findAll());
		return resu;
	}

	@Override
	public Set<Producto> getProductos(String descripcion) {
		Set<Producto> resu = new TreeSet<Producto>(getComparatorProductoDescLambda());
		resu.addAll(pDao.findByDescripcion(descripcion));
		return resu;
	}
	
	@Override
	public Fabricante getFabricante(int idFabricante) {
		return fDao.findById(idFabricante);
	}
	
	@Override
	public void crearProducto(Producto p) {
		pDao.save(p);		
	}
	
	private Comparator<Producto> getComparatorProductoDesc(){
		//tenemos que hacer un compareitor para que compare las descripcion y asi usarlo en los dos metodos de arriba para que no tengamos que escribir dos exprecionse lamdan en las dos sino que llamamos este y ya esta 
		//primero lo vamos hacer con una  Anonima que implemente compareitor
		return new Comparator<Producto>() {

			@Override
			public int compare(Producto o1, Producto o2) {
				// como lo vamos hacer con el español creamos un collator
				Collator col = Collator.getInstance(new Locale("es"));
				return col.compare(o1.getProducto(), o2.getProducto());
			}
			
		};
	}
	private Comparator<Producto> getComparatorProductoDescLambda(){
	//asi se hace en una linea sin {} y sin return
		//Collator col = Collator.getInstance(new Locale("es"));
		//return col.compare(o1.getProducto(),o2.getProducto());
		
		return (o1,o2) ->{
			Collator col = Collator.getInstance(new Locale("es"));
			return col.compare(o1.getProducto(),o2.getProducto());
		};
	}
	
	//no se esta utilizando es solo un ejemplo la del id 
	private Comparator<Producto> getComparatorProductoIdLambda(){
		//como es una sola linea no se pone ni llaves ni retour
		return (p1,p2) -> p1.getId_producto() - p2.getId_producto();
	}

	@Override
	public Set<Fabricante> getFabricantes() {
		Set<Fabricante> resu = new TreeSet<Fabricante>(getComparatorFabricanteDesc());
		resu.addAll(fDao.findAll());
		return resu;
	}
	
	private Comparator<Fabricante> getComparatorFabricanteDesc(){
		return new Comparator<Fabricante>() {

			@Override
			public int compare(Fabricante f1, Fabricante f2) {
				// como lo vamos hacer con el español creamos un collator
				Collator col = Collator.getInstance(new Locale("es"));
				return col.compare(f1.getFabricante(), f2.getFabricante());
			}
			
		};
	}
	private Comparator<Fabricante> getComparatorFabricanteDescLambda(){
			return (f1,f2) ->{
			Collator col = Collator.getInstance(new Locale("es"));
			return col.compare(f1.getFabricante(),f2.getFabricante());
		};
	}
}