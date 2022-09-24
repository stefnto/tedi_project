import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from "../../environments/environment";
import { Credentials } from "./member.service";
import { tap } from "rxjs/operators";
import { JwtHelperService } from "@auth0/angular-jwt";
import { TokenStorageService } from "./token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private memberSubject: BehaviorSubject<Response>
  public member: Observable<Response>
  private readonly membersUrl: string

  constructor(private http: HttpClient,
              private tokenStorage: TokenStorageService) {
    this.membersUrl = `${environment.baseUrl}/api`
    // @ts-ignore
    this.memberSubject = new BehaviorSubject<Response>(null)
    this.member = this.memberSubject.asObservable()

  }


  public loginMember(credentials: Credentials): Observable<any> {
    const params = new HttpParams()
      .set('email', credentials.email)
      .set('password', credentials.password)
    return this.http.post<any>(`${this.membersUrl}/login`, params.toString(),
      {
        headers: new HttpHeaders()
          .set('Content-Type', 'application/x-www-form-urlencoded')
      })
      .pipe(
        tap((response: Response) => {
          const helper = new JwtHelperService();
          const decodedToken = helper.decodeToken(response.access_token);

          // get notification from encodedToken and save it to local storage
          // do the same for the role
          // and then save access/refresh token
          this.tokenStorage.saveEmail(decodedToken.sub)
          this.tokenStorage.saveRole(decodedToken.roles)
          this.tokenStorage.saveToken(response.access_token, response.refresh_token)
        })
    )
  }

  public refreshToken(): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/token/refresh`)
      .pipe(
        tap((response: Response) => {
          this.tokenStorage.saveToken(response.access_token, response.refresh_token)
        })
      );
  }

  // checks if access_token is expired or not
  public isAuthenticated(): boolean {
    const helper = new JwtHelperService();
    return !helper.isTokenExpired(this.tokenStorage.getAccessToken())
  }

}

export interface Response{
  access_token: string;
  refresh_token:    string;
}
