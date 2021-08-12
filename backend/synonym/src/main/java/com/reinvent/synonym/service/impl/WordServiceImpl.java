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
	public Word getWordsByWordString(String word) {
		return wordRepository.findByWord(word);
	}
}
