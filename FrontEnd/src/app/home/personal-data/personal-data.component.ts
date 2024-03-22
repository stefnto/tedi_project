import { Component, OnInit } from '@angular/core';
import { MemberService, TokenStorageService } from "../../services";
import { ActivatedRoute, Router } from "@angular/router";
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { Education } from "../../models/education";
import { Member } from "../../models";
import { Experience } from "../../models/experience";
import { Skills } from "../../models/skills";
import { Resume } from "../../models/resume";

@Component({
  selector: 'app-personal-data',
  templateUrl: './personal-data.component.html',
  styleUrls: ['./personal-data.component.css']
})
export class PersonalDataComponent implements OnInit {

  email!: any
  member!: Member


  educationForm!: UntypedFormGroup
  experienceForm!: UntypedFormGroup
  skillsForm!: UntypedFormGroup
  resumeForm!: UntypedFormGroup

  //variables that show the appropriate messages
  isUpdated1: boolean = false
  isUpdated2: boolean = false
  isUpdated3: boolean = false
  isUpdated4: boolean = false

  isPublic1: boolean = false
  isPublic2: boolean = false
  isPublic3: boolean = false
  isPublic4: boolean = false

  isPrivate1: boolean = false
  isPrivate2: boolean = false
  isPrivate3: boolean = false
  isPrivate4: boolean = false

  isEmpty1: boolean = false
  isEmpty2: boolean = false
  isEmpty3: boolean = false
  isEmpty4: boolean = false

  //variables that show if these entities are null
  experience!: Experience
  education!: Education
  skills!: Skills
  resume!: Resume

  editExp: boolean = false
  editEduc: boolean = false
  editSkill: boolean = false
  editResum: boolean = false



  constructor(private token: TokenStorageService,
              private memberService: MemberService,
              public router: Router,
              private route: ActivatedRoute,
              private formBuilder: UntypedFormBuilder) {

    let formControls1 = {
      experience: new UntypedFormControl('',
        [Validators.required]),
    }

    let formControls2 = {
      education: new UntypedFormControl('',
        [Validators.required])
    }

    let formControls3 = {
      skills: new UntypedFormControl('',
        [Validators.required])
    }

    let formControls4 = {
      resume: new UntypedFormControl('',
        [Validators.required])
    }

    this.experienceForm = this.formBuilder.group(formControls1)
    this.educationForm = this.formBuilder.group(formControls2)
    this.skillsForm = this.formBuilder.group(formControls3)
    this.resumeForm = this.formBuilder.group(formControls4)
  }

  ngOnInit(): void {
    this.email = this.token.getEmail()

    //get his experience
    this.memberService.getExperience(this.email).subscribe(
      (response: Experience) => {
        this.experience = response
      }
    )
    //get his education
    this.memberService.getEducation(this.email).subscribe(
      (response: Education) => {
        this.education = response
      }
    )
    //get his skills
    this.memberService.getSkills(this.email).subscribe(
      (response: Skills) => {
        this.skills = response
      }
    )
    //get his cv
    this.memberService.getResume(this.email).subscribe(
      (response: Resume) => {
        this.resume = response
      }
    )
  }

  public editExperience(): void {
    this.editExp = !this.editExp;
  }
  public editEducation(): void {
    this.editEduc = !this.editEduc;
  }
  public editSkills(): void {
    this.editSkill = !this.editSkill;
  }
  public editResume(): void {
    this.editResum = !this.editResum;
  }

  public onSubmitExperience(): void {
    this.memberService.updateExperience(this.email, this.experienceForm.controls['experience'].value).subscribe(
      () => {
        this.isUpdated1 = true
        this.memberService.getExperience(this.email).subscribe(
          (response: Experience) => {
            this.experience = response
          }
        )
      }
    )
  }

