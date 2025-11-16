package team.four.pas.repositories.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.Instant;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.time.Instant;
import java.util.UUID;

public record VMAllocationEntity(
        @BsonId UUID id,
        @BsonProperty("client") UserEntity client,
        @BsonProperty("vm") VirtualMachineEntity vm,
        @BsonProperty("startTime") Instant startTime,
        @BsonProperty("endTime") Instant endTime
) {

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VMAllocationEntity that = (VMAllocationEntity) o;
        return new EqualsBuilder().append(id, that.id).isEquals();
    }
}