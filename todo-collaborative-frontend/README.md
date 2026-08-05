# Todo Collaborative — Frontend Angular

Frontend Angular (standalone components, Signals, Tailwind CSS) pour l'API
Spring Boot [`api-todo-collaborative`](https://github.com/Thiordev221/api-todo-collaborative).

## Sécurité : où vivent les tokens ?

- **Refresh token** : cookie `httpOnly` + `Secure` + `SameSite=Strict`, posé par
  `AuthController` (backend), path restreint à `/api/auth`. Le JS Angular ne le
  voit **jamais** — invisible à `document.cookie`, donc invulnérable à un vol via
  XSS. Il n'apparaît plus du tout dans le JSON de réponse.
- **Access token** : gardé **en mémoire uniquement** (signal Angular), jamais
  persisté (ni `localStorage`, ni `sessionStorage`). Il disparaît donc à chaque
  rechargement de page — c'est voulu. Au démarrage de l'app, un
  `provideAppInitializer` appelle `/auth/refresh` silencieusement : le cookie
  part automatiquement, un nouvel access token est récupéré, la session est
  restaurée sans que l'utilisateur s'en aperçoive.
- Toutes les requêtes passent avec `withCredentials: true` (nécessaire pour
  qu'un cookie httpOnly cross-origin — `localhost:4200` → `localhost:8080` —
  parte et soit accepté).

### ⚠️ Modifications backend requises (voir conversation / commit à faire toi-même)

1. `AuthController` : pose le cookie `refreshToken` (httpOnly/secure/SameSite=Strict,
   path=`/api/auth`) sur `/register`, `/login`, `/refresh` ; le lit via `@CookieValue`
   sur `/refresh` et `/logout` ; ne renvoie plus jamais le refresh token en JSON.
2. `SecurityConfig` : ajoute un bean CORS avec `setAllowCredentials(true)` et une
   liste d'origines explicite (`http://localhost:4200` en dev) — impossible
   d'utiliser `*` dès qu'on envoie des credentials.

## Démarrage

```bash
npm install
npm start
```

L'app tourne sur `http://localhost:4200` et cible par défaut l'API sur
`http://localhost:8080/api` (voir `src/environments/environment.development.ts`).

## ⚠️ Étape backend obligatoire : CORS avec credentials

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-User-Id"));
    config.setAllowCredentials(true); // indispensable pour que le cookie httpOnly parte

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

Et dans la chaîne de filtres :

```java
.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

## Fonctionnement du header `X-User-Id`

Les contrôleurs `TodoListController`, `TacheController` et `PartageController`
attendent un header `X-User-Id` en plus du `Authorization: Bearer`. L'intercepteur
(`core/interceptors/auth.interceptor.ts`) l'ajoute automatiquement à partir de
l'`userId` renvoyé au login/register, stocké en `localStorage`.

## Structure

```
src/app/
├── core/
│   ├── models/         # Interfaces TS miroir des DTOs Java
│   ├── services/        # AuthService, TodoListService, TacheService, PartageService, UtilisateurService
│   ├── interceptors/     # auth.interceptor.ts (Bearer + X-User-Id + refresh auto sur 401)
│   └── guards/          # authGuard, adminGuard
├── features/
│   ├── auth/             # login, register
│   ├── lists/            # list-lists, list-form, list-detail (tâches + partages inclus)
│   └── admin/             # user-management (ROLE_ADMIN uniquement)
└── shared/
    └── navbar/
```

## Points d'attention

- **Refresh token automatique** : sur un 401, l'intercepteur tente un `/auth/refresh`
  puis rejoue la requête originale. Si le refresh échoue, redirection vers `/login`.
- **Permissions** : `TodoListResponse.mesPermissions` vaut `OWNER`, `EDITOR`, `VIEWER`
  ou `NONE`. Seul le `OWNER` voit la section Partages et peut modifier/supprimer la
  liste ; `OWNER`/`EDITOR` peuvent gérer les tâches ; `VIEWER` est en lecture seule.
- **Validations formulaires** : les règles (regex pseudo, complexité mot de passe,
  longueurs max) miment exactement les annotations Bean Validation du backend
  (`RegisterRequest`, `TacheCreateRequest`, etc.) pour éviter les allers-retours 400.
