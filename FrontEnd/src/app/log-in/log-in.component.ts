import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { TokenStorageService } from "../services";
import { AuthenticationService } from "../services";


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css'],
  standalone: false
})
export class LogInComponent implements OnInit {

  loginForm: UntypedFormGroup
  public msg: string = ""
  public isLoggedIn: boolean = false
  public loginFail: boolean = false
  public errMessage: string = ""
  public roles: string[] = []

  constructor(private formBuilder: UntypedFormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthenticationService,
              private tokenStorage: TokenStorageService) {

    let formControls = {
      email: new UntypedFormControl('',
        [Validators.required, Validators.email]),
      password: new UntypedFormControl('',
        [Validators.required])
    }

    this.loginForm = this.formBuilder.group(formControls)
  }

  ngOnInit(): void {

    // if a user is already logged in, don't let another user login
    if (this.tokenStorage.getAccessToken()){
      if (this.tokenStorage.getRole() === "ROLE_ADMIN")
        this.redirect_to_Admin_home()
      else if (this.tokenStorage.getRole() === "ROLE_MEMBER")
        this.redirect_to_home()
    }
  }

  get email() { return this.loginForm.get('email') }
  get password() { return this.loginForm.get('password') }


  public login(): void {
    this.authService.loginMember(this.loginForm.value).subscribe(
      () => {
        this.loginFail = false
        this.isLoggedIn = true;
        if (this.tokenStorage.getRole() ===  "ROLE_ADMIN"){
          this.redirect_to_Admin_home()
        } else {
          this.redirect_to_home()
        }
        },
      err => {
        this.msg = "Bad credentials, please try again"
        this.errMessage = err
        this.loginFail = true
      }
     )
  }

  public redirect_to_home() {
    this.router.navigate(['../home']).then()
  }

  public redirect_to_Admin_home() {
    this.router.navigate(['../administration-page']).then()
  }
}
