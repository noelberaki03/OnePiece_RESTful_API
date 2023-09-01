package com.beraki.onepiece.repository;

import org.springframework.data.repository.CrudRepository;
import com.beraki.onepiece.entities.Character;

import java.util.List;

public interface CharacterRepository extends CrudRepository<Character, Integer> {
    List<Character> findCharacterByName(String name);
    List<Character> findCharactersByAffiliation(String crew);
}
