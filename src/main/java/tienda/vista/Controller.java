package tienda.vista;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import tienda.modelo.Fabricante;
import tienda.modelo.Producto;
import tienda.modelo.Usuario;
import tienda.negocio.Tienda;
import tienda.negocio.TiendaImple;

@WebServlet("/tienda/*")
//@WebServlet("/home/*")
public class Controller extends HttpServlet {
	
	//1.para implemementar el negocio definirlo
	private Tienda neg;
	private String home;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//System.out.println("req.getCharacterEncoding()"+req.getCharacterEncoding());//como viene codificado la peticion (codificacion de caracteres)UTF-8
		//System.out.println("req.getContentType()"+req.getContentType()); //null
		//System.out.println("req.getContextPath()"+req.getContextPath()); //raiz de nuestro sitio  /w03_tienda
		//System.out.println("req.getLocalAddr()"+req.getLocalAddr()); //cual es la ip del servidor  127.0.0.1
		//System.out.println("req.getLocalPort()"+req.getLocalPort()); //el puerto en donde estoy escuchando 8080
		//System.out.println("req.getMethod()"+req.getMethod()); //el metodo que estamos usando  GET
		//System.out.println("req.getProtocol()"+req.getProtocol()); // el protocolo  HTTP/1.1
		//System.out.println("req.getRemoteAddr()"+req.getRemoteAddr()); //ip de origen 127.0.0.1
		//System.out.println("req.getLocalAddr()"+req.getLocalAddr()); //mi url127.0.0.1
		//System.out.println("req.getRequestURI()"+req.getRequestURI()); //toda la direccion que ha pedido (que uri me han pedido)/w03_tienda/tienda/algo
		//System.out.println("req.getPathInfo()"+req.getPathInfo()); //aparece solo lo que nos han pedido  /algo

		
		
		String path =req.getPathInfo();
		
		HttpSession sesion = req.getSession();
		req.setCharacterEncoding("UTF-8");
		
