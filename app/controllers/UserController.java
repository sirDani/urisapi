package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.ControllerHelper;
import play.mvc.Controller;
import models.MyUser;
import models.MyUser;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class UserController extends Controller
{
	//Funcion la cual se dispara por defecto en la aplicacion
	public static Result index() {
        return ok("Hola, esto es un api programado por: Daniel Rincón Vega y Alberto Seoane Rodriguez de la Rua");
    }
	
	
	 //Metodo GET /user/<pag>.
	//pag número de página a recuperar.
	//Opcionalmente se puede pasar el parámetro size para indicar el tamaño de página.
	 //Si no se pasa tamaño de página, se usará 10
	public static Result listUsers(Integer pag){
		Result res;
		
		String paginaSize = request().getQueryString("size");
		if (paginaSize == null) {
			paginaSize = "10";
		}

		List<MyUser> lista = MyUser.findPagina(pag, Integer.valueOf(paginaSize));
		Integer count = MyUser.finder.findRowCount();
		
		if (ControllerHelper.acceptsJson(request())) {
			Map<String, Object> result = new HashMap<String, Object>();
			
			result.put("count", count);
			result.put("pagina", lista);
			
			res = ok(Json.toJson(result));
		}
		else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.users.render(lista, count));
		}
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
			
		return res;
	}
	 
	 //Metodo GET para recuperar informacion de un MyUser
	 //Mediante el id asociado se obtiene: /user/<uId> 	 
	public static Result getUser(Long id){
		Result res;
		MyUser usuario = MyUser.finder.byId(id);
		if (usuario == null) {
			res = notFound();
		}
		else {
			if (ControllerHelper.acceptsJson(request())) {
				res = ok(Json.toJson(usuario));
			}
			else if (ControllerHelper.acceptsXml(request())) {
				res = ok(views.xml._user.render(usuario));
			}
			else {
				res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
			}
		}
		return res;
	}
	
	 // Metodo POST /user/
	//Se deben pasar los atributos JSON del usuario en el body de la petición. 	
	public static Result createUser(){
		Form<MyUser> form = Form.form(MyUser.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", form.errorsAsJson()));
		}

		MyUser usuario = form.get();
		
		usuario.save();
		
		// Esto implementa una característica de hypermedia: devolvemos la URL para consultar
		// los detalles del usuario
		response().setHeader(LOCATION, routes.UserController.getUser(usuario.getId()).absoluteURL(request()));

		return created();
	}	
	
	 // Metodo PUT /user/<uId>
	 //Se deben pasar los atributos JSON a modificar en el body de la petición
	public static Result updateUser(Long id){
		MyUser usuario = MyUser.finder.byId(id);
		if (usuario == null) {
			return notFound();
		}
		
		Form<MyUser> form = Form.form(MyUser.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(1, "invalid_user", form.errorsAsJson()));
		}

		Result res;

		if (usuario.updateData(form.get())) {
			usuario.save();
			res = ok();
		}
		else {
			res = status(NOT_MODIFIED);
		}
		
		return res;
	}
	
	 //Metodo DELETE /user/<uId>  
	// Se busca por id (en el caso de que no exista el servidor responde notFound)
	 //si existe se borra 
	public static Result deleteUser(Long id){
		MyUser usuario = MyUser.finder.byId(id);
		if (usuario == null) {
			return notFound();
		}

		usuario.delete();

		return ok();
	}
}
