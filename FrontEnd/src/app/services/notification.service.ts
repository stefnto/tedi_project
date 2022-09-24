import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;
  }


  public getNotifications(email: string): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/notifications/get/${email}`)
  }

  public seeNotification(id: number): Observable<any> {
    return this.http.post<any>(`${this.membersUrl}/notifications/seen`, id, {
      responseType: "text" as "json"
    })
  }
}
