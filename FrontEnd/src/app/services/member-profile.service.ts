import { Injectable } from '@angular/core';
import { environment } from "../../environments/environment";
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";


const MEMBER_KEY = 'member_email'
const MEMBER_CV = 'member_cv'

@Injectable({
  providedIn: 'root'
})
export class MemberProfileService {
  private readonly membersUrl: string

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`
  }

  // session storage used to temporally save member's information to display profile

  public saveEmail(email: string): void {
    window.sessionStorage.removeItem(MEMBER_KEY)
    window.sessionStorage.setItem(MEMBER_KEY, email)
  }

  public saveCV(cv: string): void {
    window.sessionStorage.removeItem(MEMBER_CV)
    window.sessionStorage.setItem(MEMBER_CV, cv)
  }

  public getEmail(): string | null {
    return window.sessionStorage.getItem(MEMBER_KEY)
  }

  public getCV(): string | null {
    return window.sessionStorage.getItem(MEMBER_CV)
  }

  // requests to backend


  public findSpecificCV(memberEmail: string): Observable<any> {
    return this.http.get(`${this.membersUrl}/members/resume/${memberEmail}`, {responseType: "text"})
  }


}
