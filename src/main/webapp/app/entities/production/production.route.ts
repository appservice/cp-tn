import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {ProductionStanComponent} from './production.component';
import {OrdersInProductionComponent} from './orders-in-production/orders-in-production.component';
import {OrderInProductionDetailComponent} from './order-in-production-detail/order-in-production-detail.component';
import {TechnologyEditComponent} from './technology-edit/technology-edit.component';

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

export const productionRoute: Routes = [
    {
        path: 'production',
        component: ProductionStanComponent,
        resolve: {
            'pagingParams': EstimationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-in-production',
        component: OrdersInProductionComponent,
        resolve: {
            'pagingParams': EstimationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-in-production/:id/detail',
        component: OrderInProductionDetailComponent,
        resolve: {
            'pagingParams': EstimationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-in-production/technology/:id/edit',
        component: TechnologyEditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
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
    // {
    //     path: 'estimation/:id/delete',
    //     component: EstimationDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'tnApp.estimation.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
