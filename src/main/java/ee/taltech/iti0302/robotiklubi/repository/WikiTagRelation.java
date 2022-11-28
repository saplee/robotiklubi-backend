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
    @ManyToOne
    @JoinColumn(name = "page_id")
    private WikiPage page;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private WikiTag tag;
}
