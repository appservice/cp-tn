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
import {NewTechnologyCardComponent} from './new-technology-card/new-technology-card.component';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {TechnologyCardFinderComponent} from './technology-card-finder/technology-card-finder.component';
import {CurrencyMaskModule} from 'ng2-currency-mask';


const ENTITY_STATES = [
    ...technologyCardRoute,
    ...technologyCardPopupRoute,
];

@NgModule({
    imports: [
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        TnComponentsModule,
        CurrencyMaskModule,


    ],
    declarations: [
        TechnologyCardComponent,
        TechnologyCardDetailComponent,
        TechnologyCardDialogComponent,
        TechnologyCardDeleteDialogComponent,
        TechnologyCardPopupComponent,
        TechnologyCardDeletePopupComponent,
        NewTechnologyCardComponent,
        TechnologyCardFinderComponent,
    ],
    entryComponents: [
        TechnologyCardComponent,
        TechnologyCardDialogComponent,
        TechnologyCardPopupComponent,
        TechnologyCardDeleteDialogComponent,
        TechnologyCardDeletePopupComponent,
        NewTechnologyCardComponent,
        TechnologyCardDialogComponent,

    ],
    providers: [
        TechnologyCardService,
        TechnologyCardPopupService,
        TechnologyCardResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnTechnologyCardModule {}
