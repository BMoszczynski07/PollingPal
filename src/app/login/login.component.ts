import { Component } from '@angular/core';
import { HomeComponent } from '../home/home.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [HomeComponent, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  formData = {
    login: '',
    password: '',
  };

  handleLogin = (): void => {
    console.log(this.formData);
  };
}
