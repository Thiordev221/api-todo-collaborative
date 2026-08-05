export type Permission = 'ONLY_READ' | 'READ_WRITE';

export interface PartageRequest {
  inviteEmail: string;
  permission: Permission;
}

export interface PartageResponse {
  id: number;
  todoListId: number;
  todoListTitre: string;
  inviteEmail: string;
  invitePseudo: string;
  permission: Permission;
  datePartage: string;
}
