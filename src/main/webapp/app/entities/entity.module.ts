import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TnUnitModule } from './unit/unit.module';
import { TnClientModule } from './client/client.module';
import { TnCommercialPartModule } from './commercial-part/commercial-part.module';
import { TnOrderModule } from './order/order.module';
import { TnEstimationModule } from './estimation/estimation.module';
import { TnOperationModule } from './operation/operation.module';
import { TnMachineModule } from './machine/machine.module';
import { TnDrawingModule } from './drawing/drawing.module';
import {TnProductionModule} from './production/production.module'
import { TnTechnologyCardModule } from './technology-card/technology-card.module';
import { TnOperatorModule } from './operator/operator.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TnUnitModule,
        TnClientModule,
        TnCommercialPartModule,
        TnOrderModule,
        TnEstimationModule,
        TnOperationModule,
        TnMachineModule,
        TnDrawingModule,
        TnProductionModule,
        TnTechnologyCardModule,
        TnOperatorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnEntityModule {}
