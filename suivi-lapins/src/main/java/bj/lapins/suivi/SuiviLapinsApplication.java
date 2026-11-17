package bj.lapins.suivi;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SuiviLapinsApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Porto-Novo"));
        SpringApplication.run(SuiviLapinsApplication.class, args);
    }
}
