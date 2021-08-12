package com.reinvent.synonym.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reinvent.synonym.dto.WordDTO;
import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.service.WordService;

@RestController
@RequestMapping("/api/words")
public class WordController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	private WordService wordService;
	
	public WordController(WordService wordService) {
		super();
		this.wordService = wordService;
	}
	
	@GetMapping("/{synonymGroup}")
	public ResponseEntity<List<WordDTO>> getWordBySynonymGroup(@PathVariable(name = "synonymGroup") Long synonymGroup) {
		List<Word> synonyms = wordService.getWordsBySynonymGroup(synonymGroup);
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = synonyms.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(wordResponse);
	}
	
	@PostMapping
	public ResponseEntity<WordDTO> createWord(@RequestBody WordDTO wordDto) {
		//Convert DTO to entity
		Word wordRequest = modelMapper.map(wordDto, Word.class);
		
		Word word = wordService.createWord(wordRequest);
		
		//Convert entity to DTO
		WordDTO wordResponse = modelMapper.map(word, WordDTO.class);
		
		return new ResponseEntity<WordDTO>(wordResponse, HttpStatus.CREATED);
		
	}
}
