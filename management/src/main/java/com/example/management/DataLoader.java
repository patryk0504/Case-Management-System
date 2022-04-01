package com.example.management;

import com.example.management.model.Case;
import com.example.management.model.Severity;
import com.example.management.model.User;
import com.example.management.repository.CaseRepository;
import com.example.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private CaseRepository caseRepository;

    @Autowired
    public DataLoader(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    public void run(ApplicationArguments args) {
        User user = new User("qwerty", "qwerty@gmail.com");
        caseRepository.save(new Case("Case1", "Case1 - description", Severity.LOW, user));
    }
}