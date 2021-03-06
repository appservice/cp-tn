import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import {JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService} from 'ng-jhipster';
import {URLSearchParams} from '@angular/http';
import {EstimationService} from '../../../estimation/estimation.service';
import {Principal} from '../../../../shared/auth/principal.service';
import {ITEMS_PER_PAGE} from '../../../../shared/constants/pagination.constants';
import {ResponseWrapper} from '../../../../shared/model/response-wrapper.model';
import {Estimation} from '../../../estimation/estimation.model';
import {EstimationFilter} from '../../estimation-filter.model';
import {Drawing} from '../../../drawing/drawing.model';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Attachment} from '../../../../tn-components/tn-file-uploader/attachment.model';


@Component({
    selector: 'tn-inquiry-item-finder',
    templateUrl: './inquiry-item-finder.component.html',
    styles: []
})
export class InquiryItemFinderComponent implements OnInit, OnDestroy {

    estimations: Estimation [];
    itemsPerPage: number;
    routeData: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    links: any;
    totalItems: any;
    queryCount: any;
    currentSearch: string;
    estimationFilter: EstimationFilter;
    closeResult: string;
    attachments: Attachment[] = [];
    clickedRow: number;


    constructor(private estimationService: EstimationService,
                private parseLinks: JhiParseLinks,
                private alertService: JhiAlertService,
                private principal: Principal,
                private activatedRoute: ActivatedRoute,
                private router: Router,
                private modalService: NgbModal,
                private eventManager: JhiEventManager) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.estimations = [];
        this.estimationFilter = new EstimationFilter();

        this.routeData = this.activatedRoute.data.subscribe((data) => {
            console.log(data);
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;

        });
        this.estimationFilter.itemNumber = activatedRoute.snapshot.params['itemNumber'] ? activatedRoute.snapshot.params['itemNumber'] : '';
        this.estimationFilter.itemName = activatedRoute.snapshot.params['itemName'] ? activatedRoute.snapshot.params['itemName'] : '';


    }

    ngOnInit() {
        this.loadAll();

    }


    loadAll() {

        let urlSearchParams = new URLSearchParams();

        // urlSearchParams.append('clientName.contains', this.orderFilter.clientName);
        if (this.estimationFilter.itemNumber && this.estimationFilter.itemNumber != null && this.estimationFilter.itemNumber !== '') {
            urlSearchParams.append('itemNumber.contains', this.estimationFilter.itemNumber);
        }
        if (this.estimationFilter.itemName && this.estimationFilter.itemName != null && this.estimationFilter.itemName !== '') {
            urlSearchParams.append('itemName.contains', this.estimationFilter.itemName);

        }
        this.estimationService.findInquiryByCriteria({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
        }, urlSearchParams).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }


    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.estimations = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }


    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    transition() {
        this.router.navigate(['/inquiry-item-finder'], {
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

    ngOnDestroy(): void {
    }

    clearFilterAndLoadAll(): void {
        this.estimationFilter.itemName = '';
        this.estimationFilter.clientName = '';
        this.estimationFilter.itemNumber = '';
        this.page = 1;
        this.sort();
        this.router.navigate(['/inquiry-item-finder'/*, {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }*/]);
        this.loadAll();
    }

    findByFilter() {
        this.search(this.estimationFilter);
        this.loadAll();

    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.search(this.estimationFilter);
            this.loadAll();
        }

    }

    openModal(content, row: number) {
        if (!this.estimations[row].drawing) {
            const drawing: Drawing = {id: null, attachments: []};
            this.estimations[row].drawing = drawing;
        }
        this.clickedRow = row;

        this.modalService.open(content, {size: 'lg'}).result.then((result) => {
            this.closeResult = `Closed with: ${result}`;
        }, (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        });
    }

    private getDismissReason(reason: any): string {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

    onFileArrayChange(event: Attachment[]) {
        this.attachments = event;
        console.log('event from parent object: ', event);
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    search(filter: EstimationFilter) {
        if (filter) {


            this.page = 0;
            // this.currentSearch = query;
            this.router.navigate(['/inquiry-item-finder', {
                itemNumber: this.estimationFilter.itemNumber,
                itemName: this.estimationFilter.itemName,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }]);
        }
        // this.loadAll();
    }

}
