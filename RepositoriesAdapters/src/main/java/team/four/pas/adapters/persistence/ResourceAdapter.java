package team.four.pas.adapters.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.four.pas.mappers.ResourceMapper;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.inside.ResourcePersistencePort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceAdapter implements ResourcePersistencePort {
    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public List<VirtualMachine> findAll() {
        return mapper.entityToDomain(repository.findAll());
    }

    @Override
    public Optional<VirtualMachine> findById(String id) {
        return Optional.ofNullable(
                mapper.entityToDomain(repository.findById(id).orElse(null))
        );
    }

    @Override
    public VirtualMachine insert(VirtualMachine vm) {
        return mapper.entityToDomain(repository.insert(
                mapper.domainToEntity(vm)
        ));
    }

    @Override
    public void updateById(String id, int cpuNumber, int ramGiB, int storageGiB) {
        repository.updateById(id, cpuNumber, ramGiB, storageGiB);
    }
}
