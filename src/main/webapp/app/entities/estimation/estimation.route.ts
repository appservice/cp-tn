import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot, Routes} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {JhiPaginationUtil} from 'ng-jhipster';

import {EstimationComponent} from './estimation.component';
import {EstimationDetailComponent} from './estimation-detail.component';
import {EstimationDeletePopupComponent} from './estimation-delete-dialog.component';
import {NewEstimationComponent} from './new-estimation/new-estimation.component';

@Injectable()
export class EstimationResolvePagingParams implements Resolve<any> {

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

import {CanDeactivate} from '@angular/router';

@Injectable()
export class ConfirmDeactivateGuard implements CanDeactivate<NewEstimationComponent> {
    constructor(private router: Router) {

    }

    canDeactivate(target: NewEstimationComponent) {
        if (target.hasChanges()) {
            let canChangeState = window.confirm('Czy chcesz wyjść z tej strony nie zachowując wprowadzonych zmian?');
            if (canChangeState == false && target.backButtonClicked) {
                const destinationLink = window.location.href;
                setTimeout(() => {
                    window.history.replaceState({}, '', destinationLink);
                    window.history.pushState({}, '', this.router.url);
                });
                target.backButtonClicked=false;
                return false;
                // this.router.navigate([this.router.url]);
            }
            return canChangeState;
        }
        return true;
    }
}

export const estimationRoute: Routes = [
    {
        path: 'estimation',
        component: EstimationComponent,
        resolve: {
            'pagingParams': EstimationResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'estimation/:id',
        component: EstimationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimation-new',
        component: NewEstimationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [ConfirmDeactivateGuard]
    },
    {
        path: 'estimation/:id/edit',
        component: NewEstimationComponent,
        data: {
            authorities: ['ROLE_TECHNOLOGIST'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        canDeactivate: [ConfirmDeactivateGuard]

    },

];

export const estimationPopupRoute: Routes = [/*
    {
        path: 'estimation-new',
        component: EstimationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'estimation/:id/edit',
        component: EstimationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },*/
    {
        path: 'estimation/:id/delete',
        component: EstimationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tnApp.estimation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];



