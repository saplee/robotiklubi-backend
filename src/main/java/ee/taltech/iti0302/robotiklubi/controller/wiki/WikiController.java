package ee.taltech.iti0302.robotiklubi.controller.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchResult;
import ee.taltech.iti0302.robotiklubi.service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WikiController {

    private final WikiService wikiService;

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/wiki/create")
    public Long createWikiPage(@RequestBody WikiPageDto wikiPageDto, Principal principal) {
        return wikiService.createPage(wikiPageDto, principal.getName());
    }

    @GetMapping("/wiki/{id}")
    public WikiPageDto getWikiPage(@PathVariable("id") Long id) {
        return wikiService.getPageById(id);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping("/wiki/update")
    public void updateWikiPage(@RequestParam("id") Long id, @RequestBody WikiPageDto wikiPageDto, Principal principal) {
        wikiService.updatePage(id, wikiPageDto, principal.getName());
    }

    @PreAuthorize("hasAuthority('MEMBER')")
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
