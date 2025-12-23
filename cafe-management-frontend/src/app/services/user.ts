import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  url = environment.apiUrl;
  http = inject(HttpClient);

  signUp(data: any) {
    return this.http.post(this.url+"/user/signUp", data, {
      headers: new HttpHeaders().set("Content-Type", "application/json")
    })
  }
}
