import { Component, OnInit, Pipe, PipeTransform } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JwtResponse } from 'src/app/response/JwtResponse';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { PostService } from 'src/app/services/post.service';
import { ProfileService } from 'src/app/services/profile.service';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.sass']
})

export class ProfileComponent implements OnInit{

    currentUser!: JwtResponse;

	following = false;
	isUpdated = false;

	profile: any;
	profileUsername: any;

	currentNumbers: any;
	postCount: number = 0;
	followersCount: number = 0;
	followingCount: number = 0;

	base64Data: any;
	selectedFile!: File;
	postCaption = "The default caption";

	posts?: any[];
	temp: any;
	image: any;

	constructor(
		private rout: ActivatedRoute,
		private authService: AuthenticationService,
		private profileService: ProfileService, 
		private postService: PostService) { }

	ngOnInit(): void {
		this.rout.params.subscribe(params => {
			this.profileUsername = params['username'];
		});

		this.currentUser = this.authService.currentUserValue;

		this.isUpdated = false;
		
		this.getProfile(this.profileUsername);
		this.setNumbers(this.profileUsername);

		this.postService.getAllPosts(this.profileUsername).subscribe(data => {
			this.posts = data;
		});
	}

	getProfile(username): void {
		this.profileService.getByUsername(username).subscribe(data => {
			this.profile = data;
		}, error => console.log(error));
	}

	setNumbers(username): void {
		this.profileService.getNumbers(username).subscribe(data => {
			this.currentNumbers = data;

			this.postCount = this.currentNumbers.postCount;
			this.followersCount = this.currentNumbers.followersCount;
			this.followingCount = this.currentNumbers.followingCount;
		}, error => console.log(error));
	}

	canUpdate(): boolean {
		if (!this.currentUser){
			return false;
		} else {
			return this.profileUsername == this.currentUser.username;
		}
	}

	follow(): void {
		this.profileService.setFollowing(this.currentUser.username, this.profileUsername).subscribe(data => {
		}, error => console.log(error));

		setTimeout(() => {
			this.setNumbers(this.profileUsername);
			this.checkFollowing(this.currentUser.username, this.profileUsername);
		}, 500);
	}

	unfollow(): void {
		this.profileService.setUnfollowing(this.currentUser.username, this.profileUsername).subscribe(data => {
			console.log(data);
		}, error => console.log(error));

		setTimeout(() => {
			this.setNumbers(this.profileUsername);
			this.checkFollowing(this.currentUser.username, this.profileUsername);
		}, 500);
	}

	isFollowing(): boolean {
		return this.following;
	}

	checkFollowing(username, target): void {
		this.profileService.checkFollowing(username, target).subscribe(data => {
			var temp = data;
			var active = Object.values(temp);

			this.following = (active.toString() == "true");
		}, error => console.log(error));
	}

	profileUpdateForm = new FormGroup({
		profile_title: new FormControl(),
		profile_description: new FormControl(),
		profile_url: new FormControl()
	})

	changeisUpdated() {
		window.location.reload();
	}

	wasUpdated(): boolean {
		return this.isUpdated;
	}

	update(prof) {
		this.profile.title = this.ProfileTitle?.value;
		this.profile.description = this.ProfileDescription?.value;
		this.profile.url = this.ProfileUrl?.value;

		this.profileService.updateProfile(this.profileUsername, this.profile).subscribe( data => {
			this.isUpdated = true;
			this.getProfile(this.profileUsername);
		}, error => console.log(error));
	}

	get ProfileTitle() {
		return this.profileUpdateForm.get('profile_title');
	}

	get ProfileDescription() {
		return this.profileUpdateForm.get('profile_description');
	}

	get ProfileUrl() {
		return this.profileUpdateForm.get('profile_url');
	}

	Uint8ToBase64(u8Arr){
		var CHUNK_SIZE = 0x8000; //arbitrary number
		var index = 0;
		var length = u8Arr.length;
		var result = '';
		var slice;
		while (index < length) {
			slice = u8Arr.subarray(index, Math.min(index + CHUNK_SIZE, length));
			result += String.fromCharCode.apply(null, slice);
			index += CHUNK_SIZE;
		}
		return btoa(result);
	}

	postData: any = {
		caption: '',
		file: ''
	};

	onFileChanged(event) {
		let selectedFile = event.target.files[0];

		let fileReader = new FileReader();
 		fileReader.readAsArrayBuffer(selectedFile);
 		fileReader.onload = () => {
			// @ts-ignore
			let array = new Uint8Array(fileReader.result);
			let base64String = this.Uint8ToBase64(array);
			this.postData.file = base64String;
		};
	}

	sendPost() {
		this.postData.caption = this.postCaption;
		this.postService.uploadPost(this.currentUser.id , this.postData).subscribe((response) => {
			console.log("Posted");
		}, error => console.log(error));
	}
}

