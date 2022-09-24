import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment";
import { Comment } from "../models/comment";


@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private readonly membersUrl: string;

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;
  }

  public addComment(comment: Comment, id: number){
    return this.http.post<Comment>(`${this.membersUrl}/post/comments/${id}`, comment, {
      responseType: "text" as "json"
    })
  }

   public getCommentsFromId(id: number){
     return this.http.get<Comment[]>(`${this.membersUrl}/post/comments/list/${id}`);
   }
}
