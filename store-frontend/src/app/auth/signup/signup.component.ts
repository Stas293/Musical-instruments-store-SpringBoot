import {Component, DestroyRef, inject} from '@angular/core';
import {Router} from '@angular/router';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ListErrorsComponent} from '../../errors/list-errors.component';

interface SignupForm {
  login: FormControl<string>;
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  phone: FormControl<string>;
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
}

@Component({
  selector: 'app-signup',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ListErrorsComponent
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  signupForm: FormGroup<SignupForm>;
  errors: any | null = null;
  isSubmitting = false;
  destroyRef = inject(DestroyRef);

  constructor(private authService: UserService,
              private router: Router) {
    this.signupForm = new FormGroup<SignupForm>({
      login: new FormControl("", {
        validators: [Validators.required, Validators.pattern('^\\w+$')],
        nonNullable: true,
      }),
      firstName: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      email: new FormControl("", {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
      phone: new FormControl("", {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl("", {
        validators: [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}')],
        nonNullable: true,
      }),
      confirmPassword: new FormControl("", {
        validators: [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}')],
        nonNullable: true,
      }),
    });
  }

  submitForm() {
    if (this.signupForm.value.password !== this.signupForm.value.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    this.isSubmitting = true;
    this.errors = {errors: {}};

    this.authService.register(this.signupForm.value as {
      login: string;
      firstName: string;
      lastName: string;
      email: string;
      phone: string;
      password: string
    }).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (login) => {
          alert(`Signup successful, please login, ${login}`);
          this.router.navigate(['/login']);
        },
        error: (err) => {
          alert('Signup failed: ' + err.message);
          this.errors = err;
          this.isSubmitting = false;
        },
        complete(): void {
        }
      });
  }
}
