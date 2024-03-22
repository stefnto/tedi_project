import { Injectable } from '@angular/core';
import { Router } from "@angular/router";
import { AuthenticationService } from "../services";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(public authService: AuthenticationService,
              public route: Router) { }

  public canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.route.navigate(['login']).then()
      return false
    }
    return true
  }

}
