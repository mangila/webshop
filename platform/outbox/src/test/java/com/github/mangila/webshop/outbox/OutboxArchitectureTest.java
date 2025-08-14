package com.github.mangila.webshop.outbox;

import com.github.mangila.webshop.shared.SimpleJob;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class OutboxArchitectureTest {

    public static final String ROOT_INFRA = "com.github.mangila.webshop.outbox.infrastructure";

    @Test
    @DisplayName("Job classes should have suffix with Job or SimpleJob")
    void jobClassesShouldHaveSuffixWithJobOrSimpleJob() {
        JavaClasses jobs = new ClassFileImporter()
                .importPackages(ROOT_INFRA.concat(".scheduler.job"));
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
