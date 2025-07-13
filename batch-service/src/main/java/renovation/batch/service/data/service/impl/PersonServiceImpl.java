/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.batch.service.data.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import renovation.batch.service.data.entity.Person;
import renovation.batch.service.data.service.PersonService;

@Service
public class PersonServiceImpl extends AbstractServiceImpl<Person, Long> implements PersonService {

    public PersonServiceImpl(JpaRepository<Person, Long> repository) {
        super(repository);
    }
}
