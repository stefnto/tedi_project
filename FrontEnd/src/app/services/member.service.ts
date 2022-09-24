import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Member, MemberInfo} from '../models';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";
import {Experience} from "../models/experience";
import {Education} from "../models/education";
import {Skills} from "../models/skills";



@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;

  }

  public findAllMembers(): Observable<Member[]> {
    return this.http.get<Member[]>(`${this.membersUrl}/members`);
  }

  public checkIfEmailExists(memberEmail: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.membersUrl}/members/get-email/${memberEmail}`);
  }

  public getMemberInfoByEmail(memberEmail: string): Observable<MemberInfo> {
    return this.http.get<MemberInfo>(`${this.membersUrl}/members/getInfo/${memberEmail}`);
  }

  public registerMember(member: Member): Observable<Member> {
    return this.http.post<Member>(`${this.membersUrl}/register`, member);
  }

  public updateCV(text: string, email: string): Observable<string> {
    return this.http.post<string>(`${this.membersUrl}/members/resume/update`, text, {
      headers: new HttpHeaders()
        .set('email', email),
      responseType: "text" as "json"
    })
  }

  public updateExperience(email: string, text: string): Observable<any> {
    return this.http.post<Experience>(`${this.membersUrl}/member/setExperience`, text, {
      headers: new HttpHeaders()
        .set('email', email),
      responseType: "text" as "json"
    });
  }

  public updateEducation(email: string, text: string): Observable<any> {
    return this.http.post<Education>(`${this.membersUrl}/member/setEducation`, text, {
      headers: new HttpHeaders()
        .set('email', email),
      responseType: "text" as "json"
    });
  }

  public updateSkills(email: string, text: string): Observable<any> {
    return this.http.post<Skills>(`${this.membersUrl}/member/setSkills`, text, {
      headers: new HttpHeaders()
        .set('email', email),
      responseType: "text" as "json"
    });
  }

  public updateEmail(old_email: string, new_email: string): Observable<string> {
    return this.http.post<string>(`${this.membersUrl}/members/change/email`, new_email, {
      headers: new HttpHeaders()
        .set('old_email', old_email),
      responseType: "text" as "json"
    })
  }

  public updatePassword(email: string, password: string): Observable<any> {
    return this.http.post<string>(`${this.membersUrl}/members/change/password`, password, {
      headers: new HttpHeaders()
        .set('email', email),
      responseType: "text" as "json"
    })
  }

  public publicResume(email: string): Observable<any>{
    return this.http.put<any>(`${this.membersUrl}/member/resume_public`, email, {
      responseType: "text" as "json"
    })
  }

  public privateResume(email: string): Observable<any>{
    return this.http.put<any>(`${this.membersUrl}/member/resume_private`, email, {
      responseType: "text" as "json"
    })
  }

  public privateEduc(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/education_private`, email, {
      responseType: "text" as "json"
    })
  }

  public publicEduc(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/education_public`, email, {
      responseType: "text" as "json"
    })
  }

  public publicExp(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/experience_public`, email, {
      responseType: "text" as "json"
    })
  }

  public privateExp(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/experience_private`, email, {
      responseType: "text" as "json"
    })
  }

  public privateSkills(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/skills_private`, email, {
      responseType: "text" as "json"
    })
  }

  public publicSkills(email: string): Observable<any> {
    return this.http.put<any>(`${this.membersUrl}/member/skills_public`, email, {
      responseType: "text" as "json"
    })
  }

  public getResume(email: string): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/member/resume/${email}`)
  }

  public getExperience(email: string): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/member/experience/${email}`)
  }

  public getEducation(email: string): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/member/education/${email}`)
  }

  public getSkills(email: string): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/member/skills/${email}`)
  }

}
export interface Credentials{
  password: string;
  email:    string;
}
