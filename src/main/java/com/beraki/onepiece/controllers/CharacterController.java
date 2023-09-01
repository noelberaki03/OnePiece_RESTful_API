package com.beraki.onepiece.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.beraki.onepiece.repository.CharacterRepository;
import com.beraki.onepiece.entities.Character;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/onepiece")
public class CharacterController {
    private final CharacterRepository characterRepository;

    public CharacterController(final CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping
    public Iterable<Character> getAllCharacters() {
        return this.characterRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public Optional<Character> getCharacterById(@PathVariable("id") Integer id) {
        return this.characterRepository.findById(id);
    }

    @GetMapping("/name/{name}")
    public List<Character> getCharacterByName(@PathVariable("name") String name) {
        validateName(name);

        List<Character> listCharacter = this.characterRepository.findCharacterByName(name);
        if (listCharacter.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return listCharacter;
    }

    @GetMapping("/affiliation/{affiliation}")
    public List<Character> searchByAffiliation(@PathVariable("affiliation") String affiliation) {
        return this.characterRepository.findCharactersByAffiliation(affiliation);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Character addCharacter(@RequestBody Character character) {
        validateCharacter(character);
        return this.characterRepository.save(character);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Character updateCharacter(@PathVariable("id") Integer id, @RequestBody Character c) {
        Optional<Character> characterToUpdateOptional = this.characterRepository.findById(id);
        if (characterToUpdateOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Character characterToUpdate = characterToUpdateOptional.get();
        if (ObjectUtils.isEmpty(c.getName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!ObjectUtils.isEmpty(c.getName())) {
            characterToUpdate.setName(c.getName());
        }
        if (!ObjectUtils.isEmpty(c.getAffiliation())) {
            characterToUpdate.setAffiliation(c.getAffiliation());
        }

        return this.characterRepository.save(characterToUpdate);
    }

    @DeleteMapping("/{id}")
    public Character deleteCharacter(@PathVariable("id") Integer id) {
        Optional<Character> characterToDeleteOptional = this.characterRepository.findById(id);
        if (characterToDeleteOptional.isEmpty()) {
            return null;
        }
        Character characterToDelete = characterToDeleteOptional.get();
        this.characterRepository.delete(characterToDelete);
        return characterToDelete;
    }

    private void validateCharacter(Character character) {
        validateName(character.getName());

        List<Character> existingCharacter = this.characterRepository.findCharacterByName(character.getName());
        if (!existingCharacter.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }
    private void validateName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}