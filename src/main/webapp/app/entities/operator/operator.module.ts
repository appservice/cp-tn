import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    OperatorService,
    OperatorPopupService,
    OperatorComponent,
    OperatorDetailComponent,
    OperatorDialogComponent,
    OperatorPopupComponent,
    OperatorDeletePopupComponent,
    OperatorDeleteDialogComponent,
    operatorRoute,
    operatorPopupRoute,
    OperatorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...operatorRoute,
    ...operatorPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        OperatorComponent,
        OperatorDetailComponent,
        OperatorDialogComponent,
        OperatorDeleteDialogComponent,
        OperatorPopupComponent,
        OperatorDeletePopupComponent,
    ],
    entryComponents: [
        OperatorComponent,
        OperatorDialogComponent,
        OperatorPopupComponent,
        OperatorDeleteDialogComponent,
        OperatorDeletePopupComponent,
    ],
    providers: [
        OperatorService,
        OperatorPopupService,
        OperatorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnOperatorModule {}
