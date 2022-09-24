import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../../environments/environment";
import { Post } from "../models/post";


@Injectable({
  providedIn: 'root'
})
export class PostService {

  private readonly membersUrl: string

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;
  }

  public addPost(post: Post, email: string): Observable<Post>{
    return this.http.post<Post>(`${this.membersUrl}/post/addtomember/${email}`, post, {
      responseType: "text" as "json"
    });
  }

  public getPostsFromEmail(email: string): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.membersUrl}/post/list/all/${email}`);
  }

  public getRecommendedPosts(email: string): Observable<Post[]>{
    return this.http.get<Post[]>(`${this.membersUrl}/post/list/recommended/${email}`)
  }

  public doLike(email: string, id: number): Observable<any>{
    return this.http.post<string>(`${this.membersUrl}/post/like`, email, {
      headers: new HttpHeaders()
        .set('post_id', id.toString()),
      responseType: "text" as "json"
    });
  }

  public doUnLike(email: string, id: number): Observable<any>{
    return this.http.post<string>(`${this.membersUrl}/post/unlike`, email, {
      headers: new HttpHeaders()
        .set('post_id', id.toString()),
      responseType: "text" as "json"
    });
  }

  public getLikedPosts(array: number[], email: string): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/post/isLiked`, {
      headers: new HttpHeaders()
        .set('email', email)
        .set('array', array.toString())
    });
  }

  public getNumOfLikes(array: number[]): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/post/likes_number`, {
      headers: new HttpHeaders()
        .set('array', array.toString())
    });
  }

  public postImage(uploadImageData: any, post_id: number): Observable<any>{
    return this.http.post<any>(`${this.membersUrl}/post/upload_image`, uploadImageData, {
      headers: new HttpHeaders()
        .set('post_id', post_id.toString())
    })
  }

  public getImages(array: number[]): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/post/get_images`, {
      headers: new HttpHeaders()
        .set('array', array.toString())
    });
  }

  public postVideo(uploadVideoData: any, post_id: number){
    return this.http.post(`${this.membersUrl}/post/upload_video`, uploadVideoData, {
      headers: new HttpHeaders()
        .set('post_id', post_id.toString())
    })
  }

  public getVideos(array: number[]): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/post/get_videos`, {
      headers: new HttpHeaders()
        .set('array', array.toString())
    });
  }

  public postAudio(uploadAudioData: any, post_id: number){
    return this.http.post(`${this.membersUrl}/post/upload_audio`, uploadAudioData, {
      headers: new HttpHeaders()
        .set('post_id', post_id.toString())
    })
  }

  public getAudios(array: number[]): Observable<any>{
    return this.http.get<any>(`${this.membersUrl}/post/get_audios`, {
      headers: new HttpHeaders()
        .set('array', array.toString())
    });
  }
}
