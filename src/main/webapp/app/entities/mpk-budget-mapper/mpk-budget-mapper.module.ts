import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    MpkBudgetMapperService,
    MpkBudgetMapperPopupService,
    MpkBudgetMapperComponent,
    MpkBudgetMapperDetailComponent,
    MpkBudgetMapperDialogComponent,
    MpkBudgetMapperPopupComponent,
    MpkBudgetMapperDeletePopupComponent,
    MpkBudgetMapperDeleteDialogComponent,
    mpkBudgetMapperRoute,
    mpkBudgetMapperPopupRoute,
    MpkBudgetMapperResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...mpkBudgetMapperRoute,
    ...mpkBudgetMapperPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MpkBudgetMapperComponent,
        MpkBudgetMapperDetailComponent,
        MpkBudgetMapperDialogComponent,
        MpkBudgetMapperDeleteDialogComponent,
        MpkBudgetMapperPopupComponent,
        MpkBudgetMapperDeletePopupComponent,
    ],
    entryComponents: [
        MpkBudgetMapperComponent,
        MpkBudgetMapperDialogComponent,
        MpkBudgetMapperPopupComponent,
        MpkBudgetMapperDeleteDialogComponent,
        MpkBudgetMapperDeletePopupComponent,
    ],
    providers: [
        MpkBudgetMapperService,
        MpkBudgetMapperPopupService,
        MpkBudgetMapperResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnMpkBudgetMapperModule {}
