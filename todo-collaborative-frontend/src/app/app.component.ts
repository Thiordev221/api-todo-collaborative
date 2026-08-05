import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
    <app-navbar></app-navbar>
    <main class="max-w-5xl mx-auto px-4 py-6">
      <router-outlet></router-outlet>
    </main>
  `,
})
export class AppComponent {}
