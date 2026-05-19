package sn.thiordev221.app.service.contrats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.responses.PartageResponse;

public interface PartageService {
    /**
     * Invite un utilisateur à collaborer sur une liste de tâches, en vérifiant que l'utilisateur qui fait la demande a le droit de partager cette liste.
     * @param listId L'identifiant de la liste à partager.
     * @param inviteId L'identifiant de l'utilisateur à inviter.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @return PartageResponse Le partage créé.
     */
    PartageResponse inviterUtilisateur(Long listId, Long inviteId, Long currentUserId);

    /**
     * Supprime un partage entre deux utilisateurs, en vérifiant que l'utilisateur qui fait la demande a le droit de révoquer ce partage.
     * @param listId L'identifiant de la liste dont le partage doit être révoqué.
     * @param inviteId L'identifiant de l'utilisateur dont le partage doit être révoqué.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     */
    void revoquerPartage(Long listId, Long inviteId, Long currentUserId);

    /**
     * Récupère tous les partages d'une liste de tâches, en vérifiant que l'utilisateur a le droit d'accéder à cette liste.
     * @param listId L'identifiant de la liste dont on veut récupérer les partages.
     * @param currentUserId L'identifiant de l'utilisateur qui fait la demande.
     * @param pageable Les informations de pagination.
     * @return Page<PartageResponse> La page de partages correspondante.
     */
    Page<PartageResponse> getPartagesDeMaListe(Long listId, Long currentUserId, Pageable pageable);
}
