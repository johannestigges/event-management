package de.tigges.eventmanagement.rest.users;

import de.tigges.eventmanagement.rest.events.ParticipantRepository;
import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/users")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final ParticipantRepository participantRepository;
    private final ProtocolService protocolService;

    @GetMapping("")
    public List<User> getAll() {
        var instruments = instrumentRepository.findAllByOrderByIdAsc().stream()
                .collect(Collectors.toMap(Instrument::id, Function.identity()));
        return userRepository.findAllByOrderByInstrumentIdAsc().stream()
                .map(user -> user.withInstrument(instruments.get(user.instrumentId())))
                .toList();
    }

    @GetMapping("/instruments")
    public List<Instrument> getInstruments() {
        return instrumentRepository.findAllByOrderByIdAsc();
    }


    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(this::addInstrument)
                .orElseThrow(() -> new EmptyResultDataAccessException(
                        "User with id %s not found".formatted(id), 1));
    }

    @PostMapping("")
    @ResponseBody
    public User create(@RequestBody User user) {
        var saved = userRepository.insert(toEntity(user.asNew()));
        return protocolService.newEntity(addInstrument(saved));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User update(@RequestBody User user, @PathVariable Long id) {
        var saved = userRepository.update(toEntity(user));
        return protocolService.modifiedEntity(addInstrument(saved));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        participantRepository.removeFromUser(id);
        userRepository.deleteById(id);
        protocolService.deletedEntity(id, "User");
    }

    private User toEntity(User user) {
        return user.instrument() == null
                ? user.withInstrumentId(null)
                : user.withInstrumentId(user.instrument().id());
    }

    private User addInstrument(User user) {
        if (user.instrumentId() == null) {
            return user;
        }
        return instrumentRepository.findById(user.instrumentId())
                .map(user::withInstrument)
                .orElse(user);
    }
}
