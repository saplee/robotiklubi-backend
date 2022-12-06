package ee.taltech.iti0302.robotiklubi.controller.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagListDto;
import ee.taltech.iti0302.robotiklubi.service.WikiTagRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class WikiTagRelationController {

    private final WikiTagRelationService relationService;

    //    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PostMapping("/tags/relation/create")
    public void createRelation(@RequestParam("pageId") Long pageId, @RequestParam("tagId") Long tagId) {
        relationService.createRelation(pageId, tagId);
    }

    //    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @PostMapping("/tags/relation/create/many")
    public void createRelations(@RequestParam("pageId") Long pageId, @RequestBody() TagListDto tags) {
        relationService.createRelations(pageId, tags);
    }

    //    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @DeleteMapping("/tags/relation/delete")
    public void deleteRelation(@RequestParam("pageId") Long pageId, @RequestParam("tagId") Long tagId) {
        relationService.deleteRelation(pageId, tagId);
    }

    //    @PreAuthorize("hasAnyAuthority('MEMBER')")
    @DeleteMapping("/tags/relation/delete/many")
    public void deleteRelations(@RequestParam("pageId") Long pageId, @RequestBody() TagListDto tags) {
        relationService.deleteRelations(pageId, tags);
    }
}
