package team.four.pas.repositories.entities;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;

public record VirtualMachineEntity(
        @BsonId ObjectId id,
        @BsonProperty("cpuNumber") int cpuNumber,
        @BsonProperty("ramGiB") int ramGiB,
        @BsonProperty("storageGiB") int storageGiB
) {

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualMachineEntity that = (VirtualMachineEntity) o;
        return new EqualsBuilder().append(id, that.id).isEquals();
    }
}