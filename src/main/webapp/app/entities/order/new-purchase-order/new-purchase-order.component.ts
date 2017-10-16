import {Component, OnDestroy, OnInit} from '@angular/core';
import {Client} from '../../client/client.model';
import {ClientService} from '../../client/client.service';
import {ResponseWrapper} from '../../../shared/model/response-wrapper.model';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Order, OrderType, OrderStatus} from '../order.model';
import {Attachment} from '../../../tn-components/tn-file-uploader/attachment.model';
import {OrderService} from '../order.service';
import {Observable} from 'rxjs/Rx';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {isDefined} from '@angular/compiler/src/util';
import {Drawing} from '../../drawing/drawing.model';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Estimation} from '../../estimation/estimation.model';

@Component({
    selector: 'tn-new-purchase-order',
    templateUrl: './new-purchase-order.component.html',
    styles: [],

})
export class NewPurchaseOrderComponent implements OnInit, OnDestroy {

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
    purchaseOrderId: number;

    dropdownList = [];

    constructor(private alertService: JhiAlertService,
                private clientService: ClientService,
                private orderService: OrderService,
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
            console.log(params);
            if (params['inquiryId']) {
                console.log('new purcahse order');
                this.loadNewPurchaseOrder(params['inquiryId']);
                this.title = 'Nowe zamówienie';
            }
            if (params['id']) {
                console.log('edit purcahse order');
                this.loadEditedPurchaseOrder(params['id']);
            }

        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    onDeleteRow(index: number) {
        // console.log(event);
        console.log(index);
        this.order.estimations.splice(index, 1);
    }

    onWorkingCopyBtnClick() {
        console.log('save is cliccked');
        console.log(this.order);
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
        console.log('event from parent object: ', event);
    }

    save() {
        this.isSaving = true;
        // for(let estimation of this.order.estimations){
        //     estimation.drawing.name=estimation.description;
        // }
        if (this.order.id !== undefined && this.order.id !== null) {
            this.subscribeToSaveResponse(
                this.orderService.update(this.order));
        } else {
            this.subscribeToSaveResponse(
                this.orderService.createNewPurchaseOrder(this.order));
        }
    }

    private subscribeToSaveResponse(result: Observable<Order>) {
        result.subscribe((res: Order) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Order) {
        this.eventManager.broadcast({name: 'orderListModification', content: 'OK'});
        this.isSaving = false;
        this.router.navigate(['purchase-order']);
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

    loadNewPurchaseOrder(id) {
        this.orderService.findEstimated(id).subscribe((order) => {
            this.order = order;
            console.log(order);

            console.log('order status: ', order.orderStatus.toString());
            console.log('enum status: ', OrderStatus['WORKING_COPY']);
            console.log('enum 3', order.orderStatus.constructor.name);
            //     console.log('enum 3', ]);
            this.isReadOnly = order.orderStatus != null && order.orderStatus !== 'WORKING_COPY';
            order.internalNumber = null;
            order.inquiryId = order.id;
            order.id = null;
           // order.name = null;
            order.orderType = OrderType.PRODUCTION;
            order.sapNumber = null;
            order.orderStatus = null;
            order.createdAt = null;
            order.description = null;
            order.referenceNumber = null;
        });
    }

    loadEditedPurchaseOrder(id) {

        this.orderService.find(id).subscribe((order) => {
            this.order = order;
        });
        this.title = 'Edytuj Zamówienie'
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        // this.eventManager.destroy(this.eventSubscriber);
    }

    getOptions(index: number): number[] {
        return this.optionsMap.get(index);
    }

    openModal(content, row: number) {
        console.log('clickedRow: ', row);
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

    sendToProduction() {
        console.log('Sent to estimation');

        this.order.orderStatus = 'IN_PRODUCTION'; // OrderStatus.SENT_TO_ESTIMATION;
        this.save();
    }

    calculateTotalValue(): number {
        let total = 0;
        for (const estimation of this.order.estimations) {
            total = total + estimation.estimatedCost * estimation.amount;
        }
        return total;
    }

    createNewPurchaseOrder(order: Order) {

    }
}
