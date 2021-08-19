package com.reinvent.synonym.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.repository.WordRepository;
import com.reinvent.synonym.service.WordService;

@Service
public class WordServiceImpl implements WordService {
	
	private final WordRepository wordRepository;
	
	public WordServiceImpl(WordRepository wordRepository) {
		super();
		this.wordRepository = wordRepository;
	}
	
	@Override
	public Word createWord(Word word) {
		return wordRepository.save(word);
	}

	@Override
	public List<Word> getWordsBySynonymGroup(Long synonymGroup) {
		return wordRepository.findBySynonymGroup(synonymGroup);
	}
	
	@Override
	public List<Word> getWordsByWordString(String word) {
		return wordRepository.findByWord(word);
	}
	
	@Override
	public Long getLatestSynonymGroup() {
		return wordRepository.findLatestSynonymGroup();
	}
	
	@Override
	public Word checkForExistingSynonyms(List<String> listOfSynonyms) {
		List<Word> dbWords = wordRepository.findAll();
		for (int i = 0; i < dbWords.size(); i++) {
			if (getDbWordsDictionarySynonymsMatch(listOfSynonyms, dbWords.get(i).getWord()) != "") {
				return dbWords.get(i);
			}
		}
	    return null;
	}
	
	public String getDbWordsDictionarySynonymsMatch(List<String> dictionaryWords, String word) {
		for (int i = 0; i < dictionaryWords.size(); i++) {
			if (dictionaryWords.get(i) == word) {
				return dictionaryWords.get(i);
			}
		}
		return "";
	}
}
