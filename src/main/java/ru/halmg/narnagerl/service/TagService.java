package ru.halmg.narnagerl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.halmg.narnagerl.model.Tag;
import ru.halmg.narnagerl.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
