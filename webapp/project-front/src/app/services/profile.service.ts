import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

const baseURL = 'http://localhost:8080/';
const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
	providedIn: 'root'
})
export class ProfileService {

	constructor(private http: HttpClient) { }

	getByUsername(username): Observable<any> {
		return this.http.get(baseURL + "user/" + username);
	}

	setFollowing(username, target) {
		return this.http.post(baseURL + "profile/follows", {
			username: username,
			target: target
		}, httpOptions);
	}

	setUnfollowing(username, target) {
		return this.http.post(baseURL + "profile/unfollows", {
			username: username,
			target: target
		}, httpOptions);
	}

	getNumbers(username) {
		return this.http.get(baseURL + "profile/" + username +"/numbers");
	}

	checkFollowing(username, target){
		return this.http.get(baseURL + "profile/" + username + "/check/" + target); 
	}

	updateProfile(username, profile: any) {
		return this.http.put(baseURL + "profile/" + username + "/update", {
            title: profile.title,
            description: profile.description,
            url: profile.url,
        }, httpOptions);
	}
}
