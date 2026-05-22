package sn.thiordev221.app;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sn.thiordev221.app.model.Role;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.UtilisateurRepository;

@SpringBootApplication
public class ApiTodoCollaborativeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTodoCollaborativeApplication.class, args);
	}

	@Bean
public CommandLineRunner commandLineRunner(UtilisateurRepository utilisateurRepository) {
    return args -> {

        if (utilisateurRepository.count() > 0) return; // évite les doublons au redémarrage

        Utilisateur admin = Utilisateur.builder()
            .email("admin@todo.sn")
            .password("admin123")
            .pseudo("Admin")
            .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER))
            .actif(true)
            .build();

        Utilisateur abdoulaye = Utilisateur.builder()
            .email("abdoulaye@todo.sn")
            .password("pass123")
            .pseudo("Abdoulaye")
            .roles(Set.of(Role.ROLE_USER))
            .actif(true)
            .build();

        Utilisateur fatou = Utilisateur.builder()
            .email("fatou@todo.sn")
            .password("pass123")
            .pseudo("Fatou")
            .roles(Set.of(Role.ROLE_USER))
            .actif(true)
            .build();

        Utilisateur moussa = Utilisateur.builder()
            .email("moussa@todo.sn")
            .password("pass123")
            .pseudo("Moussa")
            .roles(Set.of(Role.ROLE_USER))
            .actif(true)
            .build();

        Utilisateur aminata = Utilisateur.builder()
            .email("aminata@todo.sn")
            .password("pass123")
            .pseudo("Aminata")
            .roles(Set.of(Role.ROLE_USER))
            .actif(true)
            .build();

        utilisateurRepository.saveAll(List.of(admin, abdoulaye, fatou, moussa, aminata));
        System.out.println("✅ 5 utilisateurs enregistrés avec succès.");
    };
}

}
