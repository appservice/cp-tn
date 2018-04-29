import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Order } from '../../order.model';
import { OrderService } from '../../order.service';
import {Globals, ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../../../shared';
import { PaginationConfig } from '../../../../blocks/config/uib-pagination.config';
import {OrderFilter} from '../../order-filter.model';
import {URLSearchParams} from '@angular/http';
import {EstimationService} from '../../../estimation';


@Component({
    selector: 'archived-orders',
    templateUrl: './archive-orders.component.html'
})
export class ArchiveOrdersComponent implements OnInit, OnDestroy {

currentAccount: any;
    orders: Order[];
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
    orderFilter: OrderFilter;
    downloadingExcelFile:boolean=false;


    constructor(
        private orderService: OrderService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private estimationService: EstimationService,
        public globals: Globals,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.orderFilter = new OrderFilter();
        this.orderFilter.internalNumber = activatedRoute.snapshot.params['internalNumber'] ? activatedRoute.snapshot.params['internalNumber'] : '';
        this.orderFilter.referenceNumber = activatedRoute.snapshot.params['referenceNumber'] ? activatedRoute.snapshot.params['referenceNumber'] : '';
        this.orderFilter.clientName = activatedRoute.snapshot.params['clientName'] ? activatedRoute.snapshot.params['clientName'] : '';
        this.orderFilter.orderStatus = activatedRoute.snapshot.params['orderStatus'] ? activatedRoute.snapshot.params['orderStatus'] : '';
        this.orderFilter.title = activatedRoute.snapshot.params['title'] ? activatedRoute.snapshot.params['title'] : '';


    }

    loadAll() {
        if (this.currentSearch) {
            this.orderService.archivedOrders({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }

        let urlSearchParams =this.createSearchParams();

        this.orderService.archivedOrders({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()},urlSearchParams).subscribe(
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
        this.router.navigate(['/archived-orders'], {queryParams:
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
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Order) {
        return item.id;
    }
    registerChangeInOrders() {
        this.eventSubscriber = this.eventManager.subscribe('orderListModification', (response) => this.loadAll());
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
        // this.page = pagingParams.page;
        this.orders = data;
    }
    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
    claimToEstimator(order: Order){
        this.orderService.claimToEstimator(order.id).subscribe(
            ()=>{
                this.eventManager.broadcast({name: 'orderListModification', content: 'OK'});

            }

        );
    }

    createSearchParams(): URLSearchParams {
        let urlSearchParams = new URLSearchParams();
        if (this.orderFilter.internalNumber !== null && this.orderFilter.internalNumber !== '')
            urlSearchParams.append('internalNumber.contains', this.orderFilter.internalNumber);

        if (this.orderFilter.referenceNumber !== null && this.orderFilter.referenceNumber !== '')
            urlSearchParams.append('referenceNumber.contains', this.orderFilter.referenceNumber);

        if (this.orderFilter.clientName !== null && this.orderFilter.clientName !== '')
            urlSearchParams.append('clientName.contains', this.orderFilter.clientName);

        if (this.orderFilter.orderStatus !== null && this.orderFilter.orderStatus !== '')
            urlSearchParams.append('orderStatus.equals', this.orderFilter.orderStatus);

        if (this.orderFilter.validFrom !== null )//&& this.orderFilter.validFrom !== ''
            urlSearchParams.append('createdAt.greaterOrEqualThan', this.orderFilter.getValidFromString());

        if (this.orderFilter.title !== null && this.orderFilter.title !== '')
            urlSearchParams.append('title.contains', this.orderFilter.title);

        if (this.orderFilter.validTo !== null )//&& this.orderFilter.validTo !== ''
            urlSearchParams.append('createdAt.lessOrEqualThan', this.orderFilter.getValidToString());
        return urlSearchParams;
    }


    loadFilter(filter: OrderFilter) {
        if (filter) {


            this.page = 0;
            // this.currentSearch = query;
            this.router.navigate(['/archived-orders', {
                internalNumber: this.orderFilter.internalNumber,
                referenceNumber: this.orderFilter.referenceNumber,
                clientName: this.orderFilter.clientName,
                orderStatus: this.orderFilter.orderStatus,
                title: this.orderFilter.title,
                // validFrom: this.orderFilter.validFrom.year+'-'+this.orderFilter.validFrom.month+'-'+this.orderFilter.validFrom.day,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }]);
        }
    }


    search() {
        this.loadFilter(this.orderFilter);
        this.loadAll();
    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.loadFilter(this.orderFilter);
            this.loadAll();
        }
    }

    clearFilterAndLoadAll() {
        this.orderFilter.internalNumber = '';
        this.orderFilter.referenceNumber = '';
        this.orderFilter.orderStatus = '';
        this.orderFilter.clientName = '';
        this.orderFilter.validFrom = null
        this.orderFilter.validTo = null;
        this.orderFilter.title = '';
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/archived-orders', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }


    public  getEstimationsAsExcelFile():void {
        this.downloadingExcelFile=true;
        this.estimationService.getAsExcel(this.createSearchParams()).subscribe((data: any) => {
            EstimationService.saveDownload(data, 'Wyceny.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
            this.downloadingExcelFile=false;
        },(error:any)=>{
            console.log(error);
            this.downloadingExcelFile=false;
        });
    }
}
// .subscribe((data: any) => {
//     this.saveDownload(data, 'Wyceny.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
// })
