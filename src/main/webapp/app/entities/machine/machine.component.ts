import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService, JhiDateUtils} from 'ng-jhipster';

import {Machine} from './machine.model';
import {MachineService} from './machine.service';
import {Globals, ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {IMyDate, IMyDateModel} from 'mydatepicker';
import {URLSearchParams } from '@angular/http';


@Component({
    selector: 'jhi-machine',
    templateUrl: './machine.component.html'
})
export class MachineComponent implements OnInit, OnDestroy {

    currentAccount: any;
    machines: Machine[];
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
    dateValidFrom: any;//IMyDateModel;

    constructor(private machineService: MachineService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private eventManager: JhiEventManager,
                private paginationUtil: JhiPaginationUtil,
                private dateUtils: JhiDateUtils,
                private paginationConfig: PaginationConfig,
                public globals:Globals
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';

    }

    loadAll(operatonDate: IMyDateModel) {
        if (this.currentSearch) {
            this.machineService.search({
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()
            }).subscribe(
                (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                (res: ResponseWrapper) => this.onError(res.json)
            );
            return;
        }
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('operationDate', this.dateUtils.convertLocalDateToServer(operatonDate.date, 'yyyy-MM-dd'));
        this.machineService.query({
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
        this.router.navigate(['/machine'], {
            queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll(this.dateValidFrom);
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/machine', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll(this.dateValidFrom);
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/machine', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll(this.dateValidFrom);
    }

    ngOnInit() {

        let date = new Date();
        this.dateValidFrom = {date: {year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate()}};


        this.loadAll(this.dateValidFrom);
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInMachines();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Machine) {
        return item.id;
    }

    registerChangeInMachines() {
        this.eventSubscriber = this.eventManager.subscribe('machineListModification', (response) => this.loadAll(this.dateValidFrom));
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
        this.machines = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    onDateChanged(event: IMyDateModel) {
        console.log(event);
        if (event.date.year > 0) {
            this.dateValidFrom = event;
            this.loadAll(event);

        }
    }
}
