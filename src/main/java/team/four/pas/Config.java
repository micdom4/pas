package team.four.pas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.four.pas.data.users.Admin;
import team.four.pas.repositories.LocalResourceRepository;
import team.four.pas.repositories.LocalUserRepository;

@Configuration
public class Config {

    @Bean
    public LocalUserRepository localUserRepository(){
        var localUserRepository = new LocalUserRepository();

        localUserRepository.add("BLis", "Bartosz", "Lis", Admin.class);

        return localUserRepository;
    }

    @Bean
    public LocalResourceRepository localResourceRepository(){
        var localResourceRepository = new LocalResourceRepository();

        localResourceRepository.addVM(1, 1, 10);

        return localResourceRepository;
    }

}