  public onSubmitEducation(): void {
    this.memberService.updateEducation(this.email, this.educationForm.controls['education'].value).subscribe(
      () => {
        this.isUpdated2 = true
        this.memberService.getEducation(this.email).subscribe(
          (response: Education) => {
            this.education = response
          }
        )
      }
    )
  }

  public onSubmitSkills(): void {
    this.memberService.updateSkills(this.email, this.skillsForm.controls['skills'].value).subscribe(
      () => {
        this.isUpdated3 = true
        this.memberService.getSkills(this.email).subscribe(
          (response: Skills) => {
            this.skills = response
          }
        )
      }
    )
  }

  onSubmitResume(): void {
    this.memberService.updateCV(this.resumeForm.controls['resume'].value, this.email).subscribe(
      () => {
        this.isUpdated4 = true
        this.memberService.getResume(this.email).subscribe(
          (response: Resume) => {
            this.resume = response
          }
        )
      }
    )
  }

  public makePublicExperience(): void {
    this.isEmpty1 = this.isObjectEmpty(this.experience)//check if experience is empty or not when pressing the button
    if (!this.isEmpty1) {
      this.isUpdated1 = false  //don't appear the message of update in experience field
      this.isPublic1 = true //appear the message of public information in experience field
      this.isPrivate1 = false //don't appear the message of private information in experience field
      this.isEmpty1 = false  //dont appear the warning message after the successful message
    }
    this.memberService.publicExp(this.email).subscribe()
  }

  public makePrivateExperience(): void {
    this.isEmpty1 = this.isObjectEmpty(this.experience)
    if (!this.isEmpty1) {
      this.isUpdated1 = false
      this.isPublic1 = false
      this.isPrivate1 = true
      this.isEmpty1 = false
    }
    this.memberService.privateExp(this.email).subscribe()
  }

  public makePublicEducation(): void {
    this.isEmpty2 = this.isObjectEmpty(this.education)
    if (!this.isEmpty2) {
      this.isUpdated2 = false
      this.isPublic2 = true
      this.isPrivate2 = false
      this.isEmpty2 = false
    }
    this.memberService.publicEduc(this.email).subscribe()
  }

  public makePrivateEducation(): void {
    this.isEmpty2 = this.isObjectEmpty(this.education)
    if (!this.isEmpty2) {
      this.isUpdated2 = false
      this.isPrivate2 = true
      this.isPublic2 = false
      this.isEmpty2 = false
    }
    this.memberService.privateEduc(this.email).subscribe()
  }

  public makePublicSkills(): void {
    this.isEmpty3 = this.isObjectEmpty(this.skills)
    if (!this.isEmpty3) {
      this.isUpdated3 = false
      this.isPrivate3 = false
      this.isPublic3 = true
      this.isEmpty3 = false
    }
    this.memberService.publicSkills(this.email).subscribe()
  }

  public makePrivateSkills(): void {
    this.isEmpty3 = this.isObjectEmpty(this.skills)
    if (!this.isEmpty3) {
      this.isUpdated3 = false
      this.isPrivate3 = true
      this.isPublic3 = false
      this.isEmpty3 = false
    }
    this.memberService.privateSkills(this.email).subscribe()
  }

  public makePublicResume(): void {
    this.isEmpty4 = this.isObjectEmpty(this.resume)
    if (!this.isEmpty4) {
      this.isUpdated4 = false
      this.isPublic4 = true
      this.isPrivate4 = false
      this.isEmpty4 = false
    }
    this.memberService.publicResume(this.email).subscribe()
  }

  public makePrivateResume(): void {
    this.isEmpty4 = this.isObjectEmpty(this.resume)
    if (!this.isEmpty4) {
      this.isUpdated4 = false
      this.isPublic4 = false
      this.isPrivate4 = true
      this.isEmpty4 = false
    }
    this.memberService.privateResume(this.email).subscribe()
  }

  public isObjectEmpty(obj: any): boolean { //checks whether experience, education or skills is empty
    return (typeof obj === 'undefined' || obj === null)  //so that we wont set them as public or private
  }

}
