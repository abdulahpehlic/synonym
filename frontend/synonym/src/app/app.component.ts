import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { WordService } from './word/service/word-service';
import { WordResponse } from './word/model/word-response';

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
  wordsGrouped: WordResponse[][];
  wordSearchForm: FormGroup;
  selectedWord: string;

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
        this.wordsGrouped = this.groupSynonyms(this.words);
        this.selectedWord = wordSearchFormValue.wordString;
        console.log(JSON.stringify(this.wordsGrouped))
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
  
  noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }
}
