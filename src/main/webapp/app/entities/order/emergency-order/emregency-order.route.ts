import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { JhiPaginationUtil } from 'ng-jhipster';
import {NewEmergencyOrderComponent} from './new-order/new-emergency-order.component';
import {OrderType} from '../order.model';
import {UserRouteAccessService} from '../../../shared/auth/user-route-access-service';
import {EmergencyOrderComponent} from './emergency-order.component';
import {EmergencyOrderDetailComponent} from './emergency-order-detail/emergency-order-detail.component';
import {EmergencyOrderItemFinderComponent} from './emergency-order-item-finder/emergency_order-item-finder.component';
import {OrderResolvePagingParams} from '../order.route';




@Injectable()
export class EmergencyOrderResolvePagingParams implements Resolve<any> {

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

export const emergencyOderRoute: Routes = [
    {
        path: 'new-emergency-order',
        component: NewEmergencyOrderComponent,
        resolve: {
            'pagingParams': EmergencyOrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries',
            orderType: OrderType.EMERGENCY
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'emergency-order',
        component: EmergencyOrderComponent,
        resolve: {
            'pagingParams': EmergencyOrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries',
            orderType: OrderType.EMERGENCY
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'emergency-order/:id/edit',
        component: NewEmergencyOrderComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER'],
            pageTitle: 'tn.order.purchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'emergency-order/:id',
        component: EmergencyOrderDetailComponent,
        data: {
            authorities: ['ROLE_ORDER_INTRODUCER','ROLE_MANAGER'],
            pageTitle: 'tn.order.purchaseOrders'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'emergency-order-item-finder',
        component: EmergencyOrderItemFinderComponent,
        resolve: {
            'pagingParams': EmergencyOrderResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.order.inquiries'
        },
        canActivate: [UserRouteAccessService]
    },

];

export const orderPopupRoute: Routes = [
    //
    // {
    //     path: 'order/:id/delete',
    //     component: OrderDeletePopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'tnApp.order.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // }
];
