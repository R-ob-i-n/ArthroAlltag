package Therapie_Management.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

// UserDetailsServiceAutoConfiguration ausgeschlossen: wir pruefen Passwoerter selbst in
// AuthService gegen unsere eigene User-Tabelle, der von Spring Boot sonst automatisch
// erzeugte Default-User mit zufaelligem Passwort wird nirgends gebraucht.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
