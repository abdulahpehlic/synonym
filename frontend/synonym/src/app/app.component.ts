import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { from, Observable } from 'rxjs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WordService } from './word/service/word-service';
import { WordResponse } from './word/model/word-response';
import { groupBy, mergeMap, toArray } from 'rxjs/operators';
import { group } from '@angular/animations';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'synonym';
  myControl = new FormControl();
  options: string[] = ['One', 'Two', 'Three'];
  filteredValues: any;
  words: WordResponse[];
  wordSearchForm: FormGroup;

  constructor (private wordService: WordService) {}

  ngOnInit() {
    this.initForms();
  }

  initForms(){
    this.wordSearchForm = new FormGroup({
      wordString: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30), this.noWhitespaceValidator])
    })
  }

  getSynonyms(wordSearchFormValue: any){
    
    this.wordService.fetchWords(wordSearchFormValue.wordString).subscribe(
      (data: any) => {
        this.words = data;
        this.words = this.groupSynonyms(this.words)
      },
      (err) => {
        console.error(JSON.stringify(err));
      },
      () => {
        
      }
    );
  }

  groupSynonyms(words: WordResponse[]) {
    return words.reduce((r, a) => {
      r[a.synonymGroup] = r[a.synonymGroup] || [];
      r[a.synonymGroup].push(a);
      return r;
  }, Object.create(null));
  }
  noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }
}
