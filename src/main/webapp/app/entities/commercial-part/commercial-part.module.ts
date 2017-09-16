import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    CommercialPartService,
    CommercialPartPopupService,
    CommercialPartComponent,
    CommercialPartDetailComponent,
    CommercialPartDialogComponent,
    CommercialPartPopupComponent,
    CommercialPartDeletePopupComponent,
    CommercialPartDeleteDialogComponent,
    commercialPartRoute,
    commercialPartPopupRoute,
    CommercialPartResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...commercialPartRoute,
    ...commercialPartPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CommercialPartComponent,
        CommercialPartDetailComponent,
        CommercialPartDialogComponent,
        CommercialPartDeleteDialogComponent,
        CommercialPartPopupComponent,
        CommercialPartDeletePopupComponent,
    ],
    entryComponents: [
        CommercialPartComponent,
        CommercialPartDialogComponent,
        CommercialPartPopupComponent,
        CommercialPartDeleteDialogComponent,
        CommercialPartDeletePopupComponent,
    ],
    providers: [
        CommercialPartService,
        CommercialPartPopupService,
        CommercialPartResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnCommercialPartModule {}
