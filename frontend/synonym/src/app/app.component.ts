import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MediaObserver, MediaChange } from '@angular/flex-layout'
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  
  title = 'synonym';
  mediaSub: Subscription;
  deviceSm: boolean;
  constructor (private router: Router, public mediaObserver: MediaObserver) {}

  navigate(url: string){
    this.router.navigateByUrl(url);
  }

  currentRoute() {
    return this.router.url;
  }
}
