package com.example.management.repository;
import com.example.management.model.Case;
import com.example.management.model.Severity;
import com.example.management.model.Status;
import com.example.management.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
class CaseRepositoryTest {
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void removeAllByStatusDone() {
        User user = new User("testUsername", "testEmail@gmail.com");
        Case newCase = new Case("testTitle","testDescription", Severity.HIGH, user);
        entityManager.persist(newCase);
        entityManager.flush();

        List<Case> c = caseRepository.findAll();
        System.out.println(c.size());

        Long num = caseRepository.removeAllByStatus(Status.DONE);
        assertThat(num).isNotNull();
        assertThat(num).isEqualTo(0);
    }

    @Test
    void findByUuid() {
        User user = new User("testUsername", "testEmail@gmail.com");
        Case newCase = new Case("testTitle","testDescription", Severity.HIGH, user);
        entityManager.persist(newCase);
        entityManager.flush();

        UUID u = newCase.getUuid();

        Optional<Case> _case = caseRepository.findByUuid(u);

        assertThat(_case).isPresent();
        assertThat(_case.get().getId()).isEqualTo(newCase.getId());
    }
}