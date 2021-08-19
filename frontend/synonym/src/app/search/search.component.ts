import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { WordResponse } from '../core/word/model/word-response';
import { WordService } from '../core/word/service/word-service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  myControl = new FormControl();
  options: string[] = ['One', 'Two', 'Three'];
  filteredValues: any;
  words: WordResponse[];
  wordsGrouped: WordResponse[][];
  wordSearchForm: FormGroup;
  selectedWord: string;

  constructor (private wordService: WordService, private router: Router) {}

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
        this.wordsGrouped = this.groupSynonyms(this.words);
        this.selectedWord = wordSearchFormValue.wordString;
      },
      (err) => {
        console.error(JSON.stringify(err));
      },
      () => {
        
      }
    );
  }

  isWordEmpty(word: any) {
    return (word == null) || (word.length == 0)
  }

  groupSynonyms(words: WordResponse[]) {
    return words.reduce(function (r, a) {
      r[a.synonymGroup] = r[a.synonymGroup] || [];
      r[a.synonymGroup].push(a);
      return r;
  }, [Object.create([])]);
  }
  
  navigate(url: string){
    this.router.navigateByUrl(url);
  }

  noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }

}
