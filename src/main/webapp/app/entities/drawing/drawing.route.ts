import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DrawingComponent } from './drawing.component';
import { DrawingDetailComponent } from './drawing-detail.component';
import { DrawingPopupComponent } from './drawing-dialog.component';
import { DrawingDeletePopupComponent } from './drawing-delete-dialog.component';
import {DrawingEditComponent} from './drawing-edit/drawing-edit.component';

@Injectable()
export class DrawingResolvePagingParams implements Resolve<any> {

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

export const drawingRoute: Routes = [
    {
        path: 'drawing',
        component: DrawingComponent,
        resolve: {
            'pagingParams': DrawingResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.drawing.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'drawing/:id',
        component: DrawingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.drawing.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'drawing/:id/edit',
        component: DrawingEditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.drawing.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const drawingPopupRoute: Routes = [
    {
        path: 'drawing-new',
        component: DrawingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.drawing.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    // {
    //     path: 'drawing/:id/edit',
    //     component: DrawingPopupComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'tnApp.drawing.home.title'
    //     },
    //     canActivate: [UserRouteAccessService],
    //     outlet: 'popup'
    // },
    {
        path: 'drawing/:id/delete',
        component: DrawingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.drawing.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
