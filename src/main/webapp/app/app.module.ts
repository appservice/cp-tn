import './vendor.ts';

import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {TnSharedModule, UserRouteAccessService} from './shared';
import {TnAppRoutingModule} from './app-routing.module';
import {TnHomeModule} from './home';
import {TnAdminModule} from './admin/admin.module';
import {TnAccountModule} from './account/account.module';
import {TnEntityModule} from './entities/entity.module';

import {customHttpProvider} from './blocks/interceptor/http.provider';
import {PaginationConfig} from './blocks/config/uib-pagination.config';
import {TnComponentsModule} from './tn-components/tn-components.module';
import {NgxMyDatePickerModule} from 'ngx-mydatepicker';
import {Ng2Webstorage} from 'ngx-webstorage';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent, ProfileService} from './layouts';


@NgModule({
    imports: [
        BrowserModule,
        TnAppRoutingModule,
        Ng2Webstorage.forRoot({prefix: 'jhi', separator: '-'}),
        TnSharedModule,
        TnHomeModule,
        TnAdminModule,
        TnAccountModule,
        TnEntityModule,
        TnComponentsModule,
        NgxMyDatePickerModule.forRoot(),


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
        {provide: LOCALE_ID, useValue: "pl-PL"}
    ],
    bootstrap: [JhiMainComponent]
})
export class TnAppModule {
}
