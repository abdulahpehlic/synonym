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
	
	@GetMapping("/{word}")
	public ResponseEntity<List<WordDTO>> getWordBySynonymGroup(@PathVariable(name = "word") String wordString) {
		Word requestWord = wordService.getWordsByWordString(wordString);
		
		Long synonymGroup = requestWord.getSynonymGroup();
		
		List<Word> synonyms = wordService.getWordsBySynonymGroup(synonymGroup);
		
		synonyms.removeIf(obj -> obj.getId() == requestWord.getId());
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = synonyms.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(wordResponse);
	}
	
	@PostMapping
	public ResponseEntity<List<WordDTO>> createWord(@RequestBody List<WordDTO> wordDtoList) {
		//Convert DTO to entity
		List<Word> wordRequest = wordDtoList.stream()
				.map(word -> modelMapper.map(word, Word.class))
				.collect(Collectors.toList());
		
		wordRequest.forEach(word -> wordService.createWord(word));
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = wordRequest.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return new ResponseEntity<List<WordDTO>>(wordResponse, HttpStatus.CREATED);
		
	}
}
