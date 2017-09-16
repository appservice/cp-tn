import './vendor.ts';

import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {Ng2Webstorage} from 'ng2-webstorage';

import { TnSharedModule, UserRouteAccessService } from './shared';
import { TnHomeModule } from './home/home.module';
import { TnAdminModule } from './admin/admin.module';
import { TnAccountModule } from './account/account.module';
import { TnEntityModule } from './entities/entity.module';

import {customHttpProvider} from './blocks/interceptor/http.provider';
import {PaginationConfig} from './blocks/config/uib-pagination.config';
import {TnComponentsModule} from './tn-components/tn-components.module';


// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import {CurrencyMaskModule} from 'ng2-currency-mask';


@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        TnSharedModule,
        TnHomeModule,
        TnAdminModule,
        TnAccountModule,
        TnEntityModule,
        TnComponentsModule,


        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
        { provide: LOCALE_ID, useValue: "pl-PL" }
    ],
    bootstrap: [ JhiMainComponent ]
})
export class TnAppModule {}
