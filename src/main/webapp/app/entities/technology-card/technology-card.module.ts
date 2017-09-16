import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TnSharedModule } from '../../shared';
import {
    TechnologyCardService,
    TechnologyCardPopupService,
    TechnologyCardComponent,
    TechnologyCardDetailComponent,
    TechnologyCardDialogComponent,
    TechnologyCardPopupComponent,
    TechnologyCardDeletePopupComponent,
    TechnologyCardDeleteDialogComponent,
    technologyCardRoute,
    technologyCardPopupRoute,
    TechnologyCardResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...technologyCardRoute,
    ...technologyCardPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TechnologyCardComponent,
        TechnologyCardDetailComponent,
        TechnologyCardDialogComponent,
        TechnologyCardDeleteDialogComponent,
        TechnologyCardPopupComponent,
        TechnologyCardDeletePopupComponent,
    ],
    entryComponents: [
        TechnologyCardComponent,
        TechnologyCardDialogComponent,
        TechnologyCardPopupComponent,
        TechnologyCardDeleteDialogComponent,
        TechnologyCardDeletePopupComponent,
    ],
    providers: [
        TechnologyCardService,
        TechnologyCardPopupService,
        TechnologyCardResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnTechnologyCardModule {}
