package sn.thiordev221.app.dto.responses;

import java.util.List;

import sn.thiordev221.app.model.Role;

public record AuthResponse(
    String token,
    String type, // "Bearer"
    Long userId,
    String email,
    String pseudo,
    List<Role> roles
) {
    // Petit constructeur pratique pour fixer le type par défaut
    public AuthResponse(String token, Long userId, String email, String pseudo, List<Role> roles) {
        this(token, "Bearer", userId, email, pseudo, roles);
  } 
    
}
