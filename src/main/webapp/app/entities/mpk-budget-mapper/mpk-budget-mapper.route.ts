import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MpkBudgetMapperComponent } from './mpk-budget-mapper.component';
import { MpkBudgetMapperDetailComponent } from './mpk-budget-mapper-detail.component';
import { MpkBudgetMapperPopupComponent } from './mpk-budget-mapper-dialog.component';
import { MpkBudgetMapperDeletePopupComponent } from './mpk-budget-mapper-delete-dialog.component';

@Injectable()
export class MpkBudgetMapperResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const mpkBudgetMapperRoute: Routes = [
    {
        path: 'mpk-budget-mapper',
        component: MpkBudgetMapperComponent,
        resolve: {
            'pagingParams': MpkBudgetMapperResolvePagingParams
        },
        data: {
            authorities: ['ROLE_TECHNOLOGIS','ROLE_SAP_INTRODUCER'],
            pageTitle: 'tnApp.mpkBudgetMapper.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'mpk-budget-mapper/:id',
        component: MpkBudgetMapperDetailComponent,
        data: {
            authorities: ['ROLE_TECHNOLOGIS','ROLE_SAP_INTRODUCER'],
            pageTitle: 'tnApp.mpkBudgetMapper.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const mpkBudgetMapperPopupRoute: Routes = [
    {
        path: 'mpk-budget-mapper-new',
        component: MpkBudgetMapperPopupComponent,
        data: {
            authorities: ['ROLE_TECHNOLOGIS','ROLE_SAP_INTRODUCER'],
            pageTitle: 'tnApp.mpkBudgetMapper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mpk-budget-mapper/:id/edit',
        component: MpkBudgetMapperPopupComponent,
        data: {
            authorities: ['ROLE_TECHNOLOGIS','ROLE_SAP_INTRODUCER'],
            pageTitle: 'tnApp.mpkBudgetMapper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'mpk-budget-mapper/:id/delete',
        component: MpkBudgetMapperDeletePopupComponent,
        data: {
            authorities: ['ROLE_TECHNOLOGIS','ROLE_SAP_INTRODUCER'],
            pageTitle: 'tnApp.mpkBudgetMapper.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
