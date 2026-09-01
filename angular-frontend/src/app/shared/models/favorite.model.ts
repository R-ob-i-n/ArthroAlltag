export interface Favorite {
  id: number;
  tippId: number;
  hinzugefuegtAm: string;
}

export interface FavoriteCreateRequest {
  tippId: number;
}
