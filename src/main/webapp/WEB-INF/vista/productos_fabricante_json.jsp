<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Productos</title>
<link rel="stylesheet" type="text/css" href="${css}/productos.css">
<script type="text/javascript">
//declaramos funciones globales 
let tabla;
function solicitud(){
	//Preparar los parametros para la peticion Ej:idFabricante=5
	let param = "idFabricante="+ encodeURIComponent(document.getElementById("idFabricante").value);
	
	//Creamos el objeto que nos permita hacer la peticion
	let req = new XMLHttpRequest();
	
	//Indicamos el método Http y la URI
	req.open("post", "productos_fabricante_json_respuesta");
	
	//Registrar el oyente al evento readystatechange (cambio de estado - cuando reciba respuesta)
	req.addEventListener("readystatechange", 
		function(){
			if(req.readyState == 4 && req.status == 200){
				cargarTabla(req);
			}
	});
	//Armar la cabecera 
	req.setRequestHeader("content-type","application/x-www-form-urlencoded");
	
	//Realizar la petición
	req.send(param);
	
	//ya terminamos ajax y ahora vamos a crear la funcion crear tabla 
}

//hacemos la funcion crear tabla
function cargarTabla(req){
	//console.log(req.responseText);//probamos ajax
	let productos = JSON.parse(req.responseText);//lo convertimos en objeto javaScript, ya tenemos el array de productos
	//console.log(productos); probamos que nos funcione 
	tabla.innerHTML ="";//con esto lo acabamos de limpiar y el contenido del body se quedo en blanco
	for(let i = 0; i < productos.length; i++){
		insertarFila(productos[i]);//creamos la funcion insertarFila
	}
	document.querySelector("#tabla_datos").style.visibility = "visible";
}
function insertarFila(producto){
	let tr = document.createElement("tr");
	let td = document.createElement("td");
	tr.appendChild(td); //que el td sea hijo del tr
	td.textContent=producto.producto; //producto (objeto) producto(definicion)
	
	td = document.createElement("td");
	tr.appendChild(td);	
	td.textContent=producto.precio;
	
	tabla.appendChild(tr);
}

window.onload = function(){
	document.getElementById("idFabricante").addEventListener("change",solicitud);
	tabla = document.querySelector("#tabla_datos tbody"); //usa un selector css para hacerse con elemento del dom 
}
</script>
<style>
#tabla_datos{
visibility: hidden;
}
</style>
</head>
<body>
	<header class="cabecera">
	<h2>Busqueda de Productos por Fabricante</h2>
	</header>
	
	<div id="contPrincipal">
		<form action="${home}/productos_fabricante" method= "post">
			<select id="idFabricante" name="idFabricante">
				<option hidden="hidden" value="">Seleccion fabricante</option>
				<c:forEach var="fabricante" items="${fabs}">
					<option value="${fabricante.idFabricante}">${fabricante.fabricante}</option>
				</c:forEach>
			</select>
		</form>
		<table id="tabla_datos">
			<thead>
				<tr>
					<th>Descripcion</th>
					<th>Precio</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	<a href="${home}/menu_principal"><button>Volver</button></a>
	</div>
</body>
</html>