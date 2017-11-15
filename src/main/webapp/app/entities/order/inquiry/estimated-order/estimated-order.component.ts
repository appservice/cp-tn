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
import {Drawing} from '../../../drawing/drawing.model';
import {ModalDismissReasons, NgbDatepicker, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Estimation} from '../../../production/estimation.model';
import {MoveToArchiveDialogComponent} from './move-to-archive-dialog.component';
import {debounceTime} from 'rxjs/operator/debounceTime';
import {Subject} from "rxjs/Subject";
import {EstimationService} from "../../../estimation/estimation.service";


@Component({
    selector: 'estimated-order',
    templateUrl: './estimated-order.component.html',
    styles: [],

})
export class EstimatedOrderComponent implements OnInit, OnDestroy {

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
    alert: any;
    private _success = new Subject<any>();

    dropdownList = [];

    constructor(private alertService: JhiAlertService,
                private clientService: ClientService,
                private orderService: OrderService,
                private estimationService: EstimationService,
                private eventManager: JhiEventManager,
                private router: Router,
                private route: ActivatedRoute,
                private modalService: NgbModal) {
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
                // console.log('params exiest');
                this.load(params['id']);
                this.title = 'Edytuj zlecenie';

            } else {
                this.title = 'Nowe zlecenie';
            }

        });

        this._success.subscribe((alert) => this.alert = alert);
        debounceTime.call(this._success, 3000).subscribe(() => this.alert = null);

    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    addEstimation() {
        const drawing: Drawing = {id: null, attachments: []}
        this.order.estimations.push({
            id: null, amount: null,
            drawing: drawing
        });
    }

    onDeleteRow(index: number) {
        // console.log(event);
        // console.log(index);
        this.order.estimations.splice(index, 1);
    }

    onWorkingCopyBtnClick() {
        // console.log('save is cliccked');
        // console.log(this.order);
        this.order.orderStatus = 'WORKING_COPY';//OrderStatus.WORKING_COPY;
        this.save();
    }

    previousState() {
        window.history.back();
    }

    onFileChange() {
        console.log(this.optionsModel);
        // this.save();
    }

    onFileArrayChange(event: Attachment[]) {
        this.attachments = event;
        // console.log('event from parent object: ', event);

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
        this.orderService.find(id).subscribe((order) => {
            this.order = order;

            // console.log('order status: ',order.orderStatus.toString());
            // console.log('enum status: ',OrderStatus['WORKING_COPY']);
            // console.log('enum 3', order.orderStatus.constructor.name);
            //     console.log('enum 3', ]);
            this.isReadOnly = order.orderStatus != null && order.orderStatus != 'WORKING_COPY';

        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        // this.eventManager.destroy(this.eventSubscriber);
    }

    getOptions(index: number): number[] {
        return this.optionsMap.get(index);
    }

    openModal(content, row: number) {
        // console.log('clickedRow: ', row);
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

    sendToEstimation() {
        // console.log('Sent to estimation');

        this.save();
    }

    convertToDate(ngBootstrapDate: any): Date {

        if (ngBootstrapDate && ngBootstrapDate !== null) {


            if (typeof ngBootstrapDate === 'string') {
                return null;
                // return new Date( ngBootstrapDate);
            }

            return new Date(ngBootstrapDate.year, ngBootstrapDate.month, ngBootstrapDate.day);
        }
        return null;
    }

    isPrintingDisabled(): boolean {
        let isPrintable = false;
        for (let estimation  of this.order.estimations) {
            isPrintable = isPrintable || estimation.checked;
        }
        return !isPrintable;
    }


    createPdfOffer() {
        this.orderService.createPdfOffer(this.order);
    }

    moveToArchive() {
        const modalRef = this.modalService.open(MoveToArchiveDialogComponent);
        modalRef.componentInstance.order = this.order;
    }


    addOfferRemarks() {
        this.orderService.saveOfferRemarks(this.order.id, this.order.offerRemarks).subscribe((res) => {
            this._success.next({
                type: 'success',
                message: 'Zaktualizowano.',
            });

        });
    }

    public closeAlert() {
        this.alert = null;
    }

    publishPrice(estimation: Estimation, isPublic: boolean) {
        console.log(estimation, isPublic);
        this.estimationService.publishPrice(estimation.id, isPublic).subscribe((resp) => {
            estimation.pricePublished=isPublic;
            console.log(resp)
        }, (error: any) => {
            console.log(error);
        });
    }

}
