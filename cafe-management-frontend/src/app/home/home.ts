import { Component, inject } from '@angular/core';
import { BestSeller } from '../best-seller/best-seller';
import { MaterialModule } from '../shared/material-module';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Signup } from '../signup/signup';

@Component({
  selector: 'app-home',
  imports: [BestSeller, MaterialModule],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  dialog = inject(MatDialog);

  handleSignUpAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = "550px";
    this.dialog.open(Signup, dialogConfig);
  }
}
