// Reflète org.springframework.data.domain.Page côté Spring
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;      // page courante (0-indexed)
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
