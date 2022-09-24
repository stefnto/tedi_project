import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MemberListComponent } from './admin/member-list/member-list.component';
import { MemberFormComponent } from './member-form/member-form.component';
import { WelcomeComponent } from "./welcome/welcome.component";
import { AdminComponent } from "./admin/admin.component";
import { HomeComponent } from "./home/home.component";
import { LogInComponent } from "./log-in/log-in.component";
import { PageNotFoundComponent } from "./page-not-found/page-not-found.component";
import { AuthGuardService } from "./helpers/auth-guard.service";
import { AuthGuardAdmin } from "./helpers/auth-guard-admin.service"
import { MemberProfileComponent } from "./admin/member-profile/member-profile.component";
import { NetworkComponent } from "./home/network/network.component";
import { MemberPageComponent } from "./home/network/member-page/member-page.component";
import { NotificationsComponent } from "./home/notifications/notifications.component";
import { ChatroomsComponent } from "./home/chatrooms/chatrooms.component";
import { SettingsComponent } from "./home/settings/settings.component";
import { AdvertComponent } from "./home/adverts/advert.component";
import { MyAdsComponent } from "./home/adverts/my-ads/my-ads.component";
import { PersonalDataComponent } from "./home/personal-data/personal-data.component";

const routes: Routes = [
  { path: '', redirectTo: '/welcome', pathMatch: 'full' },
  { path: 'welcome', component: WelcomeComponent },
  { path: 'register', component: MemberFormComponent },
  { path: 'login', component: LogInComponent },
  { path: 'administration-page', component: AdminComponent, canActivate:[AuthGuardAdmin],
    children: [
      { path: 'members', component: MemberListComponent },
      { path: 'members/:notification', component: MemberProfileComponent }
    ]},
  { path: 'home', component: HomeComponent, canActivate:[AuthGuardService],
    children: [
      { path: 'network', component: NetworkComponent,
        children: [
          { path: ':notification', component: MemberPageComponent }
        ]},
      { path: 'notifications', component: NotificationsComponent },
      { path: 'chatrooms', component: ChatroomsComponent },
      { path: 'adverts', component: AdvertComponent,
        children:[
          { path: 'my-ads', component: MyAdsComponent}
        ]},
      { path: 'personal-data', component: PersonalDataComponent},
      { path: 'settings', component: SettingsComponent }
    ]
  },



  // Wild-card component
  { path: 'not-found', component: PageNotFoundComponent},
  { path: '**', pathMatch: 'full', redirectTo: 'not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
