package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageCreationResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
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

    public WikiPageCreationResponseDto createPage(WikiPageDto wikiPageDto) {
        WikiPageCreationResponseDto responseDto = new WikiPageCreationResponseDto();
        responseDto.setSucceeded(true);
        try {
            WikiPage page = new WikiPage();
            page.setTitle(wikiPageDto.getTitle());
            page.setContent(wikiPageDto.getTitle());
            page.setAuthorId(wikiPageDto.getAuthor());
            wikiRepository.save(page);
        } catch (Exception e) {
            responseDto.setSucceeded(false);
        }
        return responseDto;
    }
}
