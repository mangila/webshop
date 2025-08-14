package com.github.mangila.webshop.outbox;

import com.github.mangila.webshop.shared.SimpleJob;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class OutboxArchitectureTest {

    @Test
    @DisplayName("Should have job classes ending with Job or SimpleJob")
    void jobClassesShouldEndWithJobOrSimpleJob() {
        JavaClasses jobs = new ClassFileImporter()
                .importPackages("com.github.mangila.webshop.outbox.infrastructure.scheduler.job");
        ArchRuleDefinition.classes()
                .that()
                .implement(SimpleJob.class)
                .should()
                .haveSimpleNameEndingWith("Job")
                .orShould()
                .haveSimpleNameEndingWith("SimpleJob")
                .check(jobs);
    }

}
