import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { WordService } from '../core/word/service/word-service';

@Component({
  selector: 'app-create-word',
  templateUrl: './create-word.component.html',
  styleUrls: ['./create-word.component.css']
})
export class CreateWordComponent implements OnInit {

  selectedSynonyms: String[] = [];
  possibleDescriptions: any[] = [];
  possibleSynonyms : any[] = [];
  suggestedSynonyms: any[] = [];
  synonymObjectArray: any[] = [];
  wordAddForm: FormGroup;
  synonymAddForm: FormGroup;
  isWordSubmitted: boolean = false;
  wordSelected: String;
  jsonDataThesaurus: any[] = [];
  selectedDefinition: String;

  constructor(private wordService: WordService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.initForms();
  }

  initForms(){
    this.wordAddForm = new FormGroup({
      wordString: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(30), this.noWhitespaceValidator])
    })
    
    this.synonymAddForm = new FormGroup({
      wordString: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(30), this.noWhitespaceValidator])
    })
  }

  getPossibleDescriptions(wordAddForm: any) {
    this.possibleDescriptions = [];
    this.wordService.fetchThesaurusResponse(wordAddForm.wordString).subscribe(
      (data: any) => {
        this.jsonDataThesaurus = data.data.definitionData.definitions;
        this.jsonDataThesaurus.forEach((definition: any) => {
          this.possibleDescriptions.push(definition.definition);
        });
        
        this.isWordSubmitted = true;
        this.wordSelected = wordAddForm.wordString;
      },
      (err) => {
        console.error(JSON.stringify(err));
      },
      () => {
        
      }
    );
  }

  handleSynonymSuggestionPick(synonym: String) {
    this.selectedSynonyms.push(synonym);
    this.suggestedSynonyms.forEach((element,index)=>{
      if(element === synonym) {
        this.suggestedSynonyms.splice(index,1);
      }
    });
    this.synonymAddForm.reset();
  }

  addSynonymUsingInput(synonym: String) {
    if (this.selectedSynonyms.indexOf(synonym) !== -1) {
      alert("Synonym already picked!")
      return;
    }
    if (this.possibleSynonyms.indexOf(synonym) !== -1) {
      this.handleSynonymSuggestionPick(synonym);
      return;
    }
    if (this.possibleSynonyms.indexOf(synonym) !== -1) {
      this.selectedSynonyms.push(synonym)
    }
    else {
      alert(synonym + " is not a synonym! Please enter a word which is a synonym to " + this.wordSelected)
    }
  }

  handleSynonymSuggestionRemove(synonym: String) {
    this.synonymAddForm.reset();
    let hasSynonymMaxSimilarity;
    this.synonymObjectArray.forEach((element)=>{
      if(element.similarity !== "100" && element.term === synonym) {
        this.selectedSynonyms.splice(this.selectedSynonyms.indexOf(synonym), 1);
        hasSynonymMaxSimilarity = false;
      }
    });
    if (hasSynonymMaxSimilarity === false) {
      return;
    }
    this.suggestedSynonyms.push(synonym);
    this.selectedSynonyms.forEach((element,index)=>{
      if(element === synonym) {
        this.selectedSynonyms.splice(index,1);
      }
    });
  }

  getPotentialSynonyms(description: String) {
    this.selectedDefinition = description;
    this.jsonDataThesaurus.forEach((definition: any) => {
      if (definition.definition === description) {
        this.synonymObjectArray = definition.synonyms;
      }
    });
    this.synonymObjectArray.forEach((synonym) => {
        this.possibleSynonyms.push(synonym.term);
        if (synonym.similarity === "100") {
          this.suggestedSynonyms.push(synonym.term);
        }
    });
  }

  addSynonyms(wordAddFormValue: any) {

  }

  onCancelModal(){
    this.selectedSynonyms = [];
    this.possibleSynonyms = [];
    this.suggestedSynonyms = [];
    this.dialog.closeAll();
    this.synonymAddForm.reset();
  }

  openModal(templateRef: any) {
    let dialogRef = this.dialog.open(templateRef, {
        width: '350px',
        panelClass: 'modal',
    });

    dialogRef.afterClosed().subscribe(result => {
      this.synonymAddForm.reset();
    });
  }
  
  noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }
}
