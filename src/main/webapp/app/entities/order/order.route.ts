import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrderComponent } from './order.component';
import { OrderPopupComponent } from './order-dialog.component';
import { OrderDeletePopupComponent } from './order-delete-dialog.component';

import { Principal } from '../../shared';
import {NewOrderComponent} from './new-order/new-order.component';
import {OrderToEstimationComponent} from "./orders-to-estimation/order-to-estimation.component";
import {OrderInEstimationComponent} from './orders-in-estimation/order-in-estimation.component';
import {EstimatedOrderComponent} from './estimated-order/estimated-order.component';
import {OrderDetailComponent} from './order-detail/order-detail.component';
import {NewPurchaseOrderComponent} from './new-purchase-order/new-purchase-order.component';
import {OrderType} from './order.model';
import {PurchaseOrderComponent} from './purchase-order/purchase-order.component';
import {ArchiveOrdersComponent} from './archive-orders/archive-orders.component';


@Injectable()
export class OrderResolvePagingParams implements Resolve<any> {

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

export const orderRoute: Routes = [
    {
        path: 'order',
        component: OrderComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries',
            orderType: OrderType.ESTIMATION
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order',
        component: PurchaseOrderComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.purchaseOrders',
            orderType:OrderType.PRODUCTION
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order/:id',
        component: OrderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zapyptanie ofertowe'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order/:id/edit',
        component: NewOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tn.order.purchaseOrder'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new-order',
        component: NewOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new-purchase-order',
        component: NewPurchaseOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/:id/edit',
        component: NewPurchaseOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-to-estimation',
        component: OrderToEstimationComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zlecenia czekające na rozpoczęcie wyceny'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'orders-in-estimation',
        component: OrderInEstimationComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zlecenia aktualnie wyceniane'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimated-order/:id',
        component: EstimatedOrderComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zlecenia aktualnie wyceniane'
        },
        canActivate: [UserRouteAccessService]
    },    {
        path: 'archived-orders',
        component: ArchiveOrdersComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Zlecenia archivalnee'
        },
        canActivate: [UserRouteAccessService]
    },
];

export const orderPopupRoute: Routes = [
//     {
//     //     path: 'order-new',
//     //     component: OrderPopupComponent,
//     //     data: {
//     //         authorities: ['ROLE_USER'],
//     //         pageTitle: 'tnApp.order.home.title'
//     //     },
//     //     canActivate: [UserRouteAccessService],
//     //       outlet: 'popup'
    // },
    // {
    //     path: 'order/:id/edit',
    //     component: OrderPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'tnApp.order.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    {
        path: 'order/:id/delete',
        component: OrderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
