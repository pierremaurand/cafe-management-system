import { MediaMatcher } from '@angular/cdk/layout';
import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { Header } from './header/header';
import { RouterOutlet } from '@angular/router';
import { MaterialModule } from '../../shared/material-module';
import { Sidebar } from './sidebar/sidebar';

@Component({
  selector: 'app-full',
  imports: [Sidebar, Header, MaterialModule, RouterOutlet],
  templateUrl: './full.html',
})
export class Full implements OnDestroy, AfterViewInit {
  mobileQuery: MediaQueryList;

  private _mobileQueryListener: () => void;

  constructor(
    changeDetectorRef: ChangeDetectorRef,
    media: MediaMatcher
  ) {
    this.mobileQuery = media.matchMedia('(min-width: 768px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
  ngAfterViewInit() { }
}
