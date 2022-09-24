import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService, TokenStorageService } from '../services';
import { Member } from '../models';
import { FormGroup, FormBuilder, Validators, FormControl } from "@angular/forms";
import {filter, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-member-form',
  templateUrl: './member-form.component.html',
  styleUrls: ['./member-form.component.css']
})
export class MemberFormComponent implements OnInit {
  registerForm: FormGroup
  public emailAlreadyExist: boolean = false
  public isRegistered: boolean = false
  public registerFail: boolean = false
  public members!: Member[]

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private memberService: MemberService,
    private formBuilder: FormBuilder,
    private tokenStorage: TokenStorageService) {

    let formControls = {
      surname: new FormControl('', [
        Validators.required,
        Validators.pattern("[A-Za-z .'-]+"),
      ]),
      name: new FormControl('', [
        Validators.required,
        Validators.pattern("[A-Za-z .'-]+"),
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
      password: new FormControl('', [
        Validators.required
      ]),
      password2: new FormControl('', [
        Validators.required,
      ]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(10),
        Validators.pattern("[0-9]+")
      ]),
    }

    this.registerForm = this.formBuilder.group(formControls)
  }


  ngOnInit(): void {

    // if a user is already logged in, don't let another user register
    if (this.tokenStorage.getAccessToken()){
      this.redirect_to_initial()
    }
  }

  get name() { return this.registerForm.get('name') }
  get surname() { return this.registerForm.get('surname') }
  get phone() { return this.registerForm.get('phone') }
  get email() { return this.registerForm.get('email') }
  get password() { return this.registerForm.get('password') }
  get password2() { return this.registerForm.get('password2') }


  public onSubmit(): void {
    this.memberService.checkIfEmailExists(this.registerForm.controls['email'].value).pipe(
        tap(res => this.emailAlreadyExist = res),           // emailAlreadyExist becomes true if res is true, meaning notification already exists
      filter(res => !res),                                //  if res = false  , meaning notification already exists switchMap won't be called
      switchMap(() => this.memberService.registerMember(this.registerForm.value)))
      .subscribe(
        data => {
          console.log(data)
          this.isRegistered = true
          this.registerFail = false
        },
        () => {
          this.isRegistered = true
          this.registerFail = false
        }
      )
  }

  public redirect_to_initial() {
    this.router.navigate(['../home']).then()
  }
}
