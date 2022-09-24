import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from "@angular/forms";
import { FormsModule } from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { MemberService} from "./services";
import { AuthInterceptor } from './helpers/auth-interceptor'

import { AppComponent } from './app.component';
import { MemberFormComponent } from './member-form/member-form.component';
import { MemberListComponent } from './admin/member-list/member-list.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { LogInComponent } from './log-in/log-in.component';
import { AdminComponent } from "./admin/admin.component";
import { HomeComponent } from './home/home.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { MemberProfileComponent } from './admin/member-profile/member-profile.component';
import { NetworkComponent } from './home/network/network.component';
import { MemberPageComponent } from './home/network/member-page/member-page.component';
import { NotificationsComponent } from './home/notifications/notifications.component';
import { ChatroomsComponent } from './home/chatrooms/chatrooms.component';
import { SettingsComponent } from './home/settings/settings.component';
import { AdvertComponent } from "./home/adverts/advert.component";
import { MyAdsComponent } from "./home/adverts/my-ads/my-ads.component";
import { PersonalDataComponent } from './home/personal-data/personal-data.component';


@NgModule({
  declarations: [
    AppComponent,
    MemberFormComponent,
    MemberListComponent,
    WelcomeComponent,
    LogInComponent,
    AdminComponent,
    HomeComponent,
    PageNotFoundComponent,
    MemberProfileComponent,
    NetworkComponent,
    MemberPageComponent,
    NotificationsComponent,
    ChatroomsComponent,
    SettingsComponent,
    AdvertComponent,
    MyAdsComponent,
    PersonalDataComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [MemberService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
