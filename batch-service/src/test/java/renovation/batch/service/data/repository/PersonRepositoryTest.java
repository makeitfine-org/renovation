/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.data.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import renovation.batch.service.configs.PostgresSQLContainerConfigs;
import renovation.batch.service.data.entity.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integrationTest")
@SpringBootTest
@ContextConfiguration(classes = PostgresSQLContainerConfigs.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Slf4j
@AllArgsConstructor
class PersonRepositoryTest {

    private final PersonRepository personRepository;

    @Test
    void save_findAll_Success() {
        var p = new Person();
        p.setFirstName("John");
        p.setMiddleName("Torrontovych");
        p.setLastName("Timotov");
        p.setAddress("USA, New-York, Some street 5/12");

        personRepository.save(p);

        var findAll = personRepository.findAll();
        findAll.forEach(person -> assertEquals(
                        person,
                        Person.builder()
                                .firstName("John")
                                .middleName("Torrontovych")
                                .lastName("Timotov")
                                .address("USA, New-York, Some street 5/12")
                                .build()
                )
        );
    }
}
