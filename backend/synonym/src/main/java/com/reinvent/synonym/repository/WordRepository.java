package com.reinvent.synonym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reinvent.synonym.model.Word;

public interface WordRepository extends JpaRepository<Word, Long>{
	
	List<Word> findBySynonymGroup(Long synonymGroup);
	
	List<Word> findByWord(String word);
	
	@Query(value = "SELECT COALESCE(MAX(SYNONYM_GROUP),0) FROM WORD;", nativeQuery = true)
	Long findLatestSynonymGroup();
	
}
