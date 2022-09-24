import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";

const ACCESS_TKN = 'access_token'
const REFRSH_TKN = 'refresh_token'
const USER_KEY = 'user_email'
const USER_RLS = 'user_role'


@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private readonly membersUrl: string

  constructor() {
    this.membersUrl = `${environment.baseUrl}/api`;
  }

  signOut(): void {
    window.localStorage.clear()
  }

  public saveToken(accessToken: string, refreshToken:string): void {
    window.localStorage.removeItem(ACCESS_TKN)
    window.localStorage.setItem(ACCESS_TKN, accessToken)

    window.localStorage.removeItem(REFRSH_TKN)
    window.localStorage.setItem(REFRSH_TKN, refreshToken)
  }

  public saveEmail(email: string): void {
    window.localStorage.removeItem(USER_KEY)
    window.localStorage.setItem(USER_KEY, email)
  }

  public saveRole(roles: string[]): void{
    window.localStorage.removeItem(USER_RLS)
    window.localStorage.setItem(USER_RLS, JSON.stringify(roles).replace('[', '')
                                                                .replace(']', '')
                                                                .replace('"', '')
                                                                .replace('"', ''))
  }

  public getAccessToken(): any {
    return window.localStorage.getItem(ACCESS_TKN)
  }

  public getRefreshToken(): any {
    return window.localStorage.getItem(REFRSH_TKN)
  }

  public getEmail(): any{
    return window.localStorage.getItem(USER_KEY)
  }

  public getRole(): any{
    return window.localStorage.getItem(USER_RLS)
  }
}
