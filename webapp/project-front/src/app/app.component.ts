import { Component, OnInit } from '@angular/core';
import { WebSocketService } from './services/web-socket.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from './services/authentication.service';
import { JwtResponse } from './response/JwtResponse';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.sass']
})
export class AppComponent {
	public notifications = 0;

	currentUser!: JwtResponse;

	constructor(
		private snackBar: MatSnackBar,
		private authService: AuthenticationService,
		private webSocketService: WebSocketService) {

		this.currentUser = this.authService.currentUserValue;


		//Open connection with server socket
		let stompClient = this.webSocketService.connect();

		stompClient.connect({}, frame => {

			//Subscribe to notification topic
			stompClient.subscribe('/topic/notification', notifications => {
				this.openSnackBar(notifications.body);
			})
		});

		if (this.currentUser) {

			stompClient.connect({}, frame => {

				stompClient.subscribe('/user/' + this.currentUser.username + '/notify', data => {
					var response = JSON.parse(data.body);
					this.openSnackBar(response.message);
				})
			});
		}
	}

	openSnackBar(message: string) {
		this.snackBar.open(message, "close");
	}
}
