package ee.taltech.iti0302.robotiklubi.repository;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "role")
    private Integer role;
    @Column(name = "is_admin")
    private Boolean isAdmin;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
