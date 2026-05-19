package sn.thiordev221.app.service.contrats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sn.thiordev221.app.dto.requests.UtilisateurUpdateRequest;
import sn.thiordev221.app.dto.responses.UtilisateurResponse;

/**
 * Service pour gérer les utilisateurs de l'application.
 * Ce service peut inclure des méthodes pour créer, lire, mettre à jour et supprimer 
 * des utilisateurs, ainsi que pour gérer l'authentification et les rôles des utilisateurs.
 */
public interface UtilisateurService {

    /**
     * Récupère un utilisateur par son identifiant.
     * @param id L'identifiant de l'utilisateur.
     * @return UtilisateurResponse L'utilisateur correspondant.
     */
    UtilisateurResponse getUtilisateurById(Long id);

    /**
     * Récupère tous les utilisateurs du système.
     * @param pageable Les informations de pagination.
     * @return Page<UtilisateurResponse>
     */
    Page<UtilisateurResponse> getAllUtilisateurs(Pageable pageable);  

    /**
     * Récupère tous les utilisateurs qui sont actifs.
     * @param pageable Les informations de pagination.
     * @return Page<UtilisateurResponse>
     */
    Page<UtilisateurResponse> getAllByActifTrue(Pageable pageable);

    //Récupèrer les utilisateurs dont le pseudo contient une chaîne de caractères (insensible à la casse)
    /**
     * Récupère tous les utilisateurs dont le pseudo contient une chaîne de caractères (insensible à la casse).
     * @param pseudo La chaîne de caractères à rechercher dans les pseudos des utilisateurs.
     * @param pageable Les informations de pagination.
     * @return Page<UtilisateurResponse>
     */
    Page<UtilisateurResponse> getAllByPseudoContainingIgnoreCase(String pseudo, Pageable pageable);

     /**
      * Met à jour les informations d'un utilisateur.
      * @param id L'identifiant de l'utilisateur à mettre à jour.
      * @param request Les nouvelles informations de l'utilisateur.
      * @return UtilisateurResponse L'utilisateur mis à jour.
      */
    UtilisateurResponse updateUtilisateur(Long id, UtilisateurUpdateRequest request);

    /**
     * Supprime un utilisateur du système.
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    void deleteUtilisateur(Long id);
}
