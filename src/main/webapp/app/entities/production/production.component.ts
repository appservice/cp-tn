import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiAlertService, JhiEventManager, JhiPaginationUtil, JhiParseLinks} from 'ng-jhipster';
import {URLSearchParams} from '@angular/http';

import {Estimation} from './estimation.model';
import {ITEMS_PER_PAGE, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {ProductionService} from './production.service';
import {ProductionItem} from './production-item.model';
import {EstimationFilter} from "../order/estimation-filter.model";


@Component({
    selector: 'jhi-production',
    templateUrl: './production.component.html'
})
export class ProductionStanComponent implements OnInit, OnDestroy {

    currentAccount: any;
    productionItems: ProductionItem[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    estimationFilter: EstimationFilter;
    waitForResponse: boolean = false;

    expiredRealization: boolean = false;


    constructor(private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                // private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private eventManager: JhiEventManager,
                private paginationUtil: JhiPaginationUtil,
                private paginationConfig: PaginationConfig,
                private productionService: ProductionService,) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.estimationFilter = new EstimationFilter();
        this.estimationFilter.itemName = activatedRoute.snapshot.params['itemName'] ? activatedRoute.snapshot.params['itemName'] : '';
        this.estimationFilter.itemNumber = activatedRoute.snapshot.params['itemNumber'] ? activatedRoute.snapshot.params['itemNumber'] : '';
        this.estimationFilter.clientName = activatedRoute.snapshot.params['clientName'] ? activatedRoute.snapshot.params['clientName'] : '';
        this.estimationFilter.orderNumber = activatedRoute.snapshot.params['orderNumber'] ? activatedRoute.snapshot.params['orderNumber'] : '';
        this.estimationFilter.sapNumber = activatedRoute.snapshot.params['sapNumber'] ? activatedRoute.snapshot.params['sapNumber'] : '';
        this.estimationFilter.realizationDateExpired = activatedRoute.snapshot.params['realizationDateExpired'] ? activatedRoute.snapshot.params['realizationDateExpired'] : 'false';


    }

    loadAll() {

        let urlSearchParams = this.createSearchParams();

        this.productionService.getItemsActualInProduction({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        }, urlSearchParams).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/production'], {
            queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }


    ngOnInit() {
        this.loadAll();
        // this.principal.identity().then((account) => {
        //     this.currentAccount = account;
        // });
        this.registerChangeInEstimations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Estimation) {
        return item.id;
    }

    registerChangeInEstimations() {
        this.eventSubscriber = this.eventManager.subscribe('itemsInProductionListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.productionItems = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }


    exportToExcel() {
        this.waitForResponse = true;
        let urlSearchParams = this.createSearchParams();

        this.productionService.getProductionAsExcel(urlSearchParams).subscribe((res: any) => {
                this.waitForResponse = false;
            }
        );
    }


    createSearchParams(): URLSearchParams {
        let urlSearchParams = new URLSearchParams();
        if (this.estimationFilter.itemNumber !== null && this.estimationFilter.itemNumber !== '')
            urlSearchParams.append('itemNumber.contains', this.estimationFilter.itemNumber);

        if (this.estimationFilter.itemName !== null && this.estimationFilter.itemName !== '')
            urlSearchParams.append('itemName.contains', this.estimationFilter.itemName);

        if (this.estimationFilter.orderNumber !== null && this.estimationFilter.orderNumber !== '')
            urlSearchParams.append('orderInternalNumber.contains', this.estimationFilter.orderNumber);

        if (this.estimationFilter.clientName !== null && this.estimationFilter.clientName !== '')
            urlSearchParams.append('clientName.contains', this.estimationFilter.clientName);

        if (this.estimationFilter.sapNumber !== null && this.estimationFilter.sapNumber !== '')
            urlSearchParams.append('sapNumber.contains', this.estimationFilter.sapNumber);

        if (this.estimationFilter.realizationDateExpired !== null)
            urlSearchParams.append('realizationDateExpired.specified', this.estimationFilter.realizationDateExpired);
        return urlSearchParams;
    }

    loadFilter(filter: EstimationFilter) {
        if (filter) {
            this.page = 0;
            this.router.navigate(['/production', {
                itemNumber: this.estimationFilter.itemNumber,
                itemName: this.estimationFilter.itemName,
                orderNumber: this.estimationFilter.orderNumber,
                clientName: this.estimationFilter.clientName,
                sapNumber: this.estimationFilter.sapNumber,
                realizationDateExpired: this.estimationFilter.realizationDateExpired,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }]);
        }
    }

    search() {
        this.loadFilter(this.estimationFilter);
        this.loadAll();
    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.loadFilter(this.estimationFilter);
            this.loadAll();
        }
    }

    clearFilterAndLoadAll(): void {
        this.estimationFilter.itemName = '';
        this.estimationFilter.clientName = '';
        this.estimationFilter.itemNumber = '';
        this.estimationFilter.orderNumber = '';
        this.estimationFilter.sapNumber = '';
        this.estimationFilter.realizationDateExpired = 'false';
        this.expiredRealization = false;
        this.page = 0;
        this.router.navigate(['/production', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    returnToTechnologyVerification(orderId: number) {
        this.productionService.returnToTechnologyVerification(orderId).subscribe((resp: ResponseWrapper) => {
            this.loadAll();
        }, (error: any) => {
            this.onError(this.error)
        });
    }
}
