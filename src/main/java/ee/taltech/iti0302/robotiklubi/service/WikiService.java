package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import ee.taltech.iti0302.robotiklubi.repository.WikiRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WikiService {

    @NonNull
    private WikiRepository wikiRepository;
    @NonNull
    private WikiPageMapper wikiPageMapper;

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
}
