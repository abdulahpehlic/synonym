package com.reinvent.synonym.service;

import java.util.List;

import com.reinvent.synonym.model.Word;

public interface WordService {
	
	List<Word> getWordsBySynonymGroup(Long synonymGroup);
	
	Word createWord(Word word);
	
	Word getWordsByWordString(String word);
	
}
