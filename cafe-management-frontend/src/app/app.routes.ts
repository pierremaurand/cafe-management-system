import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Full } from './layouts/full/full';

export const routes: Routes = [
    { path:'', component: Home},
    { 
        path: 'cafe',
        component: Full, 
        children: [
            {
                path: '',
                redirectTo: '/cafe/dashboard',
                pathMatch: 'full'
            },
            {
                path: '',
                loadChildren: () => import('./material-component/material-module').then(m => m.MaterialModule),
            },
            {
                path: 'dashboard',
                loadChildren: () => import('./dashboard/dashboard-module').then(m => m.DashboardModule),
            }
        ]
    },
    { path: '**', component: Home}
];
