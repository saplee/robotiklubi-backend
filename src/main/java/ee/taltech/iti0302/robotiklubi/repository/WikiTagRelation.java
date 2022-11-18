package ee.taltech.iti0302.robotiklubi.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "wiki_tag_relations")
@Entity
public class WikiTagRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "page_id")
    private Integer pageId;
    @Column(name = "tag_id")
    private Integer tagId;
}
