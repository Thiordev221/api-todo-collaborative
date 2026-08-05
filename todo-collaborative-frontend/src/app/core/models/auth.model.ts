export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  pseudo: string;
  email: string;
  password: string;
}

export interface RefreshRequest {
  refreshToken: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string | null; // toujours null désormais — transporté par cookie httpOnly, jamais exposé en JSON
  tokenType: string; // "Bearer"
  userId: number;
  email: string;
  pseudo: string;
  roles: string[]; // ex: ["ROLE_USER", "ROLE_ADMIN"]
}
