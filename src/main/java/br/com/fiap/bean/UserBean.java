package br.com.fiap.bean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;

@Named
@RequestScoped
public class UserBean {

	private User user = new User();
	
	private UploadedFile image;
	
	@Inject 
	private UserDao dao;
	
	public String save() throws IOException{
		System.out.println(this.user);
		
		ServletContext servletContext = (ServletContext) FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getContext();
		String path = servletContext.getRealPath("/");
		
		FileOutputStream out = 
				new FileOutputStream(path + "\\images\\" + image.getFileName());
		out.write(image.getContent());
		out.close();
		
		user.setImagePath("\\images\\" + image.getFileName());
		
		dao.create(getUser());
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage("Visitante cadastrado com sucesso"));
		
		return "users";
	}
	
	public List<User> getAll(){
		return dao.listAll();
	}
	
	public String login() {
		if(dao.exist(user)) return "setups";
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido", "Erro"));
		return "login";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}


}
