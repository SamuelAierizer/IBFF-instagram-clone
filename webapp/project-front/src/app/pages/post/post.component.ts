import { Component, Input, NgModule, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Role } from 'src/app/model/Role';
import { JwtResponse } from 'src/app/response/JwtResponse';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { PostService } from 'src/app/services/post.service';
//import  saveAs  from 'file-saver';

@Component({
	selector: 'app-post',
	templateUrl: './post.component.html',
	styleUrls: ['./post.component.sass']
})

export class PostComponent implements OnInit {
	@Input() post?: any;
	postId: any;

	currentUser!: JwtResponse;
	Role = Role;

	authenticated = false;

	liked: any;
	likeCount: any;

	viewComments = false;
	alreadyLoaded = false;
	commentForm!: FormGroup;

	commentPayload = {
		postId: 0,
		username: '',
		text: ''
	};

	comments!: any[];
	fileUrl: any;
	sanitizer: any;

	constructor(private postService: PostService,
		private authService: AuthenticationService) { }

	ngOnInit(): void {
		this.liked = false;
		this.viewComments = false;

		this.commentForm = new FormGroup({
			text: new FormControl('', Validators.required)
		});

		this.currentUser = this.authService.currentUserValue;

		this.likeCount = this.post.likes;
		this.postId = this.post.id;

		if (this.post.username != this.currentUser.username) {
			this.setIsLiked();
		}
	}

	toggleComments() {
		if (this.viewComments == true) {
			this.viewComments = false;
		} else {
			this.viewComments = true;
			if (this.alreadyLoaded == false) {
				this.getCommentsForPost();
				this.alreadyLoaded = true;
			}
		}
	}

	getCommentsForPost() {
		this.postService.getAllComments(this.postId).subscribe(data => {
			this.comments = data;
		}, err => {
			console.log(err.error.message);
		});
	}

	isSameUser(postUsername): boolean {
		return this.post.username == this.currentUser.username;
	}

	isLiked(): boolean {
		return this.liked;
	}

	setIsLiked() {
		this.postService.checkLiked(this.currentUser.username, this.postId).subscribe(data => {
			this.liked = data;
		});
	}

	setLikeCount() {
		this.postService.getLikeCount(this.post).subscribe(data => {
			this.likeCount = data;
		});
	}

	like() {
		this.likeCount = this.likeCount + 1;
		this.postService.setLike(this.currentUser.username, this.postId).subscribe(
			data => {
				this.likeCount = data;
				this.liked = true;
			},
			err => {
				this.likeCount = this.likeCount - 1;
				console.log(err.error.message);
			}
		);
	}

	dislike() {
		this.likeCount = this.likeCount - 1;
		this.postService.setDislike(this.currentUser.username, this.postId).subscribe(
			data => {
				this.likeCount = data;
				this.liked = false;
			},
			err => {
				this.likeCount = this.likeCount + 1;
				console.log(err.error.message);
			}
		);
	}

	share() {
		this.postService.share(this.currentUser.username, this.postId).subscribe(
			data => {
				console.log("share success");
			},
			err => {
				console.log(err.error.message);
			}
		);
	}

	postComment() {
		this.commentPayload.text = this.commentForm.get('text')?.value;
		this.commentPayload.postId = this.postId;
		this.commentPayload.username = this.currentUser.username;

		this.postService.submitComment(this.commentPayload).subscribe(data => {
			this.commentForm.get('text')?.setValue('');
			this.getCommentsForPost();
		}, err => console.log(err.error.message));
	}

	export() {
		this.postService.exportComments(this.postId).subscribe((data) => {
			if(data) {
				let typeForBlob = 'text/xml;charset=utf-8';
				let blob = new Blob([data as BlobPart],{type:typeForBlob});
				//saveAs(blob, "comment-data.xml");

			}
		});
	}
}
