import { Component, OnDestroy, OnInit } from '@angular/core';
import { MemberProfileService } from "../../../services/member-profile.service";
import { ActivatedRoute } from "@angular/router";
import { MemberService, TokenStorageService } from "../../../services";
import { FriendService } from "../../../services/friend.service";
import { MemberInfo } from "../../../models";
import { Education } from "../../../models/education";
import { Experience } from "../../../models/experience";
import { Skills } from "../../../models/skills";
import { Resume } from "../../../models/resume";
import { Observable } from 'rxjs';

@Component({
  selector: 'app-member-page',
  templateUrl: './member-page.component.html',
  styleUrls: ['./member-page.component.css'],
  standalone: false
})
export class MemberPageComponent implements OnInit, OnDestroy {
  private sub: any

  public loggedinMemberEmail: any

  // information about the member that it's profile is being displayed
  public memberPageEmail: any
  public memberExists!: boolean
  public memberCV!: Resume
  public areFriends: any
  public searchedMember!: MemberInfo

  searchedMember$!: Observable<MemberInfo>

  public education!: Education
  public experience!: Experience
  public skills!: Skills

  public requestSent: any

  constructor(private memberProfileService: MemberProfileService,
              private memberService: MemberService,
              private route: ActivatedRoute,
              private tokenStorage: TokenStorageService,
              private friendService: FriendService) {
  }

  ngOnInit(): void {
    // get notification of logged in member
    this.loggedinMemberEmail = this.tokenStorage.getEmail()


    // get notification from route params and put it to session storage
    this.sub = this.route.params.subscribe(params => {
      this.memberPageEmail = params['notification']
      this.memberPageEmail = JSON.stringify(this.memberPageEmail).replace("[", "")
        .replace("'", "")
        .replace("'", "")
        .replace("'", "")
        .replace('"', "")
        .replace(',', "")
        .replace('"', "")
        .replace("]", "")
        .replace(" ", "")
    })

    if (this.memberPageEmail !== 'member-not-exists') {
      this.memberExists = true

      this.memberProfileService.saveEmail(this.memberPageEmail)

      this.searchedMember$ = this.memberService.getMemberInfoByEmail(this.memberPageEmail);
      this.memberService.getMemberInfoByEmail(this.memberPageEmail).subscribe(
        (response: MemberInfo) => {
          console.log(response);
          this.searchedMember = response
        }
      )
      // get CV from backend and put it to session storage
      this.memberService.getResume(this.memberPageEmail).subscribe(
        (data: Resume) => {
          this.memberCV = data
        }
      )

      // if members are friends areFriends becomes true
      this.friendService.checkFriends(this.loggedinMemberEmail, this.memberPageEmail).subscribe(
        data => {
          this.areFriends = data
        }
      )

      this.getPersonalData(this.memberPageEmail)

    }
    else
      this.memberExists = false
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  public sendFriendRequest(): void {
    this.friendService.sendFriendRequest(this.loggedinMemberEmail, this.memberPageEmail).subscribe(
      data => {
        this.requestSent = data
      }
    )
  }

  public getPersonalData(email: string): void {
    this.memberService.getExperience(email).subscribe(
      data => {
        this.experience = data
      }
    )
    this.memberService.getEducation(email).subscribe(
      data => {
        this.education = data
      }
    )
    this.memberService.getSkills(email).subscribe(
      data => {
        this.skills = data
      }
    )
  }

  public isObjectEmpty(obj: any): boolean { //checks whether experience, education or skills is empty
    return (typeof obj === 'undefined' || obj === null)  //so that we wont set them as public or private
  }


}
