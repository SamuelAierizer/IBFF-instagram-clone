import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
	selector: 'app-registration',
	templateUrl: './registration.component.html',
	styleUrls: ['./registration.component.sass']
})
export class RegistrationComponent implements OnInit {
	form: any = {};
	isSuccessful = false;
	isSignUpFailed = false;
	errorMessage = '';

	constructor(private authService: AuthenticationService) { }

	ngOnInit(): void {
	}

	onSubmit() {
		this.authService.register(this.form).subscribe(
			data => {
				console.log(data);
				this.isSuccessful = true;
				this.isSignUpFailed = false;
			},
			err => {
				this.errorMessage = err.error.message;
				this.isSignUpFailed = true;
			}
		);
	}
}
