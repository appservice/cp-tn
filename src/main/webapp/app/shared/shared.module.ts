import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {DatePipe} from '@angular/common';

import {
    AccountService,
    AuthServerProvider,
    CSRFService,
    HasAnyAuthorityDirective,
    JhiLoginModalComponent,
    LoginModalService,
    LoginService,
    Principal,
    StateStorageService,
    TnSharedCommonModule,
    TnSharedLibsModule,
    UserService,
} from './';
import {Globals} from './globals.service';

@NgModule({
    imports: [
        TnSharedLibsModule,
        TnSharedCommonModule,

    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        DatePipe,
        Globals
    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        TnSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        DatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class TnSharedModule {}
