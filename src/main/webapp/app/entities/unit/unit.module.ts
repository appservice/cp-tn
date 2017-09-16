import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    UnitService,
    UnitPopupService,
    UnitComponent,
    UnitDetailComponent,
    UnitDialogComponent,
    UnitPopupComponent,
    UnitDeletePopupComponent,
    UnitDeleteDialogComponent,
    unitRoute,
    unitPopupRoute,
    UnitResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...unitRoute,
    ...unitPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UnitComponent,
        UnitDetailComponent,
        UnitDialogComponent,
        UnitDeleteDialogComponent,
        UnitPopupComponent,
        UnitDeletePopupComponent,
    ],
    entryComponents: [
        UnitComponent,
        UnitDialogComponent,
        UnitPopupComponent,
        UnitDeleteDialogComponent,
        UnitDeletePopupComponent,
    ],
    providers: [
        UnitService,
        UnitPopupService,
        UnitResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnUnitModule {}
