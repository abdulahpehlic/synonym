import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { WordRequest } from '../core/word/model/word-request';
import { WordResponse } from '../core/word/model/word-response';
import { WordService } from '../core/word/service/word-service';

@Component({
  selector: 'app-create-word',
  templateUrl: './create-word.component.html',
  styleUrls: ['./create-word.component.css']
})
export class CreateWordComponent implements OnInit {

  selectedSynonyms: string[] = [];
  possibleDescriptions: any[] = [];
  possibleSynonyms : any[] = [];
  suggestedSynonyms: any[] = [];
  synonymObjectArray: any[] = [];
  wordAddForm: FormGroup;
  synonymAddForm: FormGroup;
  isWordSubmitted: boolean = false;
  wordSelected: string;
  jsonDataThesaurus: any[] = [];
  selectedDefinition: string;
  existingSynonyms: string[] = [];
  isThereExistingSynonyms: boolean = false;
  wordAddRequest: any[] = [];
  existingSynonymGroup: number = -1;
  existingDefinition: string;
  wordAddResponseData: any;

  constructor(private wordService: WordService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.initForms();
  }

  initForms(){
    this.wordAddForm = new FormGroup({
      wordString: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(30), this.noWhitespaceValidator])
    })
    
    this.synonymAddForm = new FormGroup({
      wordString: new FormControl('', [Validators.maxLength(30)])
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

  handleSynonymSuggestionPick(synonym: string) {
    this.selectedSynonyms.push(synonym);
    this.suggestedSynonyms.forEach((element,index)=>{
      if(element === synonym) {
        this.suggestedSynonyms.splice(index,1);
      }
    });
    this.synonymAddForm.reset();
  }

  addSynonymUsingInput(synonym: string) {
    if (this.selectedSynonyms.indexOf(synonym) !== -1) {
      alert("Synonym already picked!")
      return;
    }
    if (this.existingSynonyms.indexOf(synonym) !== -1) {
      alert("Synonym already exists in the database!")
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

  handleSynonymSuggestionRemove(synonym: string) {
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

  getPotentialSynonyms(description: string) {
    this.getExistingSynonyms(description);
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

  getExistingSynonyms(description: string) {
    this.wordService.fetchWordsByDescription(description).subscribe(
      (data: WordResponse[]) => {
        if (data.length !== 0) {
          this.existingDefinition = data[0].description;
          this.existingSynonymGroup = data[0].synonymGroup;
          data.forEach((synonymObject: any) => {
            this.existingSynonyms.push(synonymObject.word);
          });
          this.existingSynonyms.splice(this.existingSynonyms.indexOf(this.wordSelected), 1)
          this.isThereExistingSynonyms = true;
          this.existingSynonyms.forEach((existingSynonym: any) => {
            this.suggestedSynonyms.forEach((suggestedSynonym: any, index) => {
              if (suggestedSynonym === existingSynonym) {
                this.suggestedSynonyms.splice(index, 1);
              }
            });
            this.possibleSynonyms.forEach((possibleSynonym: any, index) => {
              if (possibleSynonym === existingSynonym) {
                this.possibleSynonyms.splice(index, 1);
              }
            });
          });
        }
        else {
          this.isThereExistingSynonyms = false;
        }
      },
      (err) => {
        console.error(JSON.stringify(err));
      },
      () => {
        
      }
    );
  }

  addSynonyms() {
    if (this.existingSynonymGroup !== -1 || this.existingDefinition !== this.selectedDefinition) {
      this.selectedSynonyms.forEach((selectedSynonym: any) => {
        this.wordAddRequest.push( {
          word: selectedSynonym,
          description: this.selectedDefinition,
          synonymGroup: this.existingSynonymGroup
        })
      });
      if (this.existingSynonyms.indexOf(this.wordSelected) !== -1 || this.existingDefinition !== this.selectedDefinition) {
        const pivotWord = {
          word: this.wordSelected,
          description: this.selectedDefinition,
          synonymGroup: this.existingSynonymGroup
        }
        this.wordAddRequest.push(pivotWord);
      }
    }
    else {
      this.selectedSynonyms.forEach((selectedSynonym: any) => {
        this.wordAddRequest.push({
          word: selectedSynonym,
          description: this.selectedDefinition
        })
      });
      if (this.existingSynonyms.indexOf(this.wordSelected) !== -1) {
        const pivotWord : WordRequest = {
          word: this.wordSelected,
          description: this.selectedDefinition
        }
        this.wordAddRequest.push(pivotWord);
      }
    }
    
    this.wordService.addWords(this.wordAddRequest).subscribe(
      (data: any) => {
        this.wordAddResponseData = data;
      },
      (err) => {
        console.error(JSON.stringify(err));
      },
      () => {
      }
    );
  }

  onCancelModal(){
    this.selectedSynonyms = [];
    this.possibleSynonyms = [];
    this.suggestedSynonyms = [];
    this.existingSynonyms = [];
    this.wordAddRequest = [];
    this.existingSynonymGroup = -1;
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
      this.onCancelModal();
    });
  }
  
  noWhitespaceValidator(control: FormControl) {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { 'whitespace': true };
  }
}
