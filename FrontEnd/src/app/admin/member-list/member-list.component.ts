import { Component, OnInit } from '@angular/core';
import { Member } from '../../models';
import { AdminService, MemberService } from '../../services';
import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { saveAs } from  "file-saver";
import * as JsonToXML from "js2xmlparser";


@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styleUrls: ['./member-list.component.css']
})
export class MemberListComponent implements OnInit {
  public members!: Member[];
  private emails: Array<string> = []


  constructor(private memberService: MemberService,
              private adminService: AdminService,
              public router: Router) {
  }

  ngOnInit(): void {
    this.getMembers();
  }

  public getMembers(): void {
    this.memberService.findAllMembers().subscribe(
      (response:Member[]) => {
        this.members = response
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }

   public exportJSON(){
    // empty post_ids before every new export
    this.emails = []
    this.members.forEach(member =>{
      // for all the checked buttons that their id attribute ends with "_json"
      let checkButton = document.getElementById(member.email+"_json")

      // if button was checked get the 'name' attribute of the html tag
      // which consists of the notification and push it into the post_ids
      if ((checkButton as HTMLInputElement).checked){
        let temp = checkButton!.outerHTML.substring(51)
        temp = temp.substring(0,temp.indexOf('_'))
        this.emails.push(temp)
      }
    })
     this.adminService.getMembersInformationJSON(this.emails).subscribe(
       data => {
         saveAs(data, "data.json")
       }
     )
  }

  public exportXML(){
    // empty post_ids before every new export
    this.emails = []

    this.members.forEach(member =>{
      // for all the buttons that their id attribute ends with "_xml"
      let checkButton = document.getElementById(member.email+"_xml")

      // if button was checked get the 'name' attribute of the html tag
      // which consists of the notification and push it into the post_ids
      if ((checkButton as HTMLInputElement).checked){
        let temp = checkButton!.outerHTML.substring(51)
        temp = temp.substring(0,temp.indexOf('_'))
        this.emails.push(temp)
      }
    })
    this.adminService.getMembersInformationXML(this.emails).subscribe(
      data => {
        // data is JSON type, thus convert it to xml with parse() method
        const blob = new Blob([JsonToXML.parse("data", data)])
        saveAs(blob, "data.xml")
      }
    )
  }
}
