import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {Order, OrderType} from '../order.model';
import {OrderFilter} from '../order-filter.model';
import {OrderService} from '../order.service';
import {Principal} from '../../../shared/auth/principal.service';
import {PaginationConfig} from '../../../blocks/config/uib-pagination.config';
import {ExcelService} from '../../../tn-components/excel.service';
import {ITEMS_PER_PAGE} from '../../../shared/constants/pagination.constants';
import {ResponseWrapper} from '../../../shared/model/response-wrapper.model';
import {URLSearchParams} from '@angular/http';
import {Globals} from '../../../shared';


@Component({
    selector: 'tn-emergency-order',
    templateUrl: './emergency-order.component.html'
})
export class EmergencyOrderComponent implements OnInit, OnDestroy {

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
    validDate:boolean;

    previousPage: any;
    reverse: any;
    orderType: OrderType;
    title: String;
    orderFilter: OrderFilter;

    constructor(private orderService: OrderService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private eventManager: JhiEventManager,
                private paginationUtil: JhiPaginationUtil,
                private paginationConfig: PaginationConfig,
                private excelService: ExcelService,
                public globals:Globals) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.orderType = data['orderType'];
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
        this.orderFilter.creatorName = activatedRoute.snapshot.params['creatorName'] ? activatedRoute.snapshot.params['creatorName'] : '';
    }

    loadAll() {
        if (this.orderType == OrderType.EMERGENCY) {
            this.title = 'Zlecenia awaryjne/usługowe';

            let urlSearchParams = this.createSearchParams();
            this.orderService.getAllEmergencyOrders({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            }, urlSearchParams).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
        }

    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/emergency-order'], {
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
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInOrders();
        this.validDate=true;
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
        this.orders = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }




    model: any;

    exportToExcel(){
        let urlSearchParams = this.createSearchParams();
        this.orderService.getEmergencyOrdersAsExcel(urlSearchParams);
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

        if (this.orderFilter.creatorName !== null && this.orderFilter.creatorName !== '')
            urlSearchParams.append('creatorName.contains', this.orderFilter.creatorName);

        if (this.orderFilter.validTo !== null )//&& this.orderFilter.validTo !== ''
            urlSearchParams.append('createdAt.lessOrEqualThan', this.orderFilter.getValidToString());
        return urlSearchParams;
    }


    loadFilter(filter: OrderFilter) {
        if (filter) {


            this.page = 0;
            // this.currentSearch = query;
            this.router.navigate(['/emergency-order', {
                internalNumber: this.orderFilter.internalNumber,
                referenceNumber: this.orderFilter.referenceNumber,
                clientName: this.orderFilter.clientName,
                orderStatus: this.orderFilter.orderStatus,
                title: this.orderFilter.title,
                creatorName: this.orderFilter.creatorName,
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
        this.orderFilter.creatorName = '';
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/emergency-order', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
}
