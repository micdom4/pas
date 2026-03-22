package team.four.pas;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.Manager;

@AllArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthService authService;
    private final ResourceService resourceService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        System.out.println("Starting Data Initialization...");

        if (userService.findByMatchingLogin("SSigma").isEmpty()) {
            authService.register(new Admin(null, "SSigma", "isrp6", "Super", "Sigma", true));
        }

        if (userService.findByMatchingLogin("TAnderson").isEmpty()) {
            authService.register(new Manager(null, "TAnderson", "isrp5", "Thomas", "Anderson", true));
        }

        if (userService.findByMatchingLogin("HKwinto").isEmpty()) {
            authService.register(new Client(null, "HKwinto", "isrp4", "Henryk", "Kwinto", true));
        }

        if (resourceService.getAll().isEmpty()) {
            resourceService.addVM(new VirtualMachine(null, 4, 8, 128));
            resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
            resourceService.addVM(new VirtualMachine(null, 10, 32, 1024));
        }

        System.out.println("Data has been successfully initialized.");
    }
}