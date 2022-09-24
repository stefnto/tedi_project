import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpErrorResponse } from '@angular/common/http';

import { AuthenticationService, TokenStorageService } from '../services';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from "rxjs/operators";

const TOKEN_HEADER_KEY = 'Authorization';       // for Spring Boot back-end

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private tokenStorage: TokenStorageService,
              private authService: AuthenticationService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;
    const token = this.tokenStorage.getAccessToken()
    const refreshToken = this.tokenStorage.getRefreshToken()

    // don't intercept register requests
    if (authReq.url.search("/register") !== -1){
      return next.handle(authReq)
    }

    // this is a refresh request, dont intercept it
    if (req.url.search("token/refresh") !== -1) {
      console.log("refresh token interception...")
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + refreshToken)})
      return next.handle(authReq);
    }

    // put the access_token in the Authorization header together with a prefix
    if (this.tokenStorage.getAccessToken()) {
      authReq = req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)})
    }

    return next.handle(authReq).pipe(catchError(error => {

      // access_token has expired, thus prompt a refresh request on the server
      if (error instanceof HttpErrorResponse && error.status === 403) {
        return this.handle403Error(authReq, next)
      } else {
        return throwError(error)
      }
    }))
  }


  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null)

  private handle403Error(request: HttpRequest<any>, next: HttpHandler) {
    let authReq = request
    console.log("refreshing: " + this.isRefreshing)
    if (!this.isRefreshing){
      console.log("started refreshing token..")
      this.isRefreshing = true
      this.refreshTokenSubject.next(null)

      return this.authService.refreshToken().pipe(
        switchMap((token: any) => {

          // token was refreshed and new access token is in token.access_token
          // not refreshing anymore so stopped requests must continue continue
          this.isRefreshing = false
          console.log("refresh_token used: " + token.refresh_token)
          this.refreshTokenSubject.next(token.refresh_token)
          console.log("access_token generated: " + token.access_token)

          // put new access_token as header and proceed with the request that was blocked
          authReq = request.clone({headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token.access_token)})
          return next.handle(authReq)

        }))

    } else {
      return this.refreshTokenSubject.pipe(

        // further requests will be stopped as long as refreshTokenSubject is null
        filter(token => token != null),
        take(1),
        switchMap(() => {
          return next.handle(authReq)
        })
      )
    }
  }



}
