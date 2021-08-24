package com.reinvent.synonym.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WORD")
public class Word {

	//Primary key, unique id for all the different words
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//String which contains the actual word
	@Column(name = "WORD")
	private String word;
	
	//Description used to prevent users from entering multiple words with same meaning and same description
	@Column(name = "DESCRIPTION")
	private String description;
	
	/*
	 * Most of the logic is handled by this attribute. 
	 * If we say that A is synonym to B then B must be a synonym to A, 
	 * and with transitive rule applied,
	 * we can say that all of these synonyms belong to the same group, thus the synonymGroup attribute
	 * 
	 * Premise A: If A is a synonym to B than B is a synonym to A
	 * 
	 * Premise B: If B is a synonym to A and C a synonym to B, then C should automatically, 
	 * by transitive rule, also be the synonym for "A".
	 * 
	 * Conclusion: A, B and C belong to the same group, and if any of these are associated with D, 
	 * D instantly becomes a part of the group
	 */
	@Column(name = "SYNONYM_GROUP")
	private Long synonymGroup;
	
}
