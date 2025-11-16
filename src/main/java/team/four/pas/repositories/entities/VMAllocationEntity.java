package team.four.pas.repositories.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

@Getter @Setter
public class VMAllocationEntity extends IdentifiableEntity {
    @BsonProperty
    private UserEntity client;

    @BsonProperty
    private VirtualMachineEntity vm;

    @BsonProperty
    private Instant startTime;

    @BsonProperty
    private Instant endTime;
}
