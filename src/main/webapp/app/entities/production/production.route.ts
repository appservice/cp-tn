import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {ProductionStanComponent} from './production.component';
import {OrdersInProductionComponent} from './orders-in-production/orders-in-production.component';
import {OrderInProductionDetailComponent} from './order-in-production-detail/order-in-production-detail.component';
import {TechnologyEditComponent} from './technology-edit/technology-edit.component';
import {EmergencyOrderTechnologyEditComponent} from './emergency-order-technology-edit/emergency-order-technology-edit.component';
import {OperationListModalComponent, OperationListPopupComponent} from './operation-list-modal-component/operation-list-modal-component.component';
import {ArchiveProdDialogPopupComponent} from './archive-prod-dialog/archive-prod-dialog.component';
import {FinishedDetailsComponent} from './finished-details-component/finished-details.component';

@Injectable()
export class ItemsInProductionPagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {
    }

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

@Injectable()
export class FinishedItemsPagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'deliveredAt,desc';
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
            'pagingParams': ItemsInProductionPagingParams
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
            'pagingParams': ItemsInProductionPagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'items-finished',
        component: FinishedDetailsComponent,
        resolve: {
            'pagingParams': FinishedItemsPagingParams
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
            'pagingParams': ItemsInProductionPagingParams
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
    {
        path: 'orders-in-production/emergency-order-technology/:id/edit',
        component: EmergencyOrderTechnologyEditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.production.home.title'
        },
        canActivate: [UserRouteAccessService]
    },

];

export const productionPopupRoute: Routes = [
    {
        path: 'production-new',
        component: OperationListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'production/:id/showOperations',
        component: OperationListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'production/:id/moveToArchive',
        component: ArchiveProdDialogPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },

];
