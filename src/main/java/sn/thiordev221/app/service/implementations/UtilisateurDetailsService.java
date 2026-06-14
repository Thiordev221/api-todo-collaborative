package sn.thiordev221.app.service.implementations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
public class UtilisateurDetailsService implements UserDetailsService{

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur", "email", email));
    }
    
}
