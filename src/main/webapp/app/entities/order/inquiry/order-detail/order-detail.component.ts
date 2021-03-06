import {Component, OnDestroy, OnInit} from '@angular/core';
import {Client} from '../../../client/client.model';
import {ClientService} from '../../../client/client.service';
import {ResponseWrapper} from '../../../../shared/model/response-wrapper.model';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Order, OrderStatus, OrderType} from '../../order.model';
import {Attachment} from '../../../../tn-components/tn-file-uploader/attachment.model';
import {OrderService} from '../../order.service';
import {Observable} from 'rxjs/Rx';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Drawing} from '../../../drawing/drawing.model';
import {ExcelService} from '../../../../tn-components/excel.service';

@Component({
    selector: 'tn-order-detail',
    templateUrl: './order-detail.component.html',
    // styles: [],
    styleUrls: ['./order-detail.component.css'],

})
export class OrderDetailComponent implements OnInit, OnDestroy {

    title: string;
    clients: Client[];
    order: Order;
    isSaving: boolean;
    // optionsModel: number[];
    // selectedAttachments: SelectItem[]=[];
    attachments: Attachment[] = [];
    subscription: Subscription;
    optionsMap: Map<number, number[]> = new Map<number, number[]>();
    optionsModel: number[];
    closeResult: string;
    clickedRow: number;
    isReadOnly: boolean = false;

    dropdownList = [];

    constructor(private alertService: JhiAlertService,
                private clientService: ClientService,
                private orderService: OrderService,
                private eventManager: JhiEventManager,
                private router: Router,
                private route: ActivatedRoute,
                private modalService: NgbModal,
                private excelService: ExcelService,) {
        this.order = new Order();
        this.order.estimations = [];

    }

    ngOnInit() {
        this.isSaving = false;

        this.clientService.query()
            .subscribe((res: ResponseWrapper) => {
                this.clients = res.json;
            }, (res: ResponseWrapper) => this.onError(res.json));

        this.order.orderType = OrderType.ESTIMATION;

        this.subscription = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.load(params['id']);

            }

        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    onWorkingCopyBtnClick() {
        this.order.orderStatus = 'WORKING_COPY';//OrderStatus.WORKING_COPY;
        this.save();
    }

    previousState() {
        window.history.back();
    }

    onFileArrayChange(event: Attachment[]) {
        this.attachments = event;
    }

    save() {
        this.isSaving = true;
        // for(let estimation of this.order.estimations){
        //     estimation.drawing.name=estimation.description;
        // }
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderService.update(this.order));
        } else {
            this.subscribeToSaveResponse(
                this.orderService.create(this.order));
        }
    }

    private subscribeToSaveResponse(result: Observable<Order>) {
        result.subscribe((res: Order) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Order) {
        this.eventManager.broadcast({name: 'orderListModification', content: 'OK'});
        this.isSaving = false;
        this.router.navigate(['order']);
        // this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    load(id) {
        this.orderService.findInquiry(id).subscribe((order) => {
            this.order = order;
            this.isReadOnly = order.orderStatus != null && order.orderStatus != 'WORKING_COPY';

        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        // this.eventManager.destroy(this.eventSubscriber);
    }

    openModal(content, row: number) {
        if (!this.order.estimations[row].drawing) {
            const drawing: Drawing = {id: null, attachments: []};
            this.order.estimations[row].drawing = drawing;
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

    convertToDate(ngBootstrapDate: any): Date {

        if (ngBootstrapDate && ngBootstrapDate != null) {
            if (typeof ngBootstrapDate === 'string') {
                return null;
                // return new Date( ngBootstrapDate);
            }
            return new Date(ngBootstrapDate.year, ngBootstrapDate.month - 1, ngBootstrapDate.day);
        }
        return null;
    }

    isProductionCreateBtnDisabled(): boolean {
        let isDisabled = false;
        for (let estimation of this.order.estimations) {
            isDisabled = isDisabled || estimation.estimatedCost !== null
        }
        return isDisabled;
    }

    cloneOrder(): void {
        this.orderService.cloneOrder(this.order.id).subscribe((resp: any) => {
                this.router.navigateByUrl("/order")
            },
            (error: any) => {
                this.alertService.error('error.canNotCloneOrder');
                console.log(error);
            });
    }

}
