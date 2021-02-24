package mk.napijse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mk.napijse.model.entities.Event;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
