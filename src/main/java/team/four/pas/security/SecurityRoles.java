package team.four.pas.security;

public class SecurityRoles {

    private SecurityRoles() {
        throw new UnsupportedOperationException();
    }

    public final static String CLIENT = "ROLE_CLIENT";
    public final static String MANAGER = "ROLE_MANAGER";
    public final static String ADMIN = "ROLE_ADMIN";
}
