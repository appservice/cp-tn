import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';

import {TechnologyCard} from './technology-card.model';
import {TechnologyCardService} from './technology-card.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper, UserService} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {TechnologyCardFilter} from './technology-card-filter';
import {URLSearchParams} from '@angular/http';
import {UserShortDTO} from '../user';
import {log} from 'util';

@Component({
    selector: 'jhi-technology-card',
    templateUrl: './technology-card.component.html'
})
export class TechnologyCardComponent implements OnInit, OnDestroy {

    currentAccount: any;
    technologyCards: TechnologyCard[];
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
    technologyCardFilter: TechnologyCardFilter;
    technologyCreators: UserShortDTO[];
    selectedCreator: number;//UserShortDTO;

    constructor(private technologyCardService: TechnologyCardService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private eventManager: JhiEventManager,
                private paginationUtil: JhiPaginationUtil,
                private paginationConfig: PaginationConfig,
                private userService: UserService) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
        this.technologyCardFilter = new TechnologyCardFilter();
        this.technologyCardFilter.number = activatedRoute.snapshot.params['drawingNumber'] ? activatedRoute.snapshot.params['drawingNumber'] : '';
        this.technologyCardFilter.name = activatedRoute.snapshot.params['drawingName'] ? activatedRoute.snapshot.params['drawingName'] : '';
        if(activatedRoute.snapshot.params['createdBy'] && activatedRoute.snapshot.params['createdBy']!='null'){
            this.technologyCardFilter.createdBy=activatedRoute.snapshot.params['createdBy'];
        }else{
            this.technologyCardFilter.createdBy=null;
        }


    }

    loadAll() {

        let urlSearchParams = new URLSearchParams();

        // urlSearchParams.append('clientName.contains', this.orderFilter.clientName);
        if (this.technologyCardFilter.number && this.technologyCardFilter.number != null && this.technologyCardFilter.number !== '') {
            urlSearchParams.append('drawingNumber.contains', this.technologyCardFilter.number);
        }
        if (this.technologyCardFilter.name && this.technologyCardFilter.name != null && this.technologyCardFilter.name !== '') {
            urlSearchParams.append('drawingName.contains', this.technologyCardFilter.name);
        }
        if (this.technologyCardFilter.createdAtFrom && this.technologyCardFilter.createdAtFrom != null && this.technologyCardFilter.getValidFromString() !== '') {

            urlSearchParams.append('createdAt.greaterOrEqualThan', this.technologyCardFilter.getValidFromString());
        }
        if (this.technologyCardFilter.createdAtTo && this.technologyCardFilter.createdAtTo != null && this.technologyCardFilter.getValidToString() !== '') {

            urlSearchParams.append('createdAt.lessOrEqualThan', this.technologyCardFilter.getValidToString());
        }
        if (this.technologyCardFilter.createdBy && this.technologyCardFilter.createdBy != null  ) {

            urlSearchParams.append('createdById.equals', this.technologyCardFilter.createdBy.toString());
        }

        this.technologyCardService.query({
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
        this.router.navigate(['/technology-card'], {
            queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,

                    // search: this.currentSearch,

                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
       // this.currentSearch = '';
        this.router.navigate(['/technology-card', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/technology-card', {
           // search: this.currentSearch,
            drawingNumber: this.technologyCardFilter.number,
            drawingName: this.technologyCardFilter.name,
            createdBy: this.technologyCardFilter.createdBy,
            // createdAtFrom: this.technologyCardFilter.createdAtFrom,
            // createdAtTo: this.technologyCardFilter.createdAtTo,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.userService.getUserWhichCanCreateTechnologyCard().subscribe(
            (response: any) => {
                this.technologyCreators = response;
            },
            (error: any)=>{
                console.log(error);
            }
        );

        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTechnologyCards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TechnologyCard) {
        return item.id;
    }

    registerChangeInTechnologyCards() {
        this.eventSubscriber = this.eventManager.subscribe('technologyCardListModification', (response) => this.loadAll());
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
        this.technologyCards = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }


    clearFilterAndLoadAll() {
        this.page = 0;
        this.technologyCardFilter.name = '';
        this.technologyCardFilter.number = '';
        this.technologyCardFilter.createdAtFrom = null;
        this.technologyCardFilter.createdAtTo = null;
        this.technologyCardFilter.createdBy=null;
        this.router.navigate(['/technology-card', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.findByFilter();
        }

    }

    byId(item1: UserShortDTO, item2: UserShortDTO) {
        if(item1 && item2){
            return item1.id === item2.id;

        }
    }


    findByFilter() {
        this.search(this.technologyCardFilter);
        this.loadAll();

    }

}
