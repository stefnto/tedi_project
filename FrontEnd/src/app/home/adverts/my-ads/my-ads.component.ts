import { Component, OnInit } from '@angular/core';
import {AdvertService} from "../../../services/advert.service";
import {TokenStorageService} from "../../../services";
import {Advert} from "../../../models/advert";
import {HttpErrorResponse} from "@angular/common/http";
import {Member} from "../../../models";

@Component({
  selector: 'app-my-ads',
  templateUrl: './my-ads.component.html',
  styleUrls: ['./my-ads.component.css'],
  standalone: false
})
export class MyAdsComponent implements OnInit {

  email!: any
  public ads!: Advert[]  // holds the friendAds that will be displayed in the advertisements
  ad_ids!: number[]
  members!: Member[][]
  public isPressed!: Boolean[]

  constructor(private adService: AdvertService,
              private token: TokenStorageService) { }

  ngOnInit(): void {
    this.email = this.token.getEmail()
    this.ads = this.getAds(this.email)
  }

  public getAds(email: string): Advert[] {
    this.adService.getOwnAds(email).subscribe(
      (response: Advert[]) => {
        let i !: number
        this.ads = response

        this.ad_ids = new Array(this.ads.length)

        for (i=0; i<this.ads.length; i++)
          this.ad_ids[i] = this.ads[i].id

        this.getAppliedMembers(this.ad_ids)

        this.isPressed = new Array(this.ads.length)
        for (i=0; i<this.ads.length; i++)
          this.isPressed[i] = false
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
    return this.ads
  }

  public getAppliedMembers(array: number[]): void{
    this.adService.getAppliedMembers(array).subscribe(
      (response) => {
        this.members = response
      }
    )
  }

  public switch(ad: Advert): void{
    let p = this.ads.indexOf(ad)
    if(!this.isPressed[p])
      this.isPressed[p] = true
    else
      this.isPressed[p] = false
  }


}
