import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { NgxUiLoaderModule, NgxUiLoaderRouterModule, POSITION, SPINNER } from 'ngx-ui-loader';

const ngxUiLoaderConfig = {
  text: "Loading...",
  textColor: "#FFFFFF",
  textPosition: POSITION.centerCenter,
  bgsColor: "#7b1fa2",
  fgsColor: "#7b1fa2",
  fgsType: SPINNER.squareJellyBox,
  fgsSize: 100,
  hasProgressBar: false
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom(NgxUiLoaderModule.forRoot(ngxUiLoaderConfig)),
    provideHttpClient(withFetch()),
    importProvidersFrom(NgxUiLoaderRouterModule), 
  ]
};
