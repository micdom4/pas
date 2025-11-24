package team.four.pas.services.data.resources;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@ToString
public class VirtualMachine {
    private String id;
    private int cpuNumber;
    private int ramGiB;
    private int storageGiB;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VirtualMachine that = (VirtualMachine) o;

        return new EqualsBuilder().append(id, that.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
