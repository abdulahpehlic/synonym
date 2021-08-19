package com.reinvent.synonym.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reinvent.synonym.dto.WordDTO;
import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.service.WordService;

@CrossOrigin
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
	
	// I NEED TO PROVIDE FUNCTIONALITY FOR MULTIPLE RESULTS IF A WORD IS REQUESTED
	@CrossOrigin
	@GetMapping("/{wordString}")
	public ResponseEntity<List<WordDTO>> getWordBySynonymGroup(@PathVariable String wordString) {
		List<Word> requestWords = wordService.getWordsByWordString(wordString);
		if (requestWords == null) {
			return ResponseEntity.ok().body(new ArrayList<>());
		}
		List<Long> synonymGroups = new ArrayList<>();
		requestWords.forEach(word -> synonymGroups.add(word.getSynonymGroup()));
		
		List<Word> synonyms = new ArrayList<>();
		synonymGroups.forEach(synonymGroup -> synonyms.addAll(wordService.getWordsBySynonymGroup(synonymGroup)));
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = synonyms.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(wordResponse);
	}
	@CrossOrigin
	@PostMapping("/add")
	public ResponseEntity<List<WordDTO>> createWord(@RequestBody List<WordDTO> wordDtoList) {
		//Convert the list of WordDTOs to a list of Word entities
		List<Word> wordRequest = wordDtoList.stream()
				.map(word -> modelMapper.map(word, Word.class))
				.collect(Collectors.toList());
		//Get the highest synonym group to increment next one by one
		Long latestSynonymGroup = wordService.getLatestSynonymGroup();
		wordRequest.forEach(word -> word.setSynonymGroup(latestSynonymGroup + 1));
		//Save the synonyms in the database
		wordRequest.forEach(word -> wordService.createWord(word));
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = wordRequest.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return new ResponseEntity<List<WordDTO>>(wordResponse, HttpStatus.CREATED);
		
	}
}
