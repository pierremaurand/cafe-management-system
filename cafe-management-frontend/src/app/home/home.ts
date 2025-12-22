import { Component } from '@angular/core';
import { BestSeller } from '../best-seller/best-seller';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  imports: [BestSeller, MatIcon],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {

}
