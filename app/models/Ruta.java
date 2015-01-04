package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Ruta extends Model
{
	@Id private Long id;
	@Required private String ruta;
	private String tag;
	
	@ManyToOne
	@JsonIgnore
	private MyUser user;
	
	public static Finder<Long, Ruta> finder = new Finder<Long, Ruta>(Long.class, Ruta.class);
	

	public boolean updateData(Ruta newData) {
		boolean flag = false;
		
		if (newData.ruta != null) {
			this.ruta = newData.ruta;
			flag = true;
		}
		
		return flag;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}
	
	
}
