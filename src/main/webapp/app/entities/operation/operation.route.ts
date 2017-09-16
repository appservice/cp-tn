import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OperationComponent } from './operation.component';
import { OperationDetailComponent } from './operation-detail.component';
import { OperationPopupComponent } from './operation-dialog.component';
import { OperationDeletePopupComponent } from './operation-delete-dialog.component';

@Injectable()
export class OperationResolvePagingParams implements Resolve<any> {

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

export const operationRoute: Routes = [
    {
        path: 'operation',
        component: OperationComponent,
        resolve: {
            'pagingParams': OperationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.operation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'operation/:id',
        component: OperationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.operation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const operationPopupRoute: Routes = [
    {
        path: 'operation-new',
        component: OperationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.operation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'operation/:id/edit',
        component: OperationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.operation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'operation/:id/delete',
        component: OperationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.operation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
