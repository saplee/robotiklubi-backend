package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.exception.BadRequestException;
import ee.taltech.iti0302.robotiklubi.exception.InternalServerException;
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
            String tagName = tagDto.getTag();
            Optional<WikiTag> existing = tagRepository.findByTagIgnoreCase(tagName);
            if (existing.isPresent()) throw new BadRequestException("Tag already exists.");
            WikiTag tag = WikiTag.builder().tag(tagName).build();
            tagRepository.save(tag);
        } catch (Exception e) {throw new InternalServerException("Could not create tag.", e);}
    }

    public List<TagDto> getAllTags() {
        try{return wikiTagMapper.toDtoList(tagRepository.findAll());}
        catch (Exception e) {throw new InternalServerException("Could not retrieve all tags.", e);}
    }

    public void updateTag(Long id, TagDto tagDto) {
        try {
            Optional<WikiTag> tagOptional = tagRepository.findById(id);
            if (tagOptional.isEmpty()) throw new NotFoundException("Tag not found.");
            tagOptional.get().setTag(tagDto.getTag());
        } catch (Exception e) {throw new InternalServerException("Could not update tag (id " + id + ").", e);}
    }

    public void deleteTag(Long id) {
        try {
            Optional<WikiTag> tagOptional = tagRepository.findById(id);
            if (tagOptional.isEmpty()) throw new NotFoundException("Tag not found.");
            List<WikiTagRelation> relations = wikiTagRelationRepository.findAllByTag(tagOptional.get());
            wikiTagRelationRepository.deleteAll(relations);
            tagRepository.delete(tagOptional.get());
        } catch (Exception e) {throw new InternalServerException("Could not delete tag (id " + id + ").", e);}
    }
}
