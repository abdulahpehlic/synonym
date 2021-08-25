package com.reinvent.synonym.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.repository.WordRepository;
import com.reinvent.synonym.service.WordService;

@SpringBootTest
class WordServiceImplTest {
	
	@Mock
	private WordRepository wordRepository;

	private WordService wordService;
	
	@BeforeEach
    public void setUp() {
        wordService = new WordServiceImpl(wordRepository);
    }
	
	@Test
	void testCheckForExistingSynonyms() {
		//Given
		Word testWord = new Word();
		testWord.setId(1L);
		testWord.setDescription("testDescription");
		testWord.setSynonymGroup(1L);
		testWord.setWord("test");
		List<Word> testList = new ArrayList<>();
		testList.add(testWord);
		List<String> testParameterList = new ArrayList<>();
		testParameterList.add("test");
		
		//When
		doReturn(testList).when(wordRepository).findAll();
		
		//Then
		Word resultWord = wordService.checkForExistingSynonyms(testParameterList);
		assertEquals("test", resultWord.getWord());
	}

}
