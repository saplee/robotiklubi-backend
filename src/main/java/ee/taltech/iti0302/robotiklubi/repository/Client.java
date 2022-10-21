package ee.taltech.iti0302.robotiklubi.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Table(name = "clients")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "mail")
    private String email;
    @Column(name = "phone")
    private String phoneNumber;
}
