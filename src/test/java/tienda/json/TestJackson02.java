package tienda.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tienda.modelo.Producto;

public class TestJackson02 {

	//se hace esta clase para saber como se usa el jason y solo es un ejemplo
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		String json = "{\"producto\":\"Monitor 27 LED Full HD\",\"precio\":245.99,\"fabricante\":{\"idFabricante\":1,\"fabricante\":\"Asus\",\"id_fabricante\":1},\"id_producto\":7}";

		System.out.println(json);
		
		System.out.println("------------------------------------");
		
		//crear el mapa
		ObjectMapper mapper = new ObjectMapper();
		//nos va a dar error por las exception se manda hacia arria
		Producto p = mapper.readValue(json, Producto.class);
		//ahora lo pintamos
		System.out.println(p);
	}

}
