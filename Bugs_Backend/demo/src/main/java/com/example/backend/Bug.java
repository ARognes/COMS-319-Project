package com.example.backend;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;

/**
 * 
 * @author Matt Koeser
 * Class for bug
 */
@Entity
@Table(name="bug")
public class Bug {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bugid")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer bugid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "BodyParts")//?
	private String BodyParts;
	
	@Column(name = "creator")
	private String creator;
	
	@Column(name = "seed")
	private Integer seed;
	
	
	public Bug() {
		/*
		 * generate random head, body part(s), and legs
		 */
		/*
		 * If file is stored on server, upload to server and create reference to name of file 
		 * and store it within the bug class
		 */
	}
	
	
}
