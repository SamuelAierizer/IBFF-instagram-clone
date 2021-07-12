import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
	providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor {

	constructor(
		private authService: AuthenticationService,
		private router: Router
	) { }

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(request).pipe(catchError(err => {
		  if (err.status === 401) {
			// auto logout if 401 response returned from api
			this.authService.logout();
			  this.router.navigate(['/login']);
		  }
	
			const error = err.error || err.statusText;
		  return throwError(error);
		}))
	  }
}
