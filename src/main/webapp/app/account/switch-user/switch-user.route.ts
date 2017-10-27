import { Route } from '@angular/router';
import {SwitchUserComponent} from './switch-user.component';


export const swichUserRoute: Route = {
    path: 'tn-switch-user',
    component: SwitchUserComponent,
    data: {
        pageTitle: 'configuration.title'
    }
};
