package team.four.pas.entities.resources;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "virtual_machines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VirtualMachineEntity {

    @Id
    private String id;

    private int cpuNumber;
    private int ramGiB;
    private int storageGiB;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualMachineEntity that = (VirtualMachineEntity) o;
        return new EqualsBuilder().append(id, that.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}