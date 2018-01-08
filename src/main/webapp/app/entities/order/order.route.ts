import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OrderComponent } from './inquiry/order.component';
import { OrderPopupComponent } from './order-dialog.component';
import { OrderDeletePopupComponent } from './order-delete-dialog.component';

import { Principal } from '../../shared';
import {NewOrderComponent} from './inquiry/new-order/new-order.component';
import {OrderToEstimationComponent} from "./inquiry/orders-to-estimation/order-to-estimation.component";
import {OrderInEstimationComponent} from './inquiry/orders-in-estimation/order-in-estimation.component';
import {EstimatedOrderComponent} from './inquiry/estimated-order/estimated-order.component';
import {OrderDetailComponent} from './inquiry/order-detail/order-detail.component';
import {NewPurchaseOrderComponent} from './purchase-order/new-purchase-order/new-purchase-order.component';
import {OrderType} from './order.model';
import {PurchaseOrderComponent} from './purchase-order/purchase-order.component';
import {ArchiveOrdersComponent} from './inquiry/archive-orders/archive-orders.component';
import {PurchaseOrderDetailComponent} from './purchase-order/purchase-order-detail/purchase-order-detail.component';
import {InquiryItemFinderComponent} from './inquiry/inquiry-item-finder/inquiry-item-finder.component';
import {PurchaseOrderItemFinderComponent} from './purchase-order/purchase-order-item-finder/purchase-order-item-finder.component';


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
            pageTitle: 'tnApp.order.inquiries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/:id',
        component: PurchaseOrderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.purchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'order/:id/edit',
        component: NewOrderComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER','ROLE_ADMIN'],
            pageTitle: 'tnApp.order.purchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new-order',
        component: NewOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new-purchase-order',
        component: NewPurchaseOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER'],
            pageTitle: 'tnApp.order.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order/:id/edit',
        component: NewPurchaseOrderComponent, // OrderPopupComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER'],
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
            pageTitle: 'tnApp.order.inquiriesForEstimation'
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
            pageTitle: 'tnApp.order.inquiriesEstimated'
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
            authorities: ['ROLE_TECHNOLOGIST','ROLE_MANAGER'],
            pageTitle: 'tnApp.order.inquiries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'archived-orders',
        component: ArchiveOrdersComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.archivalInquiries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'inquiry-item-finder',
        component: InquiryItemFinderComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'purchase-order-item-finder',
        component: PurchaseOrderItemFinderComponent,
        resolve: {
            'pagingParams': OrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries'
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
