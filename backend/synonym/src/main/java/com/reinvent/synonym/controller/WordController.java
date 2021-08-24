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
		this.wordService = wordService;
	}
	
	//Endpoint used to retrieve synonyms by a word string path variable
	@CrossOrigin
	@GetMapping("/{wordString}")
	public ResponseEntity<List<WordDTO>> getWordBySynonymGroup(@PathVariable String wordString) {
		//Get the words from the database using a word string
		List<Word> requestWords = wordService.getWordsByWordString(wordString);
		
		//Return an empty list if there are no synonyms for the entered word
		if (requestWords == null) {
			return ResponseEntity.ok().body(new ArrayList<>());
		}
		//Get the different synonym groups for each word with the same spelling, different meaning e.g. cell -> organism / prison cell
		List<Long> synonymGroups = new ArrayList<>();
		requestWords.forEach(word -> synonymGroups.add(word.getSynonymGroup()));
		
		//For each synonym group, get synonyms by that synonym group and append to synonyms list
		List<Word> synonyms = new ArrayList<>();
		synonymGroups.forEach(synonymGroup -> synonyms.addAll(wordService.getWordsBySynonymGroup(synonymGroup)));
		
		//Convert entity list to DTO
		List<WordDTO> wordResponse = synonyms.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok().body(wordResponse);
	}
	
	//Endpoint used to add new synonyms
	@CrossOrigin
	@PostMapping("/add")
	public ResponseEntity<List<WordDTO>> createWord(@RequestBody List<WordDTO> wordDtoList) {
		//Convert the list of WordDTOs to a list of Word entities
		List<Word> wordRequest = wordDtoList.stream()
				.map(word -> modelMapper.map(word, Word.class))
				.collect(Collectors.toList());
		
		//Get the highest synonym group to increment next one by 1 if there aren't existing synonyms in the database
		if (wordRequest.get(0).getSynonymGroup() == null || wordRequest.get(0).getSynonymGroup() == -1L) {
			Long latestSynonymGroup = wordService.getLatestSynonymGroup();
			wordRequest.forEach(word -> word.setSynonymGroup(latestSynonymGroup + 1));
		}
		//Save the synonyms in the database
		wordRequest.forEach(word -> wordService.createWord(word));
		
		//Convert entity list to DTO
		List<WordDTO> wordResponse = wordRequest.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		
		return new ResponseEntity<List<WordDTO>>(wordResponse, HttpStatus.CREATED);
		
	}
	
	//Endpoint used to check if there are existing synonyms with the description that is provided as a path variable
	@CrossOrigin
	@GetMapping("/check/{description}")
	public ResponseEntity<List<WordDTO>> getExistingSynonymsByDescription(@PathVariable String description) {
		//Get the list of words with the provided description from the database
		List<Word> wordResponseEntity = wordService.getWordsByDescription(description);
		
		//Convert entity to DTO
		List<WordDTO> wordResponse = wordResponseEntity.stream()
				.map(word -> modelMapper.map(word, WordDTO.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(wordResponse);
	}
}
