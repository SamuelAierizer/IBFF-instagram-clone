import { NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { AuthenticationService } from './services/authentication.service';
import { LoginComponent } from './pages/login/login.component';
import { NewsfeedComponent } from './pages/newsfeed/newsfeed.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AuthGuard } from './helper/auth.guard';

const routes: Routes = [
	{path:'login', component:LoginComponent},
	{path:'registration', component:RegistrationComponent},
	{path:'profile/:username', component:ProfileComponent},
	{path:'newsfeed', component:NewsfeedComponent},
	{path:'admin', component:AdminComponent, canActivate: [AuthGuard]},
	{path:'', redirectTo:'newsfeed', pathMatch:'full'},
	{path:'**', component:NotFoundComponent}
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
