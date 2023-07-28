package com.htm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="USER_DTLS")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
    private String userName;
    private String password;
    private String email;
    
    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "cust_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
	Set<Role> roles = new HashSet<Role>();


	public Set<Role> getRole() {

		return roles;
	}

	public void setRole(Role role) {

		this.roles.add(role);
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
