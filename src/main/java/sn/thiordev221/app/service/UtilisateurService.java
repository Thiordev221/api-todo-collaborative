package sn.thiordev221.app.service;

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

    //Récuperer un utilisateur par son id
    UtilisateurResponse findUtilisateurById(Long id);

    //Récupérer tous les utilisateurs du système
    Page<UtilisateurResponse> findAllUtilisateurs(Pageable pageable);  

    //Récupérer un utilisateur qui est actif
    Page<UtilisateurResponse> findAllByActifTrue(Pageable pageable);

    //Récupèrer les utilisateurs dont le pseudo contient une chaîne de caractères (insensible à la casse)
    Page<UtilisateurResponse> findAllByPseudoContainingIgnoreCase(String pseudo, Pageable pageable);

    //Mettre à jour les informations d'un utilisateur
    UtilisateurResponse updateUtilisateur(Long id, UtilisateurUpdateRequest request);

    //Supprimer un utilisateur du système
    void deleteUtilisateur(Long id);
}
