package de.tigges.eventmanagement.rest.users;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.tigges.eventmanagement.rest.protocol.ProtocolService;
import de.tigges.eventmanagement.rest.users.jpa.InstrumentRepository;
import de.tigges.eventmanagement.rest.users.jpa.UserEntity;
import de.tigges.eventmanagement.rest.users.jpa.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/users")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final ProtocolService protocolService;

    @GetMapping("")
    List<User> getAll() {
        return UserMapper.mapEntities(userRepository.selectAllWithInstruments());
                
    }

    @GetMapping("/instruments")
    List<Instrument> getInstruments() {
        return UserMapper.mapInstruments(instrumentRepository.findAll(Sort.by(Sort.Direction.ASC,"id")));
    }

    @GetMapping("/{id}")
    User getOne(@PathVariable Long id) {
        return userRepository.findOneWithInstrument(id)
                .map(e -> UserMapper.mapEntity(e))
                .orElseThrow(() -> new RuntimeException());
    }

    @PostMapping("")
    @ResponseBody
    User create(@RequestBody User user) {
        UserEntity entity = UserMapper.map(user);
        userRepository.save(entity);
        protocolService.newEntity(entity.getId(), "User", entity);
        return UserMapper.mapEntity(entity);
    }

    @PutMapping("/{id}")
    @ResponseBody
    User update(@RequestBody User user, @PathVariable Long id) {
        UserEntity entity = UserMapper.map(user);
        userRepository.save(entity);
        protocolService.modifiedEntity(entity.getId(), "User", entity);
        return UserMapper.mapEntity(entity);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        protocolService.deletedEntity(id, "User");
    }
}
