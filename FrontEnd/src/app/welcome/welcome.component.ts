import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { TokenStorageService  } from "../services";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
  standalone: false
})
export class WelcomeComponent implements OnInit {

  constructor(private router:Router,
              private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    // if a user is already logged in, don't let another user login
    if (this.tokenStorage.getAccessToken()){
      if (this.tokenStorage.getRole() === "ROLE_ADMIN")
        this.redirect_to_Admin_home()
      else if (this.tokenStorage.getRole() === "ROLE_MEMBER")
        this.redirect_to_home()
    }

  }

  goToPage(pageName: string): void {
    this.router.navigate([`${pageName}`]).then();
  }

  public redirect_to_home() {
    this.router.navigate(['../home']).then()
  }

  public redirect_to_Admin_home() {
    this.router.navigate(['../administration-page']).then()
  }
}
