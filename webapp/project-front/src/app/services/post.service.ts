import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication.service';

const baseURL = 'http://localhost:8080/profile/p/';
const commentURL = 'http://localhost:8080/comment/';

@Injectable({
	providedIn: 'root'
})
export class PostService {

	constructor(private http: HttpClient, private auth: AuthenticationService) { }

	uploadPost(id: any, uploadData) {
		return this.http.post(baseURL + id + "/create", uploadData);
	}

	getFeed(username): Observable<any[]> {
		return this.http.get<any[]>(baseURL + username + "/feed");
	}

	getAllPosts(username): Observable<any[]> {
		return this.http.get<any[]>(baseURL + username + "/all");
	}

	checkLiked(username, postId) {
		return this.http.get(baseURL + username + "/liked/" + postId);
	}

	getLikeCount(post) {
		return this.http.get(baseURL + post.id + "/likeCount");
	}

	setLike(username, postId) {
		return this.http.post(baseURL + "like", {
			username: username,
			postId: postId
		});
	}

	setDislike(username, postId) {
		return this.http.post(baseURL + "dislike", {
			username: username,
			postId: postId
		});
	}

	share(username, postId) {
		return this.http.post(baseURL + "share", {
			username: username,
			postId: postId
		});
	}

	getAllComments(postId): Observable<any[]> {
		return this.http.get<any[]>(commentURL + postId);
	}

	submitComment(commentPayload) {
		return this.http.post(commentURL + "submit", commentPayload);
	}

	exportComments(postId) {
		return this.http.get(commentURL + "export/" + postId);
	}
}
