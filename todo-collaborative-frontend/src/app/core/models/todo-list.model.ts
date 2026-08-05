export interface TodoListRequest {
  titre: string;
  description?: string;
}

export interface TodoListResponse {
  id: number;
  titre: string;
  description: string | null;
  dateCreation: string; // ISO LocalDateTime
  proprietaireId: number;
  proprietairePseudo: string;
  nbTaches: number;
  mesPermissions: string; // "OWNER" | "ONLY_READ" | "READ_WRITE" selon backend
}
