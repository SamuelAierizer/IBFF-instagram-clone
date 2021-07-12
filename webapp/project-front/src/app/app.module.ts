import { Injectable, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './pages/navigation/navigation.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { NewsfeedComponent } from './pages/newsfeed/newsfeed.component';
import { HttpClientModule, HttpHandler, HttpInterceptor, HttpRequest, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './pages/admin/admin.component';
import { DataTablesModule } from 'angular-datatables';
import { PostComponent } from './pages/post/post.component';
import { CookieService } from "ngx-cookie-service";
import { JwtInterceptorService } from './helper/jwt-interceptor.service';
import { ErrorInterceptorService } from './helper/error-interceptor.service';
import { WebSocketService } from './services/web-socket.service';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
	declarations: [
		AppComponent,
		NavigationComponent,
		LoginComponent,
		RegistrationComponent,
		ProfileComponent,
		NotFoundComponent,
  		NewsfeedComponent,
		AdminComponent,
  		PostComponent
	],
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		BrowserModule,
		AppRoutingModule,
		ReactiveFormsModule,
		HttpClientModule,
		DataTablesModule,
		NoopAnimationsModule,
		MatSnackBarModule
	],
	providers: [CookieService, WebSocketService,
	{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true},
	{provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true}],
	bootstrap: [AppComponent]
})
export class AppModule { }
