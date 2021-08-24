package com.reinvent.synonym.dto;

import lombok.Data;

/*
 * Data transfer object class used to restrict user from attacks through requests.
 * All the endpoints are safe, mapped to/from DTO to isolate end users from the entities
 */

@Data
public class WordDTO {
	private Long id;
	private String word;
	private String description;
	private Long synonymGroup;
}
