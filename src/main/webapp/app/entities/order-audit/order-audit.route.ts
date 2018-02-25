import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {OrderAuditComponent} from './order-audit.component';


// @Injectable()
// export class OrderAuditResolvePagingParams implements Resolve<any> {
//
//     constructor(private paginationUtil: JhiPaginationUtil) {}
//
//     resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
//         const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
//         const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,desc';
//         return {
//             page: this.paginationUtil.parsePage(page),
//             predicate: this.paginationUtil.parsePredicate(sort),
//             ascending: this.paginationUtil.parseAscending(sort)
//       };
//     }
// }

export const orderAuditRoute: Routes = [
    {
        path: 'order-audit/:orderId',
        component: OrderAuditComponent,
        resolve: {
          //  'pagingParams': OrderAuditResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN','ROLE_AUDITOR'],
            pageTitle: 'tnApp.order.inquiries',
        },
        canActivate: [UserRouteAccessService]
    },

];


