package com.reinvent.synonym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reinvent.synonym.model.Word;

public interface WordRepository extends JpaRepository<Word, Long>{
	
	List<Word> findBySynonymGroup(Long synonymGroup);
	
}
