package br.unifesp.coruja.meta.persistence.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class Permission implements DTO{
	
	private Long id;
	
	@NotEmpty
	private String rolename;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
}
