package de.tigges.eventmanagement.rest.protocol;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/protocol")
@RequiredArgsConstructor
public class ProtocolRestService {

    private final ProtocolRepository protocolRepository;

    @GetMapping("")
    public List<Protocol> getAll() {
        return protocolRepository.findAllByOrderByCreatedAt();
    }
}
