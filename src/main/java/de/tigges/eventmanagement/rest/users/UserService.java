package de.tigges.eventmanagement.rest.users;

import de.tigges.eventmanagement.rest.events.ParticipantRepository;
import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import lombok.RequiredArgsConstructor;
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
        return instrumentRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userRepository.findUserWithInstrument(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping("")
    @ResponseBody
    public User create(@RequestBody User user) {
        var savedUser = userRepository.insert(user);
        protocolService.newEntity(savedUser.getId(), "User", savedUser);
        return savedUser;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public User update(@RequestBody User user, @PathVariable Long id) {
        var savedUser = userRepository.update(user);
        protocolService.modifiedEntity(savedUser.getId(), "User", savedUser);
        return savedUser;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        participantRepository.removeFromUser(id);
        userRepository.deleteById(id);
        protocolService.deletedEntity(id, "User");
    }
}
