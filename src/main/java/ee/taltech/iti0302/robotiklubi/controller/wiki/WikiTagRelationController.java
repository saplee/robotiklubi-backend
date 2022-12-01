package ee.taltech.iti0302.robotiklubi.controller.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagListDto;
import ee.taltech.iti0302.robotiklubi.service.WikiTagRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class WikiTagRelationController {

    private final WikiTagRelationService relationService;

    @PostMapping("/tags/relation/create")
    public void createRelation(@RequestParam("pageId") Long pageId, @RequestParam("tagId") Long tagId) {
        relationService.createRelation(pageId, tagId);
    }

    @PostMapping("/tags/relation/create/many")
    public void createRelations(@RequestParam("pageId") Long pageId, @RequestBody() TagListDto tags) {
        relationService.createRelations(pageId, tags);
    }

    // TODO: @DeleteMapping("/tags/relation/delete")
    @PostMapping("/tags/relation/delete")
    public void deleteRelation(@RequestParam("pageId") Long pageId, @RequestParam("tagId") Long tagId) {
        relationService.deleteRelation(pageId, tagId);
    }

    // TODO: @DeleteMapping("/tags/relation/delete/many")
    @PostMapping("/tags/relation/delete/many")
    public void deleteRelations(@RequestParam("pageId") Long pageId, @RequestBody() TagListDto tags) {
        relationService.deleteRelations(pageId, tags);
    }
}
