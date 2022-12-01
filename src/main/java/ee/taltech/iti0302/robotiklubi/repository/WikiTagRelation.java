package ee.taltech.iti0302.robotiklubi.repository;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
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
