package renovation.batch.service.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import renovation.batch.service.data.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
