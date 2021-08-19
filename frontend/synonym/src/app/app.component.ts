import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
  title = 'synonym';
  constructor (private router: Router) {}

  ngOnInit() {
  }

  navigate(url: string){
    this.router.navigateByUrl(url);
  }
}
