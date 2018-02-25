import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OrderAuditComponent} from './order-audit.component';
import {TnComponentsModule} from '../../tn-components/tn-components.module';
import {TnSharedModule} from '../../shared';
import {RouterModule} from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {orderAuditRoute} from './order-audit.route';
import {OrderAuditService} from './order-audit.service';

const ENTITY_STATES = [
    ...orderAuditRoute,

];

@NgModule({

    imports: [
        CommonModule,
        TnSharedModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true}),
        BrowserAnimationsModule,
        TnComponentsModule,
    ],
    declarations: [OrderAuditComponent],
    entryComponents: [OrderAuditComponent],
    providers: [OrderAuditService],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class OrderAuditModule {
}
