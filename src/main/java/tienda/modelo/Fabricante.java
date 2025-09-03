package tienda.modelo;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fabricantes")
public class Fabricante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fabricante")
	private int idFabricante;
	private String fabricante;
	
	@OneToMany(mappedBy = "fabricante")
	private Set<Producto> productos;
	
	
	public Fabricante () {}
	
	public Fabricante(int id_fabricante, String fabricante) {
		super();
		this.idFabricante = id_fabricante;
		this.fabricante = fabricante;
	}
	
	

	public Set<Producto> getProductos() {
		return productos;
	}

	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}

	public int getId_fabricante() {
		return idFabricante;
	}

	public void setId_fabricante(int id_fabricante) {
		this.idFabricante = id_fabricante;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idFabricante);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fabricante other = (Fabricante) obj;
		return idFabricante == other.idFabricante;
	}

	@Override
	public String toString() {
		return "Fabricante [id_fabricante=" + idFabricante + ", fabricante=" + fabricante + "]";
	}

	
	
	
	

}
