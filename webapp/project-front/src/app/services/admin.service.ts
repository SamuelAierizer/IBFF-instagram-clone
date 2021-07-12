import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { AuthenticationService } from './authentication.service';

const baseURL = 'http://localhost:8080/admin';
const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

@Injectable({
	providedIn: 'root'
})
export class AdminService {

	constructor(private http: HttpClient, private auth: AuthenticationService) { }

	getLoggedUserCount() {
		return this.http.get(baseURL + "/loggedInUsers");
	}
	
	getAll(): Observable<User[]> {
		return this.http.get<User[]>(baseURL);
	}

	get(id): Observable<any> {
		return this.http.get(baseURL + "/edit/" + id);
	}

	updateUser(user:any) {
		return this.http.put(baseURL + "/update/" + user.id, {
            name: user.name,
            email: user.email,
            username: user.username,
        }, httpOptions);
	}

	createUser(user:any) {
		return this.auth.register(user);
	}

	delete(id) {
		return this.http.delete(baseURL + "/delete/" + id);
	}

	getLoggedInUsers() {
		return this.http.get(baseURL + '/test/loggedInUsers/');
	}
}
