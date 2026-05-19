package sn.thiordev221.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.responses.PartageResponse;

public interface PartageService {
    //Creer un partage ou inviter un utilisateur à consulter ses todoLists
    PartageResponse inviterUtilisateur(Long listId, Long inviteId, Long currentUserId);

    //Supprimer un partage entre deux utilisateurs
    void revoquerPartage(Long listId, Long inviteId, Long currentUserId);

    //Récupèrer toutes les partages de l'utilisateur connecté
    Page<PartageResponse> getPartagesDeMaListe(Long listId, Long currentUserId, Pageable pageable);
}
