import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class AdminService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;
  }

  // return the information of the specified members as blob
  public getMembersInformationJSON(emails: string[]): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/members/get/list`, {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('emails', emails),
      responseType: "blob" as "json"

    })
  }

  // return the information of the specified members as json
  // which later will convert to xml
  public getMembersInformationXML(emails: string[]): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/members/get/list`, {
      headers: new HttpHeaders()
        .set('Content-Type', 'application/json')
        .set('emails', emails),

    })
  }

}