		if(sesionIniciada(sesion)) {
			//lo creamos aqui porque lo vamos a usar en varios case y ya queda solo llamarlo 
			Set<Fabricante> fabs;
			
			switch (path) {
			case "/informacion": 
				req.setAttribute("origen", " El que te envio esto fui yo , el controlador!!!!!");
				req.getRequestDispatcher("/WEB-INF/informacion").forward(req, resp);
				break;
			case "/menu_principal":
				eliminarDatosSesion(sesion);
				req.getRequestDispatcher("/WEB-INF/vista/menu_principal.jsp").forward(req, resp);
				break;
			case "/listado_productos":
				req.getRequestDispatcher("/WEB-INF/vista/listado_productos.jsp").forward(req, resp);
				break;
			case "/alta_producto":
				fabs= neg.getFabricantes();
				req.setAttribute("fabs", fabs);
				req.getRequestDispatcher("/WEB-INF/vista/alta_producto.jsp").forward(req, resp);
				break;
			case "/alta_producto_ok":
				req.getRequestDispatcher("/WEB-INF/vista/alta_producto_ok.jsp").forward(req, resp);
				break;
			case "/alta_producto_error":
				req.getRequestDispatcher("/WEB-INF/vista/alta_producto_error.jsp").forward(req, resp);
				break;
			case "/productos_fabricante":
				//cargar los fabricantes activos
				fabs= neg.getFabricantesActivos();
				req.setAttribute("fabs", fabs);
				req.getRequestDispatcher("/WEB-INF/vista/productos_fabricante.jsp").forward(req, resp);
				break;
			case "/productos_fabricante_json":
				fabs= neg.getFabricantesActivos();
				req.setAttribute("fabs", fabs);
				req.getRequestDispatcher("/WEB-INF/vista/productos_fabricante_json.jsp").forward(req, resp);
				break;
			case "/cerrar_sesion": //hace lo mismo que el defaul porque no se le pone break.y como queremos que haga eso por eso creamos este caso 
			default:
				login(req, resp);//si me entra por otra url me lleva al login 
			}
		}else {
			switch (path) {
			case "/login": 
				req.getRequestDispatcher("/WEB-INF/vista/login.jsp").forward(req, resp);
				break;
			case "/registro_usuarios":
				req.getRequestDispatcher("/WEB-INF/vista/registro_usuarios.jsp").forward(req, resp);
				break;
		
			case "/registro_usuarios_respuesta":
				req.getRequestDispatcher("/WEB-INF/vista/registro_usuarios_respuesta.jsp").forward(req, resp);
				break;
			default:
				resp.setContentType("text/html; charset=UTF-8");
				login(req, resp);//si me entra por otra url me lleva al login 
			}
		}
	}
	//portal de enttrada del dopost para que me devuelva lo del negocio
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path =req.getPathInfo();
		
		//hacemos una sesion
		HttpSession sesion = req.getSession();
		
		//que funciones las url con una sesion iniciada (separar la parte publica de la privada  de una manera)
		//las cosas que nos muestre mientras este iniciado
		if(sesionIniciada(sesion)) {
			//declaramos referencias
			String descripcion;
			String idFabStr;
			Fabricante fab;;
			switch (path) {
			case "/listado_productos":
				 descripcion = req.getParameter("descripcion");
				//creamos un set de productos
				Set<Producto> prods;
				//para validar que no venga null
				if(descripcion != null && descripcion.length() > 0) {
					prods = neg.getProductos(descripcion);
				}else {
					prods = neg.geProductos();
				}
				req.setAttribute("prods", prods);
				req.getRequestDispatcher("/WEB-INF/vista/listado_productos.jsp").forward(req, resp);
				break;
			case "/alta_producto":
				descripcion = req.getParameter("descripcion");
				String precioStr = req.getParameter("precio");
				idFabStr = req.getParameter("idFabricante");
				double precio;
				//comprobar que no este vacio 
				if(!isEmpty(descripcion) //comprobar que no este vacio
					&& !isEmpty(precioStr)//comprobar que no este vacio
					&& !isEmpty(idFabStr) //comprobar que no este vacio
					&& isDouble(precioStr) //si es entero 
					&& isInteger(idFabStr) //si es entero el id de fabricante
					&& (precio = Double.parseDouble(precioStr)) > 0  //que sea mayor que 0
					&& (fab = neg.getFabricante(Integer.parseInt(idFabStr))) !=null) { //que no sea null
					sesion.setAttribute("producto", descripcion);
					try {
						neg.crearProducto(new Producto(descripcion,precio,fab));
						//hacemos una redireccion a alta producto ok y reemplazamos la linea de abajo, responde a la peticion 
						resp.sendRedirect(home + "/alta_producto_ok");
						//req.getRequestDispatcher("/WEB-INF/vista/alta_producto_ok.jsp").forward(req, resp);
						//pero hay que darle entrada porque no se puede entrar directamnte a web-inf 
					} catch (Exception e) {
						resp.sendRedirect(home + "/alta_producto_error");
						//req.getRequestDispatcher("/WEB-INF/vista/alta_producto_error.jsp").forward(req, resp);
					}
				
				}else {
					login(req, resp); //con esto cerramos sesion.
				}
				
				break;
			case "/productos_fabricante":
				idFabStr = req.getParameter("idFabricante");
				//hacemos el control
				if(!isEmpty(idFabStr)
					&& isInteger(idFabStr) //si es entero el id de fabricante
					&& (fab = neg.getFabricante(Integer.parseInt(idFabStr))) !=null) {
					//guardamos la sesion
					sesion.setAttribute("fab", fab);
					resp.sendRedirect(home + "/productos_fabricante");
				}else {
					login(req, resp); //con esto cerramos sesion.
				}
				break;	
			case "/productos_fabricante_json_respuesta":
				idFabStr = req.getParameter("idFabricante");
				// lo validamos
				if (!isEmpty(idFabStr) && isInteger(idFabStr) // si es entero el id de fabricante
						&& (fab = neg.getFabricante(Integer.parseInt(idFabStr))) != null) {

					// ya tenemos el fabricante y ahora lo transformamos en json
					// hacemos el mapa
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(fab.getProductos());
					// respondemos con el objeto resp
					resp.getWriter().println(json);

				} else {
					login(req, resp); // con esto cerramos sesion.
				}
				break;
			// contemplar que no tenga ls sesion iniciada
			default:
				login(req, resp); // con esto cerramos sesion.
			}
		}else {//que no necesite la sesion 
			//declaramos referencias
			String usr, pwd;
			switch (path) {
			case "/login": 
				usr = req.getParameter("usr");
				pwd = req.getParameter("pwd");
				System.out.println(usr);
				System.out.println(pwd);
				//hacemos chequeo
				Usuario actual;
				if(!isEmpty(usr)&& !isEmpty(pwd)) {//que no esta vacio usuario y clave
					if((actual = neg.validaUsuario(usr, pwd)) !=null) { //si el usuario actual es el mismo que el usuario y no es null
						//este if no se deberia poner aqui pero lo vamos a poner para conntrolarlo de algun modo que este activo (disabled)
						if(actual.isEnabled()){//si esta activo lo logeamos, si no 
							sesion.setAttribute("usuario", actual); //si todo es correcto hay que iniciar sesion (guardamos el usuario una sesion)
							resp.sendRedirect(home + "/menu_principal"); //redirigimos al menu principal
						}else {//si el usuario no esta activo
							sesion.setAttribute("error", "disabled"); //mostramos mensaje 
							resp.sendRedirect(home + "/login");//redirigimos a login 
						}
					}else {//si el usuario no coincide
						sesion.setAttribute("error", "credenciales"); //mostramos mensaje 
						resp.sendRedirect(home + "/login");//redirigimos a login 
					}
				}else {
					login(req, resp); //con esto cerramos sesion.
				}
				break;
			case "/registro_usuarios":
				String nombre = req.getParameter("nombre");
				usr = req.getParameter("usr");
				pwd = req.getParameter("pwd");
				String email = req.getParameter("email");
				//hacemos el chequeo
				if(!isEmpty(nombre)
						&& !isEmpty(usr)
						&& !isEmpty(email)
						&& !isEmpty(pwd)
						&& checkPassword(pwd)) {
					//hay que crear el usuario 
					Usuario nuevo = new Usuario(nombre.trim(),email.trim(),usr.trim(),pwd.trim());
					sesion.setAttribute("nombreUsuario", nombre);
					try {
						//preguntar si pudo andar o no )
						if(neg.crearUsuario(nuevo)) {
							sesion.setAttribute("resu", "ok");
						}else {
							sesion.setAttribute("resu", "error");
						}
					}catch(Exception e) {
						sesion.setAttribute("resu", "existe");
					}
					resp.sendRedirect(home + "/registro_usuarios_respuesta");
				}else {
					login(req, resp); //con esto cerramos sesion.
				}
				
				break;
				default:
					login(req, resp); //con esto cerramos sesion.
			}
		}	
	}
	
	
	@Override
	public void init() throws ServletException {
		//2.para instanciado el negocio
		neg = new TiendaImple();
			
		ServletContext app = getServletContext();
		
		home = app.getContextPath() + "/tienda";
		
		app.setAttribute("home", home);
		app.setAttribute("css", app.getContextPath() + "/css");
	}
	
	private boolean isEmpty(String param) {
		return param == null || param.trim().length() == 0;
	}
	
	//estos son para convertir la cadena en un double y que no venga con errores, si todo esta ok es true si no da una exception pero como no me interesa es falso 
	private boolean isDouble(String num) {
		try {
			Double.parseDouble(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	//estos son para convertir la cadena en un entero y que no venga con errores, si todo esta ok es true si no da una exception pero como no me interesa es falso 
	private boolean isInteger(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private void eliminarDatosSesion(HttpSession sesion) {
		sesion.removeAttribute("fab");
		sesion.removeAttribute("fabs");
		sesion.removeAttribute("prods");
		
	}
	
	private boolean checkPassword(String pwd) {
		return pwd.trim().length()>5; //hacemos la comprobacion 
	}
	
	private boolean sesionIniciada(HttpSession sesion) {
		return sesion.getAttribute("usuario") != null;
	}
	private void cerrarSesion(HttpSession sesion) {
		sesion.invalidate();
	}
	private void login(HttpServletRequest req,HttpServletResponse resp) throws IOException {
		cerrarSesion(req.getSession());
		resp.sendRedirect(home + "/login");
	}
}
