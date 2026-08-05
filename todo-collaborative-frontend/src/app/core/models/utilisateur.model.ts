export type Role = 'ROLE_USER' | 'ROLE_ADMIN';

export interface UtilisateurUpdateRequest {
  pseudo?: string;
  password?: string;
}

export interface UtilisateurResponse {
  id: number;
  email: string;
  pseudo: string;
  roles: Role[];
  actif: boolean;
  dateCreation: string;
}
