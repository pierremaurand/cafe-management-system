import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../services/user';
import { Router } from '@angular/router';
import { SnackbarService } from '../services/snackbar';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalContants } from '../shared/global-constants';
import { MaterialModule } from '../shared/material-module';

@Component({
  selector: 'app-signup',
  imports: [MaterialModule],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class Signup implements OnInit{
  password = true;
  confirmPassword = true;
  signUpForm:any = FormGroup;
  responseMessage:any;

  userService = inject(UserService);
  snackBarService = inject(SnackbarService);
  fb = inject(FormBuilder);
  router = inject(Router);
  ngxService = inject(NgxUiLoaderService);

  public dialogRef = inject(MatDialogRef<Signup>);

  ngOnInit(): void {
    this.signUpForm = this.fb.group({
      name: [null, [Validators.required, Validators.pattern(GlobalContants.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalContants.emailRegex)]],
      contactNumber: [null, [Validators.required, Validators.pattern(GlobalContants.contactNumberRegex)]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]]
    });
  }

  validateSubmit() {
    if(this.signUpForm.password.value != this.signUpForm.confirmPassword.value) {
      return true;
    }
    return false;
  }

  handleSubmit() {
    this.ngxService.start();
    var formData = this.signUpForm.value;
    var data = {
      name: formData.name,
      email: formData.email,
      contactNumber: formData.contactNumber,
      password: formData.password
    }

    this.userService.signUp(data).subscribe({
      next:(response:any) => {
      this.ngxService.stop();
      this.dialogRef.close();
      this.responseMessage = response?.message;
      this.snackBarService.openSnackBar(this.responseMessage, "");
      this.router.navigate(['/']);
      }, 
      error: (error) => {
        this.ngxService.stop();
        if(error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalContants.genericError;
        }
        this.snackBarService.openSnackBar(this.responseMessage, GlobalContants.error);
      }
  });
  }

}
