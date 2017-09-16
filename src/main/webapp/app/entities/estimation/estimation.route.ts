import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {EstimationComponent} from './estimation.component';
import {EstimationDetailComponent} from './estimation-detail.component';
import {EstimationDeletePopupComponent} from './estimation-delete-dialog.component';
import {NewEstimationComponent} from './new-estimation/new-estimation.component';

@Injectable()
export class EstimationResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,desc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const estimationRoute: Routes = [
    {
        path: 'estimation',
        component: EstimationComponent,
        resolve: {
            'pagingParams': EstimationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'estimation/:id',
        component: EstimationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimation-new',
        component: NewEstimationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimation/:id/edit',
        component: NewEstimationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },

];

export const estimationPopupRoute: Routes = [/*
    {
        path: 'estimation-new',
        component: EstimationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estimation/:id/edit',
        component: EstimationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'estimation/:id/delete',
        component: EstimationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
