import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {
  snackBar = inject(MatSnackBar);

  openSnackBar(message:string, action:string) {
    if(action === "error") {
      this.snackBar.open(message, '', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 2000,
        panelClass: ['black-snackbar']
      });
    } else {
      this.snackBar.open(message, '', {
        horizontalPosition: 'center',
        verticalPosition: 'top',
        duration: 2000,
        panelClass: ['green-snackbar']
      });
    }
  }
}
