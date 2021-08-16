import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { catchError } from 'rxjs/operators';
import { throwError} from 'rxjs';
import { WordResponse } from '../model/word-response';

@Injectable({
  providedIn: 'root'
})
export class WordService {
  private url = 'http://localhost:8080/api/words';

  constructor(private http: HttpClient) { }

  fetchWords(word: String) {
    return this.http.get<WordResponse[]>(this.url + '/' + word).pipe(
      catchError(this.handleError)
    )
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      console.error('An error occurred:', error.error.message);
    } else {
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
