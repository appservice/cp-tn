import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CooperationComponent } from './cooperation.component';
import { CooperationDetailComponent } from './cooperation-detail.component';
import { CooperationPopupComponent } from './cooperation-dialog.component';
import { CooperationDeletePopupComponent } from './cooperation-delete-dialog.component';

@Injectable()
export class CooperationResolvePagingParams implements Resolve<any> {

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

export const cooperationRoute: Routes = [
    {
        path: 'cooperation',
        component: CooperationComponent,
        resolve: {
            'pagingParams': CooperationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.cooperation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cooperation/:id',
        component: CooperationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.cooperation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cooperationPopupRoute: Routes = [
    {
        path: 'cooperation-new',
        component: CooperationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.cooperation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cooperation/:id/edit',
        component: CooperationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.cooperation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cooperation/:id/delete',
        component: CooperationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.cooperation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
