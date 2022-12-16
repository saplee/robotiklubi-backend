package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface ClientRepository extends JpaRepositoryImplementation<Client, Long> {
    List<Client> findAllByFirstNameAndLastNameAndEmailAndPhoneNumber(String firstName, String lastName, String email,
                                                                     String phoneNumber);
}
