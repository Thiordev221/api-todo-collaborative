export interface TacheCreateRequest {
  titre: string;
  description?: string;
  echeance?: string | null; // ISO LocalDateTime
}

export interface TacheUpdateRequest {
  titre: string;
  description?: string;
  echeance?: string | null;
}

export interface TacheResponse {
  id: number;
  titre: string;
  description: string | null;
  termine: boolean;
  dateCreation: string;
  echeance: string | null;
  todoListId: number;
}
