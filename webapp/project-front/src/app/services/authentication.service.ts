import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { JwtResponse } from '../response/JwtResponse';
import { CookieService } from 'ngx-cookie-service';
import { Subject } from 'rxjs';

const baseURL: string = "http://localhost:8080/";

@Injectable({ providedIn: 'root' })

export class AuthenticationService implements OnInit{

    private currentUserSubject!: BehaviorSubject<JwtResponse>;
    public currentUser!: Observable<JwtResponse>;
    public nameTerms = new Subject<string>();
    public username$ = this.nameTerms.asObservable();

    usrname!: any;

    get currentUserValue() {
        return this.currentUserSubject.value;
    }

    constructor(private http: HttpClient, private cookieService: CookieService) {
        const memo = localStorage.getItem('currentUser')!;
        this.currentUserSubject = new BehaviorSubject<JwtResponse>(JSON.parse(memo));
        this.currentUser = this.currentUserSubject.asObservable();
        cookieService.set('currentUser', memo);
    }

    ngOnInit(): void {
	}

    login(loginForm): Observable<any> {
        return this.http.post<JwtResponse>(baseURL + 'login', loginForm).pipe(
            tap(user => {
                if(user && user.token) {
                    this.cookieService.set('currentUser', JSON.stringify(user));
                    localStorage.setItem('currentUser', JSON.stringify(user));

                    if (loginForm.remembered){
                        localStorage.setItem('currentUser', JSON.stringify(user));
                    }
                    this.nameTerms.next(user.username);
                    this.currentUserSubject.next(user);
                    this.usrname = user.username;
                    return user;
                }
                return ;
            }), 
            //catchError(this.handleError('Login Failed', null))
        );
    }

    logout() {
        this.http.post(baseURL + 'setLogout', {}).subscribe(
            data => console.log(data), err => console.log(err.error.message)
        );
        this.currentUserSubject.next(null!);
        localStorage.removeItem('currentUser');
        this.cookieService.delete('currentUser');
    }

    register(user): Observable<any> {
        return this.http.post(baseURL + 'registration', {
            name: user.name,
            email: user.email,
            username: user.username,
            password: user.password
        });
    }
}
