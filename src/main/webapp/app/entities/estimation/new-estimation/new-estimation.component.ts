import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Estimation} from '../estimation.model';
import {Attachment} from '../../../tn-components/tn-file-uploader/attachment.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ResponseWrapper} from '../../../shared';
import {Machine, MachineService} from '../../machine';
import {CurrencyMaskConfig} from 'ng2-currency-mask/src/currency-mask.config';
import {Observable} from 'rxjs/Observable';
import {Unit, UnitService} from '../../unit';
import {EstimationService} from '../estimation.service';
import {isNullOrUndefined} from 'util';
import {OrderService} from '../../order';
import {OrderSimpleDTO} from '../../order/order-simpleDTO.model';
import {TnAlert} from '../../../tn-components/tn-alert';
import {TechnologyCard} from '../../technology-card';
import {DrawingFinderComponent} from '../../drawing';
import {Operation} from '../../operation';
import {NgForm} from '@angular/forms';
import {TnModalConfirmComponent} from '../../../tn-components/tn-modal-confirm/tn-modal-confirm.component';

@Component({
    selector: 'new-estimation',
    templateUrl: './new-estimation.component.html',
    styles: [],

})
export class NewEstimationComponent implements OnInit, OnDestroy {
    private commercialPartsTotalCost: number = 0;
    private operationsTotalCost: number = 0;
    private cooperationTotalCost: number = 0;
    private sumOfWorkingHours: number = 0;

    @ViewChild(TnModalConfirmComponent)
    tnModalConfirm: TnModalConfirmComponent;

    // editForm: FormGroup;
    estimation: Estimation;
    order: OrderSimpleDTO;
    isSaving: boolean;
    isExporting: boolean;
    subscription: Subscription;
    closeResult: string;
    clickedRow: number;
    isCollapsed: boolean = false;
    model = 1;
    machines: Machine[] = [];
    private searchingUnit = false;
    hideSearchingWhenUnsubscribed = new Observable(() => () => this.searchingUnit = false);
    searchFeild: boolean;
    backButtonClicked: boolean = false;
    termOfRealzationType: string = 'RELATIVE';//TermOfRealizationType;

    @ViewChild('editForm') editForm: NgForm;

    currencyMaskOpt: CurrencyMaskConfig;

    constructor(private alertService: JhiAlertService,
                private machineService: MachineService,
                private eventManager: JhiEventManager,
                private router: Router,
                private route: ActivatedRoute,
                private modalService: NgbModal,
                private unitService: UnitService,
                private estimationService: EstimationService,
                private orderService: OrderService) {
        this.estimation = new Estimation();
        this.estimation.discount = 0;
        this.estimation.operations = [];
        this.estimation.commercialParts = [];
        this.estimation.cooperationList = [];
        this.order = new OrderSimpleDTO();
    }

    ngOnInit() {
        this.isSaving = false;

        this.machineService.getAllNotPageable()
            .subscribe((res: ResponseWrapper) => {
                this.machines = res.json;


                this.subscription = this.route.params.subscribe((params) => {
                    if (params['id']) {
                        // console.log('params exiest');
                        this.load(params['id']);

                    }

                });
            }, (res: ResponseWrapper) => this.onError(res.json));

        this.estimation.materialPrice = 0;
        this.estimation.drawing = {id: null, attachments: []};
        // this.estimation.amount = 3;
        this.estimation.orderId = 1;

        this.currencyMaskOpt = {
            align: '',
            prefix: '',
            thousands: ' ',
            decimal: ',',
            allowNegative: false,
            suffix: ' PLN',
            allowZero: false,
            precision: 2,
        };
        this.calculateCommercialPartsTotalCost();
        this.calculateCooperationTotalCost();
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMachineById(index: number, item: Machine) {
        return index;
    }

    addOperation() {
        this.estimation.operations.push({
            id: null,
            sequenceNumber: (this.estimation.operations.length + 1) * 5

        });
    }

    addCommercialPart() {
        this.estimation.commercialParts.push({
            id: null

        });
    }

    addCooperation() {
        this.estimation.cooperationList.push({
            id: null,
            amount: 1,

        });
    }

    onDeleteRow(index: number) {
        console.log(index);
        this.estimation.operations.splice(index, 1);
        this.calculateOperationsTotalCost();
    }

    onDeleteCommercialPart(index: number) {
        console.log(index);
        this.estimation.commercialParts.splice(index, 1);
        this.calculateCommercialPartsTotalCost();
    }

    onDeleteCooperation(index: number) {
        console.log(index);
        this.estimation.cooperationList.splice(index, 1);
        this.calculateCooperationTotalCost();
    }

    onSaveBtnClick() {
        //  this.tnModalConfirm.openSaveAndCloseModal()

        this.save();
    }

    previousState() {
        this.backButtonClicked = true;
        window.history.back();
    }

    onFileChange() {
        // console.log(this.optionsModel);
        // this.save();
    }

    onFileArrayChange(event: Attachment[]) {
        if (this.estimation.drawing) {
            this.estimation.drawing.attachments = event;
            console.log('event from parent object: ', event);

        }
        // this.attachments = event;
    }

    save() {
        this.isSaving = true;
        if (this.estimation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.estimationService.update(this.estimation));
        } else {
            this.subscribeToSaveResponse(
                this.estimationService.create(this.estimation));
        }
    }

