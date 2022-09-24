import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MemberInfo } from '../models';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class FriendService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api/friends`
  }

  // takes sender notification as body and acceptor notification as parameter
  public sendFriendRequest(sender: string, acceptor: string): Observable<any> {
    return this.http.post<any>(`${this.membersUrl}/request/${acceptor}`, sender, {
      responseType: "text" as "json"}
      )
  }

  // takes sender notification as header and acceptor notification as body
  public acceptFriendRequest(sender: string, acceptor: string): Observable<any> {
    return this.http.post<any>(`${this.membersUrl}/request/accept`, acceptor, {
      headers: new HttpHeaders()
        .set('sender', sender)
        .set('Content-Type', 'application/json'),
      responseType: "text" as "json"
    })
  }
  // takes sender notification as header and acceptor notification as body
  public declineFriendRequest(sender: string, acceptor: string): Observable<any> {
    return this.http.post<any>(`${this.membersUrl}/request/decline`, acceptor, {
      headers: new HttpHeaders()
        .set('sender', sender)
        .set('Content-Type', 'application/json'),
      responseType: "text" as "json"
    })
  }

  public checkFriends(email1: string, email2: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.membersUrl}/request/exists`, {
      headers: new HttpHeaders()
        .set('email1', email1)
        .set('email2', email2)
    })
  }

  // takes notification as parameter
  public getFriends(email: string): Observable<MemberInfo[]> {
    return this.http.get<any>(`${this.membersUrl}/get/${email}`)
  }

  public getFriendRequests(email: string): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/get/requests/${email}`)
  }
}
