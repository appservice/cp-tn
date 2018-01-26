import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService, JhiDataUtils} from 'ng-jhipster';

import {Drawing} from './drawing.model';
import {DrawingService} from './drawing.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {URLSearchParams} from '@angular/http';
import {DrawingFilter} from './drawing-finder/drawing-filter';
import {EstimationFilter} from '../order/estimation-filter.model';


@Component({
    selector: 'jhi-drawing',
    templateUrl: './drawing.component.html'
})
export class DrawingComponent implements OnInit, OnDestroy {

    currentAccount: any;
    drawings: Drawing[];
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
    drawingFilter: DrawingFilter;

    constructor(private drawingService: DrawingService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private dataUtils: JhiDataUtils,
                private router: Router,
                private eventManager: JhiEventManager,
                private paginationUtil: JhiPaginationUtil,
                private paginationConfig: PaginationConfig) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.drawingFilter = new DrawingFilter();
        this.drawingFilter.number = activatedRoute.snapshot.params['number'] ? activatedRoute.snapshot.params['number'] : '';
        this.drawingFilter.name = activatedRoute.snapshot.params['name'] ? activatedRoute.snapshot.params['name'] : '';
    }


    loadAll() {

        let urlSearchParams = this.createSearchParams();

        this.drawingService.query({
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
        this.router.navigate(['/drawing'], {
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
        this.registerChangeInDrawings();
        this.drawingFilter = new DrawingFilter();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Drawing) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInDrawings() {
        this.eventSubscriber = this.eventManager.subscribe('drawingListModification', (response) => this.loadAll());
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
        this.drawings = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }


    createSearchParams(): URLSearchParams {
        let urlSearchParams = new URLSearchParams();
        if (this.drawingFilter.number !== null && this.drawingFilter.number !== '')
            urlSearchParams.append('number.contains', this.drawingFilter.number);

        if (this.drawingFilter.name !== null && this.drawingFilter.name !== '')
            urlSearchParams.append('name.contains', this.drawingFilter.name);

        if (this.drawingFilter.createdAtFrom !== null)
            urlSearchParams.append('createdAt.greaterOrEqualThan', this.drawingFilter.createdAtFrom);

        if (this.drawingFilter.createdAtTo !== null)
            urlSearchParams.append('createdAt.lessOrEqualThan', this.drawingFilter.createdAtTo);

        return urlSearchParams;
    }

    loadFilter(filter: DrawingFilter) {
        if (filter) {
            this.page = 0;
            this.router.navigate(['/drawing', {
                number: this.drawingFilter.number,
                name: this.drawingFilter.name,
                // createdAtFrom: this.drawingFilter.createdAtFrom,
                // createdAtTo: this.drawingFilter.createdAtTo,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }]);
        }
    }


    search() {
        this.loadFilter(this.drawingFilter)
        this.loadAll();
    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.loadFilter(this.drawingFilter)
            this.loadAll();
        }

    }

    clearFilterAndLoadAll() {
        this.page = 0;
        this.drawingFilter.number = '';
        this.drawingFilter.name = '';
        this.drawingFilter.createdAtFrom = null;
        this.drawingFilter.createdAtTo = null;
        this.router.navigate(['/drawing', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

}
