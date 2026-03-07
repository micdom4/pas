package team.four.pas.services.data.allocations;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@ToString
public class VMAllocation {
    private String id;
    private Client client;
    private Instant startTime;
    private Instant endTime;
    private VirtualMachine vm;

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
