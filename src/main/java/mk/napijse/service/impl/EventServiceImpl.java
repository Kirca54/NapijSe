package mk.napijse.service.impl;

import mk.napijse.model.entities.Event;
import mk.napijse.model.entities.User;
import mk.napijse.model.exceptions.UserNotFoundException;
import mk.napijse.repository.EventRepository;
import mk.napijse.repository.UserRepository;
import mk.napijse.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Event> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate, String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return this.eventRepository.findByStartGreaterThanEqualAndFinishLessThanEqualAndUser(startDate, endDate, user);
    }

    @Override
    public List<Event> findAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public Event save(Event event) {
        return this.eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Event event) {
        this.eventRepository.delete(event);
    }

    @Override
    public List<Event> findAllByUser(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return this.eventRepository.findAllByUser(user);
    }
}
