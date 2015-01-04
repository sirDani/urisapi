package controllers;

import controllers.ControllerHelper;
import play.api.*;
import play.data.DynamicForm;
import java.util.List;
import java.util.ArrayList;
import play.mvc.Controller;
import models.Ruta;
import models.MyUser;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class RutaController extends Controller
{
	
	
	 //Metodo GET /user/<uId>/ruta
	//Json y XML aceptados
	public static Result getRuta(Long uId){
		Result res;
		
		MyUser usuario = MyUser.finder.byId(uId);		
		if (usuario == null) {
			return notFound();
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			res = ok(Json.toJson(usuario.getRutas()));
		}
		else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.rutas.render(usuario.getRutas()));
		}
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
			
		return res; 
	}
	

	//Metodo POST para crear la ruta de un usuario
	// /user/<uId>/ruta
	//si el objeto Ruta es insertado incorrectamente devuelve ruta invalida
	//sino la guarda
	public static Result create(Long uId) {
		Form<Ruta> form = Form.form(Ruta.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_phone", form.errorsAsJson()));
		}
		
		MyUser usuario = MyUser.finder.byId(uId);		
		if (usuario == null) {
			return notFound();
		}

		Ruta url = form.get();
		
		url.setUser(usuario);
		url.save();
		
		return created();
	}
	
	public static Result findRuta(Long uId){
		Result res;
		
//		MyUser usuario = MyUser.finder.byId(uId);		
//		if (usuario == null) {
//			return notFound();
//		}
		
//		MyUser usuario1=MyUser.finder.where().eq("tag", tag).findUnique();
//		if(!(usuario.getId().equals(usuario1.getId()))){
//
//			return notFound();
//		}
//		Form<Ruta> form = Form.form(Ruta.class).bindFromRequest();
//		String tag = form.get();
		DynamicForm requestData = Form.form().bindFromRequest();
		String tag=requestData.get("tag");
		
		Ruta ruta=Ruta.finder.where().eq("tag",tag).findUnique();
		List<Ruta> rutas= new ArrayList<Ruta>();
		rutas.add(ruta);
		
		if(ruta==null){
			return notFound();
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			res = ok(Json.toJson(rutas));
		}
		else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.rutas.render(rutas));
		}
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
		
		return res;
	}
	
	// Metodo PUT /user/<uId>/ruta/<tId>
	// Primero busca la url, si no existe devuelve notFound y luego comprueba si el usuario existe
	//Se deben pasar los atributos JSON a modificar en el body de la petición
	public static Result updateRuta(Long uId, Long rId){
		Ruta ruta = Ruta.finder.byId(rId);
		if (ruta == null) {
			return notFound();
		}
		
		if (!ruta.getUser().getId().equals(uId)) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", null));
		}
			
		Form<Ruta> form = Form.form(Ruta.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(1, "invalid_user", form.errorsAsJson()));
		}

		Result res;

		if (ruta.updateData(form.get())) {
			ruta.save();
			res = ok();
		}
		else {
			res = status(NOT_MODIFIED);
		}
			
		return res;
	}

	//Metodo DELETE /user/<uId>/ruta/<tId>
	 // Necesario el id del usuario y el id de la ruta
	//primero busca la url, si no existe, responde notFound y despues se comprueba ->
	//->si existe el usuario y se borra la ruta, sino existe usuario la respuesta es usuario invalido
	public static Result delete(Long uId, Long tId) {
		Ruta url = Ruta.finder.byId(tId);
		if (url == null) {
			return notFound();
		}
		
		if (!url.getUser().getId().equals(uId)) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", null));
		}

		url.delete();

		return ok();
	}
}
