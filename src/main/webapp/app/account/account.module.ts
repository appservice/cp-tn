import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../shared';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';


import {
    Register,
    ActivateService,
    PasswordService,
    PasswordResetInitService,
    PasswordResetFinishService,
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState
} from './';
import {SwitchUserComponent} from './switch-user/switch-user.component';

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forChild(accountState)

    ],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent,
        SwitchUserComponent
    ],
    providers: [
        Register,
        ActivateService,
        PasswordService,
        PasswordResetInitService,
        PasswordResetFinishService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnAccountModule {}
