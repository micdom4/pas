package team.four.pas.services.data.allocations;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Document(collection = "vm_allocations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class VMAllocation {

    @Id
    private String id;

    @DocumentReference
    private Client client;

    @DocumentReference
    private VirtualMachine vm;

    private Instant startTime;
    private Instant endTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VMAllocation that = (VMAllocation) o;
        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}