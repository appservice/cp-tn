import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { OperatorComponent } from './operator.component';
import { OperatorDetailComponent } from './operator-detail.component';
import { OperatorPopupComponent } from './operator-dialog.component';
import { OperatorDeletePopupComponent } from './operator-delete-dialog.component';

@Injectable()
export class OperatorResolvePagingParams implements Resolve<any> {

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

export const operatorRoute: Routes = [
    {
        path: 'operator',
        component: OperatorComponent,
        resolve: {
            'pagingParams': OperatorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.operator.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'operator/:id',
        component: OperatorDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.operator.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const operatorPopupRoute: Routes = [
    {
        path: 'operator-new',
        component: OperatorPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.operator.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'operator/:id/edit',
        component: OperatorPopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.operator.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'operator/:id/delete',
        component: OperatorDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.operator.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
