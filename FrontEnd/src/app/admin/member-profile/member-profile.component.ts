import {Component, OnDestroy, OnInit} from '@angular/core';
import { MemberProfileService } from "../../services/member-profile.service";
import { ActivatedRoute } from "@angular/router";
import {Resume} from "../../models/resume";
import {Member, MemberInfo} from "../../models";
import {Education} from "../../models/education";
import {Experience} from "../../models/experience";
import {Skills} from "../../models/skills";
import {MemberService, TokenStorageService} from "../../services";
import {FriendService} from "../../services/friend.service";

@Component({
  selector: 'app-member-profile',
  templateUrl: './member-profile.component.html',
  styleUrls: ['./member-profile.component.css'],
  standalone: false
})
export class MemberProfileComponent implements OnInit, OnDestroy {

  private sub: any

  public loggedinMemberEmail: any

  // information about the member that it's profile is being displayed
  public member_page_email: any
  public member_exists!: boolean
  public memberCV!: Resume
  public areFriends: any
  public searchedMember!: MemberInfo

  public education!: Education
  public experience!: Experience
  public skills!: Skills

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
      this.member_page_email = params['notification']
      this.member_page_email = JSON.stringify(this.member_page_email).replace("[", "")
        .replace("'", "")
        .replace("'", "")
        .replace("'", "")
        .replace('"', "")
        .replace(',', "")
        .replace('"', "")
        .replace("]", "")
        .replace(" ", "")
    })


      this.memberProfileService.saveEmail(this.member_page_email)

      this.memberService.getMemberInfoByEmail(this.member_page_email).subscribe(
        (response: MemberInfo) => {
          this.searchedMember = response
        }
      )
      // get CV from backend and put it to session storage
      this.memberService.getResume(this.member_page_email).subscribe(
        (data: Resume) => {
          this.memberCV = data
        }
      )

      // if members are friends areFriends becomes true
      this.friendService.checkFriends(this.loggedinMemberEmail, this.member_page_email).subscribe(
        data => {
          this.areFriends = data
        }
      )

      this.getPersonalData(this.member_page_email)

  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
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
