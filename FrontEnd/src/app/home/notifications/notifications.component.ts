import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { MemberService, TokenStorageService } from "../../services";
import { FriendService } from "../../services/friend.service";
import { NotificationService } from "../../services/notification.service";
import { NotificationDTO } from "../../models/notificationDTO";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {

  requests: any
  requestAccepted: any
  requestDeclined: any
  public req!: number

  notifications: any
  notificationDismissed: any
  public notif!: number



  constructor(public router: Router,
              private memberService: MemberService,
              private friendService: FriendService,
              private tokenStorage: TokenStorageService,
              private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.friendRequests()
    this.getNotifications()
  }

  public friendRequests(): void {
    this.friendService.getFriendRequests(this.tokenStorage.getEmail()).subscribe(
      response => {
        this.requests = response
      }
    )
  }

  public acceptRequest(sender: string): void {
    this.req = this.requests.indexOf(sender)
    this.friendService.acceptFriendRequest(sender, this.tokenStorage.getEmail()).subscribe(
      response => {
        this.requestAccepted = response
      }
    )
  }

  public declineRequest(sender: string): void {
    this.req = this.requests.indexOf(sender)
    this.friendService.declineFriendRequest(sender, this.tokenStorage.getEmail()).subscribe(
      response => {
        this.requestDeclined = response
      }
    )
  }

  public showProfile(email: string): void {
    this.router.navigate(['/home/network', email]).then()
  }

  public getNotifications(): void {
    this.notificationService.getNotifications(this.tokenStorage.getEmail()).subscribe(
      response => {
        this.notifications = response
      }
    )
  }

  public dismissNotification(notification: NotificationDTO): void {
    this.notif = this.notifications.indexOf(notification)
    this.notificationService.seeNotification(notification.notification_id).subscribe(
      response => {
        this.notificationDismissed = response
      }
    )
  }
}
