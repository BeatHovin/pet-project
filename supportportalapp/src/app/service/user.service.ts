import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { CustomHttpRespone } from '../model/custom-http-response';
import { NewPassword } from '../model/newPassword';

@Injectable({providedIn: 'root'})
export class UserService {
  private host = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/user/list`);
  }

  public updateUser(formData: FormData): Observable<User> {
    return this.http.post<User>(`${this.host}/user/update`, formData);
  }

  public updatePassword(formData: FormData): Observable<NewPassword> {
    return this.http.post<NewPassword>(`${this.host}/user/updatePassword`, formData);
  }

  public addUsersToLocalCache(users: User[]): void {
    localStorage.setItem('users', JSON.stringify(users));
  }

  public getUsersFromLocalCache(): User[] {
    if (localStorage.getItem('users')) {
        return JSON.parse(localStorage.getItem('users'));
    }
    return null;
  }

  public createUserFormDate(loggedInEmail: string, user: User): FormData {
    const formData = new FormData();
    formData.append('currentEmail', loggedInEmail);
    formData.append('firstName', user.firstName);
    formData.append('lastName', user.lastName);
    formData.append('email', user.email);
    formData.append('phoneNumber', user.phoneNumber);
    formData.append('role', user.role);
    formData.append('isActive', JSON.stringify(user.active));
    formData.append('isNonLocked', JSON.stringify(user.notLocked));

    return formData;
  }

  public createPasswordFormDate(loggedInEmail: string, newPassword: NewPassword): FormData {
    const formData = new FormData();
    formData.append('currentEmail', loggedInEmail);
    formData.append('currentPassword', newPassword.currentPassword);
    formData.append('newPassword', newPassword.newPassword);

    return formData;
  }

}
