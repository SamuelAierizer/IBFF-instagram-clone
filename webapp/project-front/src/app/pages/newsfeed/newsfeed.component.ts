import { trigger, state, style, transition, animate } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { JwtResponse } from 'src/app/response/JwtResponse';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { PostService } from 'src/app/services/post.service';

@Component({
	selector: 'app-newsfeed',
	templateUrl: './newsfeed.component.html',
	styleUrls: ['./newsfeed.component.sass']
})
export class NewsfeedComponent implements OnInit {

    currentUser!: JwtResponse;

	posts?: any[];

	constructor( private authService: AuthenticationService,
		private postService: PostService) { }

	ngOnInit(): void {

		this.currentUser = this.authService.currentUserValue;

        if (this.currentUser) {
			this.postService.getFeed(this.currentUser.username).subscribe(data => {
				this.posts = data;
			});
        } 
	}

}
