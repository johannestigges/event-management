package de.tigges.eventmanagement.rest.protocol;

import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.tigges.eventmanagement.rest.protocol.jpa.ProtocolRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/protocol")
@RequiredArgsConstructor
public class ProtocolRestService {

    private final ProtocolRepository protocolRepository;

    @GetMapping("")
    public Set<Protocol> getAll() {
        return ProtocolMapper.map(protocolRepository.findAll());
    }
}
