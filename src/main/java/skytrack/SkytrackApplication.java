package skytrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SkytrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkytrackApplication.class, args);
    }

}
