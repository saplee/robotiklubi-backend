package ee.taltech.iti0302.robotiklubi.controller.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchResult;
import ee.taltech.iti0302.robotiklubi.service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WikiController {

    private final WikiService wikiService;

    @PostMapping("/wiki/create")
    public Long createWikiPage(@RequestBody WikiPageDto wikiPageDto) {
        return wikiService.createPage(wikiPageDto);
    }

    @GetMapping("/wiki/{id}")
    public WikiPageDto getWikiPage(@PathVariable("id") Long id) {
        return wikiService.getPageById(id);
    }

    // TODO: @PutMapping("/wiki/update")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT')")
    @PostMapping("/wiki/update")
    public void updateWikiPage(@RequestParam("id") Long id, @RequestBody WikiPageDto wikiPageDto) {
        wikiService.updatePage(id, wikiPageDto);
    }

    // TODO: @DeleteMapping("/wiki/delete")
    @PreAuthorize("hasAnyAuthority('MANAGEMENT')")
    @DeleteMapping("/wiki/delete")
    public void deleteWikiPage(@RequestParam("id") Long id) {
        wikiService.deletePage(id);
    }

    @GetMapping("/wiki/tags/{id}")
    public List<TagDto> getWikiPageTags(@PathVariable("id") Long id) {
        return wikiService.getPageTags(id);
    }

    @PostMapping ("/wiki/search")
    public WikiSearchResult searchWikiPages(@RequestBody WikiSearchCriteria searchCriteria) {
        return wikiService.findAllByCriteria(searchCriteria);
    }
}
