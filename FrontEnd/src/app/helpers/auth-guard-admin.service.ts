import { Injectable } from '@angular/core';
import { CanActivate, Router } from "@angular/router";
import {AuthenticationService, TokenStorageService} from "../services";

@Injectable({
  providedIn: 'root'
})
export  class AuthGuardAdmin implements CanActivate {
  constructor(public authService: AuthenticationService,
              public route: Router,
              public tokenStorage: TokenStorageService) {}

  public canActivate(): boolean {
    if (!this.authService.isAuthenticated() || this.tokenStorage.getRole() !== "ROLE_ADMIN") {
      this.route.navigate(['login']).then()
      return false
    }
    return true
  }
}
