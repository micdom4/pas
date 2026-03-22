package team.four.pas.data.allocations;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Client;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class VMAllocation {

    private String id;

    private Client client;

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