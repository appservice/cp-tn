import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TechnologyCard} from '../technology-card.model';
import {TechnologyCardService} from '../technology-card.service';
import {JhiParseLinks} from 'ng-jhipster';
import {ResponseWrapper} from '../../../shared/model/response-wrapper.model';
import {URLSearchParams} from '@angular/http';

@Component({
    selector: 'jhi-technology-card-finder',
    templateUrl: './technology-card-finder.component.html',
    styles: []
})
export class TechnologyCardFinderComponent implements OnInit {


    @Input()
    display: boolean=false;

    @Output()
    onChooseTechnologyCard: EventEmitter<TechnologyCard> = new EventEmitter<TechnologyCard>();

    technologyCards: TechnologyCard [];
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: number = 5;
    page: number = 1;
    predicate: any;
    previousPage: any;
    reverse: any;

    nameFilter: string;
    drawingNumberFilter: string;

    constructor(public activeModal: NgbActiveModal,
                private technologyCardService: TechnologyCardService,
                private parseLinks: JhiParseLinks,) {
    }

    ngOnInit() {
        this.technologyCards = [];
    }
    findTechnologyCardAndLoadFirstPage(){
        this.page=1;
        this.findTechnologyCard();
    }

    findTechnologyCard() {
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('description.contains', this.nameFilter);
        urlSearchParams.append('drawingNumber.contains', this.drawingNumberFilter);

        this.technologyCardService.query({
            'page': (this.page - 1),
            'size': this.itemsPerPage,

        }, urlSearchParams).subscribe((res: ResponseWrapper) => {
            this.onSuccess(res.json, res.headers);
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.findTechnologyCard();
        }
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.technologyCards = data;
    }

    onChooseRow(technologyCard: TechnologyCard) {
        this.technologyCardService.find(technologyCard.id).subscribe(res => {
            this.display=false;
            this.onChooseTechnologyCard.emit(res);
               // this.activeModal.close(res);
            },
        );

    }


    clearFilter() {
        this.nameFilter = null;
        this.drawingNumberFilter = null;
    }

    onEnterClickFilter(event: any) {
        if (event.keyCode == 13) {
            this.findTechnologyCardAndLoadFirstPage();
        }
    }

    showModal(){
        this.display=true;
    }

}
