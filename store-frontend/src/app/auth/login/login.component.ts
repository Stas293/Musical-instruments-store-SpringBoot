import {Component, DestroyRef, inject} from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ListErrorsComponent} from '../../errors/list-errors.component';

interface AuthForm {
  login: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    ListErrorsComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authForm: FormGroup<AuthForm>;
  errors: any | null = null;
  isSubmitting = false;
  destroyRef = inject(DestroyRef);

  constructor(private authService: UserService,
              private router: Router) {
    this.authForm = new FormGroup<AuthForm>({
      login: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }

  submitForm() {
    this.isSubmitting = true;
    this.errors = {errors: {}};

    let observable = this.authService.login(
      this.authForm.value as
        { login: string; password: string }
    );

    observable.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: () => void this.router.navigate(["/"]),
      error: (err) => {
        this.errors = err;
        this.isSubmitting = false;
      },
    });
  }
}
