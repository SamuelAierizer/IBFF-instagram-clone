import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { User } from 'src/app/model/user';
import { AdminService } from 'src/app/services/admin.service';

@Component({
	selector: 'app-admin',
	templateUrl: './admin.component.html',
	styleUrls: ['./admin.component.sass']
})
export class AdminComponent implements OnInit {
	userArray: User[] = [];
	dtOptions: DataTables.Settings = {};
	dtTrigger: Subject<any> = new Subject();

	users!: User[];
	user: User = new User();
	deleteMessage = false;
	userList: any;
	isUpdated = false;

	modalUser: any;

	loggedInUserCount: any;

	constructor(private router: Router, private adminService: AdminService) { }

	addUser() {
		this.router.navigate['register'];
	}

	ngOnInit(): void {
		this.isUpdated = false;
		this.dtOptions = {
			pageLength: 6,
			stateSave: true,
			lengthMenu:[[6, 16, 20, -1], [6, 16, 20, "All"]],
			processing: true
		};


		this.adminService.getAll().subscribe(data => {
			this.users = data;
			this.dtTrigger.next();
		});

		this.adminService.getLoggedUserCount().subscribe(data => {
			this.loggedInUserCount = data;
		});
	}

	deleteUser(id) {
		this.adminService.delete(id).subscribe( data => {
			console.log(data);
			this.deleteMessage = true;
			this.adminService.getAll().subscribe( data => {
				this.users = data;
			},
			error => console.log(error));
		});
	}

	updateUser(id) {
		this.adminService.get(id).subscribe( data => {
			this.userList = data;
			this.modalUser = data;
		}, error => console.log(error));
	}

	userUpdateForm = new FormGroup({
		user_name: new FormControl(),
		user_email: new FormControl(),
		user_username: new FormControl()
	})

	update(updsusr) {
		this.user = this.userList;
		this.user.name = this.UserName?.value;
		this.user.email = this.UserEmail?.value;
		this.user.username = this.UserUsername?.value;

		this.adminService.updateUser(this.user).subscribe( data => {
			this.isUpdated = true;
			this.adminService.getAll().subscribe( data => {
				this.users = data;
			})
		}, error => console.log(error));
	}

	get UserName() {
		return this.userUpdateForm.get('user_name');
	}

	get UserEmail() {
		return this.userUpdateForm.get('user_email');
	}

	get UserUsername() {
		return this.userUpdateForm.get('user_username');
	}

	changeisUpdated() {
		this.isUpdated = false;
	}

	isAdmin(user): boolean {
		return user.adminKey;
	}
}
