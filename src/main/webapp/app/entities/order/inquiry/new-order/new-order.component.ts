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
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Estimation} from '../../../estimation/estimation.model';

@Component({
    selector: 'jhi-new-order',
    templateUrl: './new-order.component.html',
    styles: [],

})
export class NewOrderComponent implements OnInit, OnDestroy {

    title: string;
    clients: Client[];
    order: Order;
    isSaving: boolean;
    attachments: Attachment[] = [];
    subscription: Subscription;
    optionsMap: Map<number, number[]> = new Map<number, number[]>();
    optionsModel: number[];
    closeResult: string;
    clickedRow: number;
    isReadOnly: boolean = false;
    isDrawingUpload: boolean = true;

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

        this.clientService.findAllToTypeahead()
            .subscribe((res: ResponseWrapper) => {
                this.clients = res.json;
                if (this.clients.length == 1) {
                    this.order.clientId = this.clients[0].id;
                }
            }, (res: ResponseWrapper) => this.onError(res.json));

        this.order.orderType = OrderType.ESTIMATION;

        this.subscription = this.route.params.subscribe((params) => {
            console.log(params);
            if (params['id']) {
                console.log('params exiest');
                this.load(params['id']);
                this.title = 'Edytuj zapytanie ofertowe';

            } else {
                this.title = 'Nowe zapytanie ofertowe';
                this.order.canEdit = true;

            }

        });
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackClientById(index: number, item: Client) {
        return item.id;
    }

    addEstimation() {
        // const drawing: Drawing = {id: null, attachments: []}
        this.order.estimations.push({
            id: null, amount: null,

            //     drawing: drawing,
            estimationRemarks: []
        });
        this.isDrawingUpload = false;
    }

    onDeleteRow(index: number) {
        // console.log(event);
        console.log(index);
        this.order.estimations.splice(index, 1);
        let isUploadedAttachment = true;
        for (let estimaiton of this.order.estimations) {
            isUploadedAttachment = isUploadedAttachment && this.checkIfAttachmentExist(estimaiton);
        }
        this.isDrawingUpload = isUploadedAttachment;
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
        this.isDrawingUpload = event.length > 0;

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


    saveAsAdmin() {
        this.isSaving = true;
        // for(let estimation of this.order.estimations){
        //     estimation.drawing.name=estimation.description;
        // }
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderService.updateAsAdmin(this.order));
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

            this.isReadOnly = !order.canEdit && !order.canEditAsAdmin;//order.orderStatus != null && order.orderStatus != 'WORKING_COPY';

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
        if (!this.order.estimations[row].drawing) {
            const drawing: Drawing = {id: null, attachments: []};
            this.order.estimations[row].drawing = drawing;
        }
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

    sendToEstimation() {
        console.log('Sent to estimation');

        this.order.orderStatus = 'SENT_TO_ESTIMATION';//OrderStatus.SENT_TO_ESTIMATION;
        this.save();
    }

    saveChangedDataASAdmin() {
        console.log('Sent to estimation');

        this.saveAsAdmin();
    }

    checkIfAttachmentExist(estimation: Estimation): boolean {

        if (estimation.drawing) {
            if (estimation.drawing.attachments.length > 0) {

                return true;
            }
        }
        this.isDrawingUpload = false;
        return false;
    }
}
