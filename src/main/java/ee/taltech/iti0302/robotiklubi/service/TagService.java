package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.exception.ApplicationException;
import ee.taltech.iti0302.robotiklubi.exception.NotFoundException;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiTagMapper;
import ee.taltech.iti0302.robotiklubi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class TagService {

    private final WikiTagMapper wikiTagMapper;
    private final WikiTagRepository tagRepository;

    private final WikiTagRelationRepository wikiTagRelationRepository;

    public void createTag(TagDto tagDto) {
        try {
            String tagName = tagDto.getTag().toLowerCase();
            Optional<WikiTag> existing = tagRepository.findByTag(tagName);
            if (existing.isPresent()) throw new ApplicationException("Tag already exists.");
            WikiTag tag = WikiTag.builder().tag(tagName).build();
            tagRepository.save(tag);
        } catch (Exception e) {
            throw new ApplicationException("Could not create tag.");
        }
    }

    public List<TagDto> getAllTags() {
        return wikiTagMapper.toDtoList(tagRepository.findAll());
    }

    public void updateTag(Long id, TagDto tagDto) {
        Optional<WikiTag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) throw new NotFoundException("Tag not found.");
        try {tagOptional.get().setTag(tagDto.getTag());}
        catch (Exception e) {throw new ApplicationException("Could not update tag.");}
    }

    public void deleteTag(Long id) {
        Optional<WikiTag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) throw new NotFoundException("Tag not found.");
        List<WikiTagRelation> relations = wikiTagRelationRepository.findAllByTag(tagOptional.get());
        wikiTagRelationRepository.deleteAll(relations);
        tagRepository.delete(tagOptional.get());
    }
}
