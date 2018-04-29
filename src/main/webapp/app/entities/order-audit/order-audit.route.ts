import {Routes} from '@angular/router';
import {UserRouteAccessService} from '../../shared';
import {OrderAuditComponent} from './order-audit.component';

export const orderAuditRoute: Routes = [
    {
        path: 'order-audit/:orderId',
        component: OrderAuditComponent,
        resolve: {
            //  'pagingParams': OrderAuditResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_AUDITOR'],
            pageTitle: 'tnApp.order.inquiries',
        },
        canActivate: [UserRouteAccessService]
    },
];


