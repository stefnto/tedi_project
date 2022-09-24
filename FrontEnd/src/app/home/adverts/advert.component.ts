import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AdvertService} from "../../services/advert.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MemberService, TokenStorageService} from "../../services";
import {Advert} from "../../models/advert";
import {MemberInfo} from "../../models";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-adverts',
  templateUrl: './advert.component.html',
  styleUrls: ['./advert.component.css']
})
export class AdvertComponent implements OnInit {
  myDate!: Date
  email!: any
  adForm!: FormGroup
  isPressed: boolean = false

  friendAds_ids!: number[]
  recommendedAds_ids!: number[]

  public isAppliedFriendsAds!: Boolean[]
  public isAppliedRecommendedAds!: Boolean[]

  public friendAds: Advert[] = []                 // holds the friendAds that will be displayed in the advertisements
  public recommendedAds: Advert[] = []            // holds the recommended ads based on member's skills
  public member!: MemberInfo                      // for member information used throughout the component

  constructor(public router: Router,
              private adService: AdvertService,
              private memberService: MemberService,
              private formBuilder: FormBuilder,
              private token: TokenStorageService) {

    let formControls = {
      text: new FormControl('',
        [Validators.required]),
      date: new FormControl(),
      name: new FormControl(),
      surname: new FormControl(),
      prerequisite_skills: new FormControl('',
        [Validators.required])
    }

    this.adForm = this.formBuilder.group(formControls)
  }

  ngOnInit(): void {
    this.email = this.token.getEmail()
    this.getFriendAds(this.email)
    this.getRecommendedAds(this.email)
    this.memberService.getMemberInfoByEmail(this.email).subscribe(
      (response: MemberInfo) => {
        this.member = response
        console.log(this.member)
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public show() {
    this.isPressed = !this.isPressed;
  }

  public submitAd(): void {
    this.myDate = new Date()
    this.adForm.controls['name'].setValue(this.member.name)
    this.adForm.controls['surname'].setValue(this.member.surname)
    this.adForm.controls['date'].setValue(this.myDate)
    this.adService.addAdv(this.adForm.value, this.email).subscribe(
      () => {
        // navigate to my-friendAds page
        this.router.navigate(['home/adverts/my-ads']).then()
      }
    )
  }

  // get all the ads of friends of logged in member
  public getFriendAds(email: string): void {
    this.adService.getAdsFromFriends(email).subscribe(
      (response: Advert[]) => {
        let i !: number
        this.friendAds = response
        // initialize the arrays
        this.isAppliedFriendsAds = new Array(this.friendAds.length)
        this.friendAds_ids = new Array(this.friendAds.length)

        for (i=0; i<this.friendAds.length; i++)
          this.friendAds_ids[i] = this.friendAds[i].id

        //checks if the logged in member has applied for the job or not
        this.getAppliedAds(this.friendAds_ids, email)
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  // get recommended ads of member based on skills
  public getRecommendedAds(email: string): void {
    this.adService.getRecommendedNonFriendAds(email).subscribe(
      (response: Advert[]) => {
        let i !: number
        this.recommendedAds = response

        // initialize the arrays
        this.isAppliedRecommendedAds = new Array(this.recommendedAds.length)
        this.recommendedAds_ids = new Array(this.recommendedAds.length)

        for (i=0; i<this.recommendedAds.length; i++)
          this.recommendedAds_ids[i] = this.recommendedAds[i].id

        //checks if the logged in member has applied for the job or not
        this.getAppliedRecommendedAds(this.recommendedAds_ids, email)
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public applyFriendAd(ad: Advert): void{
    let p = this.friendAds.indexOf(ad)
    if (!this.isAppliedFriendsAds[p]){
      this.isAppliedFriendsAds[p] = true
      this.adService.doApply(this.email, ad.id).subscribe()
    }
    else if (this.isAppliedFriendsAds[p]) {
      this.isAppliedFriendsAds[p] = false
      this.adService.doUnApply(this.email, ad.id).subscribe()
    }
  }

  public applyRecommendedAd(ad: Advert): void {
    let p = this.recommendedAds.indexOf(ad)
    if (!this.isAppliedRecommendedAds[p]){
      this.isAppliedRecommendedAds[p] = true
      this.adService.doApply(this.email, ad.id).subscribe()
    }
    else if (this.isAppliedRecommendedAds[p]) {
      this.isAppliedRecommendedAds[p] = false
      this.adService.doUnApply(this.email, ad.id).subscribe()
    }
  }

  public getAppliedAds(array: number[], email: string): void{
    this.adService.getAppliedAds(array, email).subscribe(
      (response) => {
        this.isAppliedFriendsAds = response
      }
    )
  }

  public getAppliedRecommendedAds(array: number[], email: string): void{
    this.adService.getAppliedAds(array, email).subscribe(
      (response) => {
        this.isAppliedRecommendedAds = response
      }
    )
  }
}
