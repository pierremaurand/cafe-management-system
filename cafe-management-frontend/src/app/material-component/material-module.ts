import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MaterialRoutingModule } from './material-routing-module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CdkTableModule } from '@angular/cdk/table';
import { MaterialModule } from '../shared/material-module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MaterialRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    CdkTableModule
  ]
})
export class MaterialComponentsModule { }
