package sn.thiordev221.app.service.implementations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.requests.UtilisateurUpdateRequest;
import sn.thiordev221.app.dto.responses.UtilisateurResponse;
import sn.thiordev221.app.mapper.UtilisateurMapper;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.UtilisateurRepository;
import sn.thiordev221.app.service.contrats.UtilisateurService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService{

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    
    @Override
    @Transactional(readOnly = true)
    public UtilisateurResponse getUtilisateurById(Long id) {
        log.info("Récupération de l'utilisateur avec l'id : {}", id);
        Utilisateur found = utilisateurRepository.findById(id)
                .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur", "id", id));
        
        log.info("Utilisateur trouvé : {}", found.getPseudo());
        return utilisateurMapper.toResponse(found);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilisateurResponse> getAllUtilisateurs(Pageable pageable) {
        log.info("Récupération de tous les utilisateurs avec pagination : page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Utilisateur> utilisateursPage = utilisateurRepository.findAll(pageable);
        return utilisateursPage.map(utilisateurMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilisateurResponse> getAllByActifTrue(Pageable pageable) {
        log.info("Récupération des utilisateurs actifs avec pagination : page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Utilisateur> utilisateursPage = utilisateurRepository.findAllByActifTrue(pageable);
        return utilisateursPage.map(utilisateurMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UtilisateurResponse> getAllByPseudoContainingIgnoreCase(String pseudo, Pageable pageable) {
        log.info("Récupération des utilisateurs avec le pseudo contenant '{}' avec pagination : page {}, size {}", pseudo, pageable.getPageNumber(), pageable.getPageSize());
        Page<Utilisateur> utilisateursPage = utilisateurRepository.findAllByPseudoContainingIgnoreCase(pseudo, pageable);
        return utilisateursPage.map(utilisateurMapper::toResponse);
    }

    @Override
    @Transactional
    // A faire plus tard lors de Spring Security : Hasher le mot de passe que l'utilisateur veut mettre à jour
    public UtilisateurResponse updateUtilisateur(Long id, UtilisateurUpdateRequest request) {
        log.info("Mise à jour de l'utilisateur avec l'id : {}", id);
        Utilisateur found = utilisateurRepository.findById(id)
                .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur", "id", id));

        utilisateurMapper.updateUtilisateurFromRequest(request, found);
        Utilisateur updated = utilisateurRepository.save(found);
        return utilisateurMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteUtilisateur(Long id) {
        log.info("Suppression de l'utilisateur avec l'id : {}", id);
        Utilisateur found = utilisateurRepository.findById(id)
                .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur", "id", id));
        utilisateurRepository.delete(found);
        log.info("Utilisateur avec l'id {} supprimé avec succès", id);
    }
    
}
