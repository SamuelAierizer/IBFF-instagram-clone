import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.sass']
})

export class LoginComponent implements OnInit {
	model: any = {
		username: '',
		password: '',
		remembered: ''
	};

	isInvalid!: boolean;
    submitted = false;
	returnUrl = '/';

	authenticated = false;
	username = "";

	constructor(
		private router: Router,
		private route: ActivatedRoute,
		private authService: AuthenticationService, 
		private cookieService: CookieService) { }

	ngOnInit(): void {
		let params = this.route.snapshot.queryParamMap;
        this.returnUrl = params.get('returnUrl') || '';
	}

	onSubmit(): void {
		this.submitted = true;
		this.authService.login(this.model).subscribe(
			user => {
				this.router.navigate(['/profile/' + user.username]);		
		}, err =>{
			this.isInvalid = true;
		});
	}

	isAuthenticated(): boolean {
        return this.authenticated;
    }
}
