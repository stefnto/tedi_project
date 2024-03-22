import {Component, OnInit} from '@angular/core';
import {MemberService, TokenStorageService} from "../services";
import {ActivatedRoute, Router} from "@angular/router";
import {UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators} from "@angular/forms";
import {PostService} from "../services/post.service";
import {Post} from "../models/post";
import {MemberInfo} from "../models";
import {HttpErrorResponse} from "@angular/common/http";
import {CommentService} from "../services/comment.service";
import {Comment} from "../models/comment";
import {FriendService} from "../services/friend.service";
import {Image} from "../models/image";
import {Video} from "../models/video";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  myDate!: Date
  uploadForm!: UntypedFormGroup
  commentForm!: UntypedFormGroup
  email!: any
  public member!: MemberInfo                                      // for member information used throughout the component
  public like_helper!: number                                     // index used when liking a post or a recommended post
  public comment_helper!: number                                  // index used to show/hide comment section on each post

  // variables used to display member and friends posts
  public posts!: Post[]
  public fake!: Post[]                                            // used to attach photo to post when uploading
  public post_comments!: Array<Array<Comment>>                    // changed everytime getPostComments() is called


  // isPressed and isLiked are arrays with the length of posts post_ids
  // posts[i], isPressed[i] and isLiked[i] refer to the same post but for different purposes
  public isPressed!: Boolean[]
  public isLiked!: Boolean[]

  // likes[i] holds the number of likes of the posts[i] post
  // post_ids[] holds the ids of the posts after they were shorted by Date
  public likes!: number[]
  public post_ids!: number[]

  // used to store post_images of posts
  post_images: Image[] = []
  // used to store post_videos of posts
  post_videos: Video[] = []
  // used to store post_audios of posts
  post_audios: any = []

  // variables used to display recommended posts
  public recommended_posts!: Post[]
  public recommended_post_comments!: Array<Array<Comment>>
  public recommended_isPressed!: Boolean[]
  public recommended_isLiked!: Boolean[]
  public recommended_likes!: number[]
  public recommended_post_ids!: number[]
  public recommended_post_images: Image[] = []
  public recommended_post_videos: Video[] = []
  public recommended_post_audios: any = []

  // file
  selectedFile!: File

  format!: any


  constructor(private token: TokenStorageService,
              public router: Router,
              private route: ActivatedRoute,
              private memberService: MemberService,
              private friendService: FriendService,
              public postService: PostService,
              private formBuilder: UntypedFormBuilder,
              private commentService: CommentService) {

    let formControls1 = {
      text: new UntypedFormControl('',
        [Validators.required]),
      date: new UntypedFormControl(),
      post_name: new UntypedFormControl(),
      post_surname: new UntypedFormControl()
    }

    let formControls2 = {
      commentt: new UntypedFormControl('',
        [Validators.required]),
      name: new UntypedFormControl(),
      surname: new UntypedFormControl()
    }

    this.uploadForm = this.formBuilder.group(formControls1)
    this.commentForm = this.formBuilder.group(formControls2)
  }

  ngOnInit(): void {
    if (this.token.getRole() === "ROLE_ADMIN")
      this.redirect_to_Admin_home()
    else {
      this.email = this.token.getEmail()
      this.getPosts(this.email)
      this.getRecommendedPosts(this.email)
      this.memberService.getMemberInfoByEmail(this.email).subscribe(
        (response: MemberInfo) => {
          this.member = response
        }
      )
    }
  }

  public onFileChanged(event: any) {
    //Select File
    this.selectedFile = event.target.files && event.target.files[0];

    // const file = event.target.files && event.target.files[0];
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.readAsDataURL(this.selectedFile);
      if (this.selectedFile.type.indexOf('image') > -1) {
        this.format = 'image';
      } else if (this.selectedFile.type.indexOf('video') > -1) {
        this.format = 'video';
      } else if (this.selectedFile.type.indexOf('audio') > -1) {
        this.format = 'audio';
      }
    }
  }


  public redirect_to_Admin_home() {
    this.router.navigate(['../administration-page']).then()
  }

  public redirect_to_home() {
    this.router.navigate(['../']).then()
  }


  public submitPost(): void {
    this.myDate = new Date()
    this.uploadForm.controls['post_name'].setValue(this.member.name)
    this.uploadForm.controls['post_surname'].setValue(this.member.surname)
    this.uploadForm.controls['date'].setValue(this.myDate)
    this.postService.addPost(this.uploadForm.value, this.email).subscribe(
      ()=> {
        if(this.format === 'image') {
          this.postService.getPostsFromEmail(this.email).subscribe(
            (response: Post[]) => {
              this.fake = response
              let post_id = this.fake[this.fake.length - 1].id
              //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
              const uploadImageData = new FormData();
              uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);

              //Make a call to the Spring Boot Application to save the image
              this.postService.postImage(uploadImageData, post_id).subscribe(
                () => {
                  window.location.reload()
                },
                () => {
                  window.location.reload()
                }
              )

            }
          )
        }
        else if(this.format === 'video'){
          this.postService.getPostsFromEmail(this.email).subscribe(
            (response: Post[]) => {
              this.fake = response
              let post_id = this.fake[this.fake.length - 1].id
              const uploadVideoData = new FormData();
              uploadVideoData.append('videoFile', this.selectedFile, this.selectedFile.name);

              this.postService.postVideo(uploadVideoData, post_id).subscribe(
                () => {
                  window.location.reload()
                },
                () => {
                  window.location.reload()
                }
              )
            })
        }
        else if(this.format === 'audio'){
          this.postService.getPostsFromEmail(this.email).subscribe(
            (response: Post[]) => {
              this.fake = response
              let post_id = this.fake[this.fake.length - 1].id
              const uploadAudioData = new FormData();
              uploadAudioData.append('audioFile', this.selectedFile, this.selectedFile.name);

              this.postService.postAudio(uploadAudioData, post_id).subscribe(
                () => {
                  window.location.reload()
                },
                () => {
                  window.location.reload()
                }
              )
            })
        }
        else {
          window.location.reload()
        }
      }
    )


  }

  // when we comment we press only the comment section of the specific post will appear
  public comment(post: Post): void {
    this.comment_helper = this.posts.indexOf(post)
    if (!this.isPressed[this.comment_helper]) {
      this.getPostComments(post.id)
      this.isPressed[this.comment_helper] = true
    } else
      this.isPressed[this.comment_helper] = false
  }

  public comment_recommended_posts(post: Post): void {
    this.comment_helper = this.recommended_posts.indexOf(post)
    if (!this.recommended_isPressed[this.comment_helper]) {
      this.getRecommendedPostComments(post.id)
      this.recommended_isPressed[this.comment_helper] = true
    } else
      this.recommended_isPressed[this.comment_helper] = false
  }

  public submitComment(id: number): void {
    this.commentForm.controls['name'].setValue(this.member.name)
    this.commentForm.controls['surname'].setValue(this.member.surname)
    this.commentService.addComment(this.commentForm.value, id).subscribe(
      () => {
        this.getPostComments(id)
      }
    )
  }

  public submitComment_recommended_posts(id: number): void {
    this.commentForm.controls['name'].setValue(this.member.name)
    this.commentForm.controls['surname'].setValue(this.member.surname)
    this.commentService.addComment(this.commentForm.value, id).subscribe(
      () => {
        this.getRecommendedPostComments(id)
      }
    )
  }

  // gets all the posts of logged in member and their friends
  public getPosts(email: string): void {
    this.postService.getPostsFromEmail(email).subscribe(
      (response: Post[]) => {
        let i !: number;
        this.posts = response
        this.posts = this.sortByDate(this.posts)          // sort the posts by their dates

        console.log(this.posts)
        // initialize the arrays
        this.post_comments = new Array(this.posts.length)
        this.isLiked = new Array(this.posts.length)
        this.likes = new Array(this.posts.length)
        this.post_ids = new Array(this.posts.length)


        // put ids into array
        for (i = 0; i < this.posts.length; i++)
          this.post_ids[i] = this.posts[i].id


        //gets the number of likes of the posts that the member sees
        //checks if the logged in member has liked the post or not
        this.getLikes(this.post_ids)
        this.getlikedPostsByMember(this.post_ids, email)


        // isPressed is used to determine if a comment section on a post should be shown or hidden
        this.isPressed = new Array(this.posts.length)
        for (i = 0; i < this.posts.length; i++)
          this.isPressed[i] = false


        this.postService.getImages(this.post_ids).subscribe(
          (response: Image[]) => {
            this.post_images = response
          }
        )

        this.postService.getVideos(this.post_ids).subscribe(
          (response: Video[]) => {
            this.post_videos = response
          }
        )

        this.postService.getAudios(this.post_ids).subscribe(
          (response) => {
            this.post_audios = response
          }
        )

      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public getRecommendedPosts(email: string): void {
    this.postService.getRecommendedPosts(email).subscribe(
      (response: Post[]) => {
        let i !: number
        this.recommended_posts = response
        this.recommended_posts = this.sortByDate(this.recommended_posts)

        console.log(this.recommended_posts)

        this.recommended_post_comments = new Array(this.recommended_posts.length)
        this.recommended_isLiked = new Array(this.recommended_posts.length)
        this.recommended_likes = new Array(this.recommended_posts.length)
        this.recommended_post_ids = new Array(this.recommended_posts.length)

        for (i = 0; i < this.recommended_posts.length; i++)
          this.recommended_post_ids[i] = this.recommended_posts[i].id

        this.getRecommendedPostsLikes(this.recommended_post_ids)
        this.getlikedRecommendedPostsByMember(this.recommended_post_ids, email)

        this.recommended_isPressed = new Array(this.recommended_posts.length)
        for (i = 0; i < this.recommended_posts.length; i++)
          this.recommended_isPressed[i] = false

        this.postService.getImages(this.recommended_post_ids).subscribe(
          (response: Image[]) => {
            this.recommended_post_images = response
          }
        )

        this.postService.getVideos(this.recommended_post_ids).subscribe(
          (response: Video[]) => {
            this.recommended_post_videos = response
          }
        )

        this.postService.getAudios(this.recommended_post_ids).subscribe(
          (response) => {
            this.recommended_post_audios = response
          }
        )
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public sortByDate(posts: Post[]): Post[] {
    if (posts) {
      posts.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
    }
    return posts
  }

  public getPostComments(id: number): void {
    this.commentService.getCommentsFromId(id).subscribe(
      (response: Comment[]) => {
        this.post_comments[this.comment_helper] = response
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public getRecommendedPostComments(id: number): void {
    this.commentService.getCommentsFromId(id).subscribe(
      (response: Comment[]) => {
        this.recommended_post_comments[this.comment_helper] = response
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    )
  }

  public likePost(post: Post): void {
    this.like_helper = this.posts.indexOf(post)
    if (!this.isLiked[this.like_helper]) {

      this.isLiked[this.like_helper] = true
      this.likes[this.like_helper] += 1
      this.postService.doLike(this.email, post.id).subscribe()

    }
    else if (this.isLiked[this.like_helper]) {

      this.isLiked[this.like_helper] = false
      this.likes[this.like_helper] -= 1
      this.postService.doUnLike(this.email, post.id).subscribe()

    }
  }

  public likeRecommendedPost(post: Post): void {
    this.like_helper = this.recommended_posts.indexOf(post)
    if (!this.recommended_isLiked[this.like_helper]) {

      this.recommended_isLiked[this.like_helper] = true
      this.recommended_likes[this.like_helper] += 1
      this.postService.doLike(this.email, post.id).subscribe()

    }
    else if (this.recommended_isLiked[this.like_helper]) {

      this.recommended_isLiked[this.like_helper] = false
      this.recommended_likes[this.like_helper] -= 1
      this.postService.doUnLike(this.email, post.id).subscribe()

    }
  }

  public getLikes(array: number[]): void {
    this.postService.getNumOfLikes(array).subscribe(
      (response) => {
        this.likes = response
      }
    )
  }

  public getRecommendedPostsLikes(array: number[]): void {
    this.postService.getNumOfLikes(array).subscribe(
      (response) => {
        this.recommended_likes = response
      }
    )
  }

  public getlikedPostsByMember(array: number[], email: string): void {
    this.postService.getLikedPosts(array, email).subscribe(
      (response) => {
        this.isLiked = response
      }
    )
  }

  public getlikedRecommendedPostsByMember(array: number[], email: string): void {
    this.postService.getLikedPosts(array, email).subscribe(
      (response) => {
        this.recommended_isLiked = response
      }
    )
  }


  public isObjectEmpty(obj?: any): boolean { //checks whether experience, education or skills is empty
    return (typeof obj === 'undefined' || obj === null)  //so that we wont set them as public or private
  }

  public playAudio(sound: any): void{
    let audio = new Audio()
    audio.src = 'data:audio/mp3;base64,' + sound.content
    audio.load()
    audio.play()
  }

  public logOut(): void {
    this.token.signOut()
    window.location.reload()
  }
}
