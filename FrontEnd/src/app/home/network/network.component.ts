import { Component, OnInit } from '@angular/core';
import { FriendService } from "../../services/friend.service";
import { MemberService, TokenStorageService } from "../../services";
import { MemberInfo } from "../../models";
import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup } from "@angular/forms";

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.css']
})
export class NetworkComponent implements OnInit {

  member_email!: string
  memberInfo!: MemberInfo[]
  searchForm!: UntypedFormGroup
  email: string = ""
  searchResult: any

  constructor(private friendService: FriendService,
              private memberService: MemberService,
              private tokenStorage: TokenStorageService,
              public router: Router,
              private formBuilder: UntypedFormBuilder) {

    let formControls = {
      search: new UntypedFormControl()
    }
    this.searchForm = this.formBuilder.group(formControls)
  }

  ngOnInit(): void {

    // get member_email stored in local storage
    this.member_email = this.tokenStorage.getEmail()

    // get list of friends
    this.friendService.getFriends(this.member_email).subscribe(
      (response: MemberInfo[]) => {
        this.memberInfo = response
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }
    )
  }

  public search(): void {
    this.searchResult = this.searchForm.value
    this.memberService.checkIfEmailExists(this.searchForm.value.search).subscribe(
      (response: boolean) => {
        if (response)
          this.redirect_to_member_page(this.searchForm.value.search)
        else
          this.redirect_to_member_page('member-not-exists')
      }
    )
  }

  public redirect_to_member_page(email: string) {
    this.router.navigate(['/home/network', email]).then(
      () => {
        window.location.reload()
      }
    )
  }
}
