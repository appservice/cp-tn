import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TechnologyCardComponent } from './technology-card.component';
import { TechnologyCardDetailComponent } from './technology-card-detail.component';
import { TechnologyCardPopupComponent } from './technology-card-dialog.component';
import { TechnologyCardDeletePopupComponent } from './technology-card-delete-dialog.component';
import {NewTechnologyCardComponent} from './new-technology-card/new-technology-card.component';
import {TechnologyCardFinderComponent} from './technology-card-finder/technology-card-finder.component';

@Injectable()
export class TechnologyCardResolvePagingParams implements Resolve<any> {

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

export const technologyCardRoute: Routes = [
    {
        path: 'technology-card',
        component: TechnologyCardComponent,
        resolve: {
            'pagingParams': TechnologyCardResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    ,{
        path: 'technology-card/:id',
        component: NewTechnologyCardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title',
            readOnly: true,
        },
        canActivate: [UserRouteAccessService]
    }
    ,{
        path: 'technology-card/:id/edit',
        component: NewTechnologyCardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'technology-card-new',
        component: NewTechnologyCardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title',
            readOnly: false,

        },
        canActivate: [UserRouteAccessService],
    }
];

export const technologyCardPopupRoute: Routes = [
   /* {
        path: 'technology-card-new',
        component: TechnologyCardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'technology-card-finder',
        component: TechnologyCardFinderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    /*{
        path: 'technology-card/:id/edit',
        component: TechnologyCardPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'technology-card/:id/delete',
        component: TechnologyCardDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.technologyCard.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
