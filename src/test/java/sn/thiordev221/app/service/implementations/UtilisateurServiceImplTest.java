package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.requests.UtilisateurUpdateRequest;
import sn.thiordev221.app.dto.responses.UtilisateurResponse;
import sn.thiordev221.app.mapper.UtilisateurMapper;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @Test
    void shouldReturnUtilisateurResponseWhenUtilisateurExists() {
        var utilisateur = Utilisateur.builder()
            .id(1L)
            .email("test@example.com")
            .pseudo("testuser")
            .password("secret")
            .actif(true)
            .build();

        var response = new UtilisateurResponse(1L, "test@example.com", "testuser", null, true, utilisateur.getDateCreation());

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurMapper.toResponse(utilisateur)).thenReturn(response);

        var result = utilisateurService.getUtilisateurById(1L);

        assertThat(result).isEqualTo(response);
        verify(utilisateurRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUtilisateurDoesNotExist() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UtilisateurNotFoundException.class, () -> utilisateurService.getUtilisateurById(1L));
    }

    @Test
    void shouldUpdateUtilisateurWhenRequestIsValid() {
        var utilisateur = Utilisateur.builder()
            .id(2L)
            .email("user@example.com")
            .pseudo("oldPseudo")
            .password("oldpwd")
            .actif(true)
            .build();

        var request = new UtilisateurUpdateRequest("newPseudo", "newPassword");
        var updatedUtilisateur = Utilisateur.builder()
            .id(2L)
            .email("user@example.com")
            .pseudo("newPseudo")
            .password("newPassword")
            .actif(true)
            .build();

        var response = new UtilisateurResponse(2L, "user@example.com", "newPseudo", null, true, updatedUtilisateur.getDateCreation());

        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurRepository.save(utilisateur)).thenReturn(updatedUtilisateur);
        when(utilisateurMapper.toResponse(updatedUtilisateur)).thenReturn(response);

        var result = utilisateurService.updateUtilisateur(2L, request);

        assertThat(result).isEqualTo(response);
    }
}
