package pheninux.xdev.gestork;

import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("pheninux.xdev.gestork")
public class GestorkApplication {


    public static void main(String[] args) {
        SpringApplication.run(GestorkApplication.class, args);
    }

    @Bean
    public Faker faker() {
        return new Faker();
    }

}
