package ee.taltech.iti0302.robotiklubi.repository;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wiki_tags")
@Entity
public class WikiTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag")
    private String tag;
}
