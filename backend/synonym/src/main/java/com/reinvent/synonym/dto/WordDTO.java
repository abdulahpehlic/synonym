package com.reinvent.synonym.dto;

import lombok.Data;

@Data
public class WordDTO {
	private Long id;
	private String word;
	private Long synonymGroup;
}
