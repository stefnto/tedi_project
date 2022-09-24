import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;

  }

  public sendMessage(text: string, email: string, interlocutor_email: string): Observable<any> {

    return this.http.post<any>(`${this.membersUrl}/chatroom/sendMessage`, text, {
      headers: new HttpHeaders()
        .set('email', email)
        .set('interlocutor', interlocutor_email.toString()),
      responseType: "text" as "json"
    })
  }

  // email1 is the member's that messages will be returned
  // email2 is the member's notification that is the other participant in the chat
  public getMessagesOfUser(email: string, interlocutor_email: string): Observable<any> {
    return this.http.get<any>(`${this.membersUrl}/chatroom/getMessages/${email}`, {
      headers: new HttpHeaders()
        .set('interlocutor', interlocutor_email)
    })
  }
}
