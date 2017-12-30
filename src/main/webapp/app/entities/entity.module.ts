import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TnOperatorModule } from './operator/operator.module';
import { TnCooperationModule } from './cooperation/cooperation.module';
import { TnMpkBudgetMapperModule } from './mpk-budget-mapper/mpk-budget-mapper.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TnOperatorModule,
        TnCooperationModule,
        TnMpkBudgetMapperModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TnEntityModule {}
