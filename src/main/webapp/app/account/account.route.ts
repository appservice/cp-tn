import { Routes } from '@angular/router';

import {
    activateRoute,
    passwordRoute,
    passwordResetFinishRoute,
    passwordResetInitRoute,
    registerRoute,
    settingsRoute
} from './';
import {swichUserRoute} from './switch-user/switch-user.route';

const ACCOUNT_ROUTES = [
    activateRoute,
    passwordRoute,
    passwordResetFinishRoute,
    passwordResetInitRoute,
    registerRoute,
    settingsRoute,
    swichUserRoute,
];

export const accountState: Routes = [{
    path: '',
    children: ACCOUNT_ROUTES
}];
