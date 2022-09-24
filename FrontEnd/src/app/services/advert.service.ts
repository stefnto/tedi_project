import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Advert} from "../models/advert";

@Injectable({
  providedIn: 'root'
})
export class AdvertService {

  private readonly membersUrl: string

  constructor(private http: HttpClient) {
    this.membersUrl = `${environment.baseUrl}/api`;
  }

  public addAdv(ad: Advert, email: string): Observable<Advert>{
    return this.http.post<Advert>(`${this.membersUrl}/ad/addAdv/${email}`, ad, {
      responseType: "text" as "json"
    });
  }

  public getAdsFromFriends(email: string): Observable<Advert[]>{
    return this.http.get<Advert[]>(`${this.membersUrl}/ad/list/${email}`)
  }

  public getRecommendedNonFriendAds(email: string): Observable<Advert[]>{
    return this.http.get<Advert[]>(`${this.membersUrl}/ad/list/recommended/${email}`)
  }

  public getOwnAds(email: string): Observable<Advert[]>{
    return this.http.get<Advert[]>(`${this.membersUrl}/ad/own_list/${email}`)
  }

  public doApply(email: string, id: number){
    return this.http.post<string>(`${this.membersUrl}/ad/apply`, email, {
      headers: new HttpHeaders()
        .set('ad_id', id.toString()),
      responseType: "text" as "json"
    });
  }

  public doUnApply(email: string, id: number){
    return this.http.post<string>(`${this.membersUrl}/ad/unapply`, email, {
      headers: new HttpHeaders()
        .set('ad_id', id.toString()),
      responseType: "text" as "json"
    });
  }

  public getAppliedAds(array: number[], email: string){
    return this.http.get<any>(`${this.membersUrl}/ad/isApplied`, {
      headers: new HttpHeaders()
        .set('email', email)
        .set('array', array.toString())
    });
  }

  public getAppliedMembers(array: number[]){
    return this.http.get<any>(`${this.membersUrl}/ad/applied_members`, {
      headers: new HttpHeaders()
        .set('array', array.toString())
    });
  }
}
