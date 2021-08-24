package com.reinvent.synonym.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reinvent.synonym.model.Word;
import com.reinvent.synonym.repository.WordRepository;

@SpringBootTest
@AutoConfigureMockMvc
class WordControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private WordRepository wordRepository;
	
	private String wordString = "cell";
	
	private String description = "test";
	
	private ObjectMapper objectMapper = new ObjectMapper();

	
	@Test
	void testGetWordBySynonymGroupReturnEmptyList() throws Exception {
		when(wordRepository.findAll()).thenReturn(null);
		mvc.perform(get("https://localhost:8080/api/words/{wordString}", wordString)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	void testGetWordByDescriptionReturnEmptyList() throws Exception {
		when(wordRepository.findAll()).thenReturn(null);
		mvc.perform(get("https://localhost:8080/api/words/check/{description}", description)
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
	}
	
	@Test
	void testAddWords() throws Exception {
		Word testWord = new Word();
		List<Word> wordsList = new ArrayList<>();
		wordsList.add(testWord);
		when(wordRepository.findByDescription(Mockito.any())).thenReturn(null);
		mvc.perform(post("https://localhost:8080/api/words/add")
				.content(objectMapper.writeValueAsString(wordsList))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}
}