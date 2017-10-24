import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MachineComponent } from './machine.component';
import { MachineDetailComponent } from './machine-detail.component';
import { MachinePopupComponent } from './machine-dialog.component';
import { MachineDeletePopupComponent } from './machine-delete-dialog.component';

@Injectable()
export class MachineResolvePagingParams implements Resolve<any> {

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

export const machineRoute: Routes = [
    {
        path: 'machine',
        component: MachineComponent,
        resolve: {
            'pagingParams': MachineResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.machine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'machine/:id',
        component: MachineDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.machine.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const machinePopupRoute: Routes = [
    {
        path: 'machine-new',
        component: MachinePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.machine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'machine/:id/edit',
        component: MachinePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.machine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'machine/:id/delete',
        component: MachineDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            pageTitle: 'tnApp.machine.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
