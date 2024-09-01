package de.tigges.eventmanagement.rest.users;

import de.tigges.eventmanagement.rest.events.ParticipantRepository;
import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return userRepository.selectAllWithInstruments();
    }

    @GetMapping("/instruments")
    public List<Instrument> getInstruments() {
        return instrumentRepository.findAllByOrderByIdAsc();
    }


    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userRepository.findUserWithInstrument(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(
                        "User with id %s not found".formatted(id), 1));
    }

    @PostMapping("")
    @ResponseBody
    public User create(@RequestBody User user) {
        return protocolService.newEntity(userRepository.insert(user));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User update(@RequestBody User user, @PathVariable Long id) {
        return protocolService.modifiedEntity(userRepository.update(user));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        participantRepository.removeFromUser(id);
        userRepository.deleteById(id);
        protocolService.deletedEntity(id, "User");
    }
}
