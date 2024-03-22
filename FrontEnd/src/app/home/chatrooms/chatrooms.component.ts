import { Component,OnInit } from '@angular/core';
import { ChatService } from "../../services/chat.service";
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { FriendService } from "../../services/friend.service";
import { MemberInfo } from "../../models";
import { HttpErrorResponse } from "@angular/common/http";
import { TokenStorageService } from "../../services";
import { MessageDTO } from "../../models/messageDTO";
import { Router } from "@angular/router";

@Component({
  selector: 'app-chatrooms',
  templateUrl: './chatrooms.component.html',
  styleUrls: ['./chatrooms.component.css']
})
export class ChatroomsComponent implements OnInit{

  public member_email!: string
  public memberInfo!: MemberInfo[]
  public selfMessages!: MessageDTO[]
  public interlocutorMessages!: MessageDTO[]
  public chatRoomMessages!: MessageDTO[]
  public interlocutorEmail!: string
  public interlocutorName!: string
  public messageForm!: UntypedFormGroup



  constructor(public router: Router,
              private chatService: ChatService,
              private friendService: FriendService,
              private formBuilder: UntypedFormBuilder,
              private tokenStorage: TokenStorageService){

    let formControl = {
      text: new UntypedFormControl('',[Validators.required])
    }

    this.messageForm = this.formBuilder.group(formControl)
  }

  ngOnInit() {

    // get member_email stored in local storage
    this.member_email = this.tokenStorage.getEmail()

    // get list of friends
    this.friendService.getFriends(this.member_email).subscribe(
      (response: MemberInfo[]) => {
        this.memberInfo = response
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }
    )

  }

  public sendMessage(email: string, interlocutor_email: string){
    this.chatService.sendMessage(this.messageForm.get('text')!.value, email, interlocutor_email).subscribe(
      () => {
        this.getChatMessages(email, interlocutor_email, this.interlocutorName)
      }
    )
  }

  public getChatMessages(email: string, interlocutor_email: string, name: string){

    this.interlocutorEmail = interlocutor_email
    this.interlocutorName = name
    // get logged in member messages of the chat
    this.chatService.getMessagesOfUser(email, interlocutor_email).subscribe(
      (response: MessageDTO[]) => {
        this.selfMessages = response

        // and then get interlocutor messages of the chat
        this.chatService.getMessagesOfUser(interlocutor_email, email).subscribe(
          (response: MessageDTO[]) => {
            this.interlocutorMessages = response

            // merge the two arrays and sort them by date
            this.chatRoomMessages = this.selfMessages.concat(this.interlocutorMessages)
            this.chatRoomMessages = this.sortByDate(this.chatRoomMessages)
          }
        )
      }
    )


  }

  public sortByDate(chatMessages: MessageDTO[]): MessageDTO[] {
    chatMessages.sort((a, b) => new Date(b.dateSent).getTime() - new Date(a.dateSent).getTime());
    return chatMessages
  }

}
