package team.four.pas.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.four.pas.mappers.AllocationMapper;
import team.four.pas.repositories.AllocationRepository;

@Component
@RequiredArgsConstructor
public class AllocationAdapter {
    private final AllocationRepository repository;
    private final AllocationMapper mapper;

}
