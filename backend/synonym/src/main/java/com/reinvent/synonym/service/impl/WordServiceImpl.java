package com.reinvent.synonym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.repository.WordRepository;
import com.reinvent.synonym.service.WordService;

@Service
public class WordServiceImpl implements WordService {
	
	private final WordRepository wordRepository;
	
	public WordServiceImpl(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}
	
	//Method used to save a word in the database
	@Override
	public Word createWord(Word word) {
		return wordRepository.save(word);
	}

	//Method used to retrieve words by synonym group from the database
	@Override
	public List<Word> getWordsBySynonymGroup(Long synonymGroup) {
		return wordRepository.findBySynonymGroup(synonymGroup);
	}
	
	//Method used to retrieve words by word string from the database
	@Override
	public List<Word> getWordsByWordString(String word) {
		return wordRepository.findByWord(word);
	}
	
	//Method used to retrieve words by description from the database
	@Override
	public List<Word> getWordsByDescription(String description) {
		return wordRepository.findByDescription(description);
	}
	
	//Method used to get the latest synonym group so it can be incremented by 1 when a word with a new definition is added
	@Override
	public Long getLatestSynonymGroup() {
		return wordRepository.findLatestSynonymGroup();
	}
	
	//Method used to retrieve all words from the database and check them against another array. It returns the first match or null if there aren't any
	//It is used when we want to see if there are words with the same definition in the database in the 'getExistingSynonymsByDescription' endpoint
	@Override
	public Word checkForExistingSynonyms(List<String> listOfSynonyms) {
		List<Word> dbWords = wordRepository.findAll();
		for (int i = 0; i < dbWords.size(); i++) {
			if (!getDbWordsDictionarySynonymsMatch(listOfSynonyms, dbWords.get(i).getWord()).equals("")) {
				return dbWords.get(i);
			}
		}
	    return null;
	}
	
	public String getDbWordsDictionarySynonymsMatch(List<String> dictionaryWords, String word) {
		for (int i = 0; i < dictionaryWords.size(); i++) {
			if (dictionaryWords.get(i).equals(word)) {
				return dictionaryWords.get(i);
			}
		}
		return "";
	}
}
