package team.four.pas.repositories.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter @Setter
public class VirtualMachineEntity extends IdentifiableEntity {

    @BsonProperty
    private int cpuNumber;

    @BsonProperty
    private int ramGiB;

    @BsonProperty
    private int storageGiB;
}
