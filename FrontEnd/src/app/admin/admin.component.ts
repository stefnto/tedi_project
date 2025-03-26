import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from "../services";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css'],
  standalone: false
})
export class AdminComponent implements OnInit {

  constructor(private token: TokenStorageService) { }

  ngOnInit(): void {
  }

  public logOut(): void {
    this.token.signOut();
  }

}
