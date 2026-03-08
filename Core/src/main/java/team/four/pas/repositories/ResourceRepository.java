package team.four.pas.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.services.data.resources.VirtualMachine;

public interface ResourceRepository extends MongoRepository<VirtualMachine, String> {

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'cpuNumber' : ?1, 'ramGiB' : ?2, 'storageGib': ?3} }")
    void updateById(String id, int cpuNumber, int ramGiB, int storageGiB);
}

