import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiParseLinks} from 'ng-jhipster';
import {ResponseWrapper} from '../../../shared/model/response-wrapper.model';
import {URLSearchParams} from '@angular/http';
import {Drawing} from '../drawing.model';
import {DrawingService} from '../drawing.service';

@Component({
    selector: 'tn-drawing-finder',
    templateUrl: 'drawing-finder.component.html',
    styles: []
})
export class DrawingFinderComponent implements OnInit {

    // @Output()
    //     onChooseRow: EventEmitter<TechnologyCard> =new EventEmitter<TechnologyCard>();

    drawings: Drawing [];
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any = 5;
    page: any = '1';
    predicate: any;
    previousPage: any;
    reverse: any;

    nameFilter: string;
    numberFilter: string;

    constructor(public activeModal: NgbActiveModal,
                private drawingService: DrawingService,
                private parseLinks: JhiParseLinks,) {
    }

    ngOnInit() {
        this.drawings = [];
    }

    findDrawings() {
        let urlSearchParams=new URLSearchParams();
        urlSearchParams.append('number.contains', this.numberFilter);
        urlSearchParams.append('name.contains', this.nameFilter);
        console.log(urlSearchParams);

        this.drawingService.query({
            'page': (this.page - 1),
            'size': this.itemsPerPage,

        },urlSearchParams).subscribe((res: ResponseWrapper) => {
            this.onSuccess(res.json, res.headers);
        });
    }

    loadPage(page: number) {
        console.log('load page: ', page);
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.findDrawings();
        }
    }

    private onSuccess(data, headers) {
        console.log(headers);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.drawings = data;
    }

    onChooseRow(drawing: Drawing) {
        this.drawingService.find(drawing.id).subscribe(res => {
                this.activeModal.close(res);
            },
        );

    }


    clearFilter(){
        this.nameFilter=null;
        this.numberFilter=null;
    }
    // private onError(error) {
    //     this.alertService.error(error.message, null, null);
    // }
}
