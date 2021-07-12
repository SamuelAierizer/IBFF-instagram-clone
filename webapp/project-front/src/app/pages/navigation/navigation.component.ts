import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Role } from 'src/app/model/Role';
import { JwtResponse } from 'src/app/response/JwtResponse';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
	selector: 'app-navigation',
	templateUrl: './navigation.component.html',
	styleUrls: ['./navigation.component.sass']
})
export class NavigationComponent implements OnInit, OnDestroy{

    form: any = {};
    username$;
    username!: string;

    userUsername!: string;

    currentUserSubscription!: Subscription;
    currentUser!: JwtResponse;
    Role = Role;

    constructor(
        private router: Router,
        private authService: AuthenticationService
    ) { }

    ngOnInit(): void {
        this.username$ = this.authService.username$.subscribe(username => this.username = username);
        this.currentUserSubscription = this.authService.currentUser.subscribe(user => {
            this.currentUser = user || '';
            this.userUsername = this.currentUser.username || '';
        });
    }

    ngOnDestroy(): void {
        this.currentUserSubscription.unsubscribe();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/newsfeed']);
    }

    search(): void {
        this.router.navigate(['/profile', this.form.username])
            .then(() => window.location.reload());
    }
}