    private subscribeToSaveResponse(result: Observable<Estimation>) {
        result.subscribe((res: Estimation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Estimation) {
        this.eventManager.broadcast({name: 'estimationListModification', content: 'OK'});
        //  this.previousState();
        this.isSaving = false;
        this.editForm.form.markAsPristine();

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
        this.estimationService.find(id).subscribe((estimation) => {
            this.estimation = estimation;
            this.calculateOperationsTotalCost();
            this.calculateCommercialPartsTotalCost();
            this.calculateCooperationTotalCost();
            if (this.estimation.estimatedRealizationDate && this.estimation.estimatedRealizationDate != null) {
                this.termOfRealzationType = 'ABSOLUTE';
            }
            if (!this.estimation.executionTimeUnit) {
                this.estimation.executionTimeUnit = 'WEEKS';
            }

            this.orderService.findOrderSimpleDto(estimation.orderId).subscribe((order) => {
                this.order = order;

            }, (error) => {
                this.onError(error);
            });


        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    openModal() {
        this.showAttachmentDialog();
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

    onModelChange() {

    }

    searchUnit = (text$: Observable<string>) =>
        text$
            .debounceTime(300)
            .distinctUntilChanged()
            .do(() => this.searchingUnit = true)
            .switchMap((term) =>
                this.unitService.searchBySentence(term)
                    .do(() => this.searchFeild = false)
                    .catch(() => {
                        this.searchFeild = true;
                        return Observable.of([]);
                    }))
            .do(() => {
                this.searchingUnit = false;
            })
            // .subscribe();
            .merge(this.hideSearchingWhenUnsubscribed);
    // .do(() => (console.log("unit model",this.unitModel)));
    formatter = (x: Unit) => x.name;
    resultFormatter = (x: Unit) => x.name;

    onSelectItem(event: any) {

        console.log('item ', event.item);
    }

    onMachineChanged(operation: Operation) {
        if (operation.machine && operation.machine.defaultTechnologyDesc && operation.machine.defaultTechnologyDesc !== null) {
            operation.description = operation.machine.defaultTechnologyDesc;
        }
        this.calculateOperationsTotalCost();
    }

    calculateOperationsTotalCost() {
        this.operationsTotalCost = 0;
        this.sumOfWorkingHours = 0;

        for (const operation of this.estimation.operations) {
            if (operation.estimatedTime != null) {
                this.sumOfWorkingHours = this.sumOfWorkingHours + operation.estimatedTime;
                if (operation.machine && operation.machine.workingHourPrice != null) {
                    this.operationsTotalCost = this.operationsTotalCost + operation.estimatedTime * operation.machine.workingHourPrice;

                }

            }
        }
    }

    calculateCommercialPartsTotalCost() {
        this.commercialPartsTotalCost = 0;
        for (const commercialPart of this.estimation.commercialParts) {
            if (commercialPart.amount != null && commercialPart.price != null) {
                this.commercialPartsTotalCost = this.commercialPartsTotalCost + commercialPart.amount * commercialPart.price;
            }
        }
    }

    calculateCooperationTotalCost() {
        this.cooperationTotalCost = 0;
        for (const cooperation of this.estimation.cooperationList) {
            if (cooperation.amount != null && cooperation.price != null) {
                this.cooperationTotalCost = this.cooperationTotalCost + cooperation.amount * cooperation.price;
            }
        }
    }

    compareMachine(m1: Machine, m2: Machine): boolean {
        if (!isNullOrUndefined(m1) && !isNullOrUndefined(m2)) {
            return m1.id === m2.id;
        }
    }

    calculateTotal(): number {
        return this.commercialPartsTotalCost + this.operationsTotalCost + this.estimation.materialPrice + this.cooperationTotalCost;
    }

    calculateDiscount(): number {
        return this.calculateTotal() * this.estimation.discount / 100
    }

    getTechnologyCardPdf() {
        this.estimationService.download(this.estimation);
    }

    messages: any[] = ['Message 5'];

    exportToTechnologyCard() {
        this.isExporting = true;
        console.log('test export technology card');
        console.log('est: ', this.estimation);
        this.estimationService.exportToTechnologyCard(this.estimation).subscribe(res => {
                const modalRef = this.modalService.open(TnAlert);
                modalRef.componentInstance.header = 'Uwaga';
                modalRef.componentInstance.content = 'Dane zostały wyeksportowane do karty technologicznej nr: ' + res.json().id;
                this.isExporting = false;
            }, (error: any) => {
                this.isExporting = false;

                const modalRef = this.modalService.open(TnAlert);
                modalRef.componentInstance.header = 'Uwaga';
                modalRef.componentInstance.content = 'Błąd podczas eksportu! '
            }
        );
    }

    private insertOperationFromTechnologyCard(technologyCard: TechnologyCard): void {
        console.log(technologyCard);
        if (!this.estimation.material || this.estimation.material == null) {
            this.estimation.material = technologyCard.material;

        }
        if (!this.estimation.materialType || this.estimation.materialType == null) {
            this.estimation.materialType = technologyCard.materialType;

        }
        if (!this.estimation.mass || this.estimation.mass == null) {
            this.estimation.mass = technologyCard.mass;

        }
        if (!this.estimation.materialPrice || this.estimation.materialPrice == null) {
            this.estimation.materialPrice = technologyCard.materialPrice;

        }

        for (let operation of technologyCard.operations) {
            let newOperation = new Operation();
            newOperation.description = operation.description;
            newOperation.estimatedTime = operation.estimatedTime;
            let machines = this.machines.filter(m => m.id === operation.machine.id);

            newOperation.machine = machines[0];
            newOperation.sequenceNumber = operation.sequenceNumber;

            this.estimation.operations.push(newOperation);

        }

        for (let commercialPart of technologyCard.commercialParts) {
            commercialPart.id = null;
            this.estimation.commercialParts.push(commercialPart);
        }

        for (let cooperation of technologyCard.cooperationList) {
            cooperation.id = null;
            this.estimation.cooperationList.push(cooperation);

        }
        this.editForm.control.markAsDirty();

        this.calculateOperationsTotalCost();
        this.calculateCooperationTotalCost();
        this.calculateCommercialPartsTotalCost();
        this.calculateTotal();
    }

    openDrawingCardModal() {

        const modalRef = this.modalService.open(DrawingFinderComponent, {size: 'lg'});
        console.log(modalRef.result);
        modalRef.result.then(result => {
            console.log(result);
            this.estimation.drawing = result;

        }, (reason: any) => {
            console.log(reason)
        });
        //
    }

    clonePriceFromSummary() {
        this.estimation.estimatedCost = this.calculateTotal() + this.calculateDiscount();
    }

    hasChanges(): boolean {
        if (this.isSaving) {
            return false;
        }
        return this.editForm.dirty
    }

    openSaveAndCloseModal() {
        const modalRef = this.modalService.open(TnModalConfirmComponent);
        modalRef.componentInstance.headerText = 'Zapis wyceny';
        modalRef.componentInstance.contentText = 'Czy wyeksportować kartę technologii?';
        modalRef.result.then((value: any) => {
            this.exportToTechnologyCard();
            this.onSaveBtnClick();
        }, (rejectValue: any) => {
            this.onSaveBtnClick();

        });

    }

    display: boolean = false;

    showAttachmentDialog() {
        this.display = true;
    }

    onChooseTechnologyCard(event) {
        console.log('chosed technology card: ', event);
    }
}

export const enum TermOfRealizationType {
    'RELATIVE',
    'ABSOLUTE',
}
