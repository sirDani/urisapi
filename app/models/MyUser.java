package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MyUser extends Model
{
	@Id private Long id;
	@Required private String nombre;
	@Required private String apellidos;
	private Integer edad;
	
	@Email private String email;
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	public List<Ruta> rutas = new ArrayList<Ruta>();
	
	public static Finder<Long, MyUser> finder=new Finder<Long, MyUser>(Long.class, MyUser.class);
	
	public static List<MyUser> findPagina(Integer pagina, Integer size) {
		return finder.orderBy("id").setMaxRows(size).setFirstRow(pagina*size).findList();
	}
	
	public boolean updateData(MyUser newData) {
		boolean flag = false;
		
		if (newData.nombre != null) {
			this.nombre = newData.nombre;
			flag = true;
		}
		
		if (newData.apellidos != null) {
			this.apellidos = newData.apellidos;
			flag = true;
		}
		
		return flag;
	}
	
	public void addRuta(Ruta ruta)
	{
		rutas.add(ruta);
		ruta.setUser(this);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Ruta> getRutas() {
		return rutas;
	}

	public void setRutas(List<Ruta> rutas) {
		this.rutas = rutas;
	}
	

	@Override
	public String toString() {
		return "User [id=" + id + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", edad=" + edad + ", email=" + email
				+ ", password=" + password + ", rutas=" + rutas + "]";
	}
	
	

	
}
