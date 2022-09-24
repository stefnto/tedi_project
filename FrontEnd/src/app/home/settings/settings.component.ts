import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from "@angular/forms";
import { MemberService, TokenStorageService } from "../../services";
import { filter, switchMap, tap } from "rxjs/operators";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  public emailAlreadyExist: boolean = false
  emailUpdated = false;
  newEmail = new FormControl('', [
    Validators.email, Validators.required
  ])

  passwordUpdated = false;
  passwordUpdateError !: string
  newPassword = new FormControl('', [Validators.required])

  constructor(private memberService: MemberService,
              private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
  }

  updateEmail(): void {
    this.memberService.checkIfEmailExists(this.newEmail.value).pipe(
      tap(res => this.emailAlreadyExist = res),                      // emailAlreadyExist becomes true if res is true, meaning notification already exists
      filter(res => !res),                                        //  if res = false  , meaning notification already exists switchMap won't be called
      switchMap(() => this.memberService.updateEmail(this.tokenStorage.getEmail(),this.newEmail.value)))
      .subscribe(
        () => {
          this.emailUpdated = true
          this.tokenStorage.saveEmail(this.newEmail.value)
        },
        () => {
          this.emailUpdated = false
        }
      )

  }

  updatePassword(): void {
    this.memberService.updatePassword(this.tokenStorage.getEmail(), this.newPassword.value).subscribe(
      (response: string) => {
          if (response === "Password changed successfully"){
            this.passwordUpdated = true
          } else {
            this.passwordUpdateError = "You are trying to change another user's password, access denied"
          }
      }
    )
  }

}
