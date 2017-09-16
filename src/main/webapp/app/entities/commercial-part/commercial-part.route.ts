import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CommercialPartComponent } from './commercial-part.component';
import { CommercialPartDetailComponent } from './commercial-part-detail.component';
import { CommercialPartPopupComponent } from './commercial-part-dialog.component';
import { CommercialPartDeletePopupComponent } from './commercial-part-delete-dialog.component';

@Injectable()
export class CommercialPartResolvePagingParams implements Resolve<any> {

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

export const commercialPartRoute: Routes = [
    {
        path: 'commercial-part',
        component: CommercialPartComponent,
        resolve: {
            'pagingParams': CommercialPartResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.commercialPart.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'commercial-part/:id',
        component: CommercialPartDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.commercialPart.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const commercialPartPopupRoute: Routes = [
    {
        path: 'commercial-part-new',
        component: CommercialPartPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.commercialPart.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'commercial-part/:id/edit',
        component: CommercialPartPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.commercialPart.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'commercial-part/:id/delete',
        component: CommercialPartDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.commercialPart.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
