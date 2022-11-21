package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.exception.ApplicationException;
import ee.taltech.iti0302.robotiklubi.exception.NotFoundException;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import ee.taltech.iti0302.robotiklubi.repository.WikiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class WikiService {

    private final WikiRepository wikiRepository;
    private final WikiPageMapper wikiPageMapper;

    public WikiPageDto getPageById(Long id) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        if (pageOptional.isPresent()) {
            WikiPage page = pageOptional.get();
            page.setTitle(page.getTitle().strip());
            return wikiPageMapper.toDto(pageOptional.get());
        }
        WikiPageDto dto = new WikiPageDto();
        dto.setTitle("");
        dto.setContent("");
        return dto;
    }

    public void createPage(WikiPageDto wikiPageDto) {
        try {
            WikiPage page = new WikiPage();
            page.setTitle(wikiPageDto.getTitle());
            page.setContent(wikiPageDto.getContent());
            page.setAuthorId(wikiPageDto.getAuthor());
            wikiRepository.save(page);
        } catch (Exception e) {
            throw new ApplicationException("Could not create wiki page.");
        }
    }

    public void updatePage(Long id, WikiPageDto wikiPageDto) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        if (pageOptional.isEmpty()) throw new NotFoundException("Wiki page not found.");
        try {
            WikiPage page = pageOptional.get();
            page.setTitle(wikiPageDto.getTitle());
            page.setContent(wikiPageDto.getContent());
            page.setLastEditedBy(wikiPageDto.getLastEditedBy());
            wikiRepository.save(page);
        } catch (Exception e) {
            throw new ApplicationException("Could not update wiki page.");
        }
    }

    public void deletePage(Long id) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        pageOptional.ifPresentOrElse(
                wikiRepository::delete,
                () -> {throw new NotFoundException("Wiki page not found.");});
    }
}
