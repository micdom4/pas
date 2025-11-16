package team.four.pas.services.data.users;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import team.four.pas.services.data.resources.VirtualMachine;


@Getter  @Setter @NoArgsConstructor @ToString
public abstract class User  {
    private String id;
    private String login;
    private String name;
    private String surname;
    private boolean active;

    public User(String id, String login, String name, String surname, boolean active) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User that = (User) o;

        return new EqualsBuilder().append(id, that.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

}
