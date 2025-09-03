package tienda.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Config {
	private static EntityManagerFactory emf;
	private static DataSource ds; 
	private static Properties prop;
	
	private Config() {}
	
	public static EntityManagerFactory getEmf() {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("Tienda");
		}
		return emf;
	}
	
	public static DataSource getDataSource() { //patron singleton 
		if(ds == null) {
			//neceistamos trabajar contra el fichero
			BasicDataSource bds = new BasicDataSource();
			Properties prop = getProp(); //nuestro objeto prop
			bds.setUrl(prop.getProperty("bbdd.url"));
			bds.setDriverClassName(prop.getProperty("bbdd.driver"));
			bds.setUsername(prop.getProperty("bbdd.user"));
			bds.setPassword(prop.getProperty("bbdd.pass"));
			ds = bds;
		}
		return ds;//referencia al mismo objeto 
	}
	
	
	//la clase properties es un mapa 
	public static Properties getProp(){ //clase preparada para ir a leer nuestro fichero de propertis y cuabdo le pidamos la clave nos la da, o lo que tengamos escrito el lo vera 
		if(prop == null) {
			prop= new Properties();
			try(FileReader fr = new FileReader("app.properties")){
				prop.load(fr);
			}catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException("No se puede leer el fichero de properetis");
			}
		}
		return prop;
	}

}
