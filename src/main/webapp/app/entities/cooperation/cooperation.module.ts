import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    CooperationService,
    CooperationPopupService,
    CooperationComponent,
    CooperationDetailComponent,
    CooperationDialogComponent,
    CooperationPopupComponent,
    CooperationDeletePopupComponent,
    CooperationDeleteDialogComponent,
    cooperationRoute,
    cooperationPopupRoute,
    CooperationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cooperationRoute,
    ...cooperationPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CooperationComponent,
        CooperationDetailComponent,
        CooperationDialogComponent,
        CooperationDeleteDialogComponent,
        CooperationPopupComponent,
        CooperationDeletePopupComponent,
    ],
    entryComponents: [
        CooperationComponent,
        CooperationDialogComponent,
        CooperationPopupComponent,
        CooperationDeleteDialogComponent,
        CooperationDeletePopupComponent,
    ],
    providers: [
        CooperationService,
        CooperationPopupService,
        CooperationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnCooperationModule {}
