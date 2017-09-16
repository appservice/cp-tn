import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {Estimation} from '../estimation.model';
import {Attachment} from '../../../tn-components/tn-file-uploader/attachment.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Machine} from '../../machine/machine.model';
import {ResponseWrapper} from '../../../shared/model/response-wrapper.model';
import {MachineService} from '../../machine/machine.service';
import {CurrencyMaskConfig} from 'ng2-currency-mask/src/currency-mask.config';
import {Observable} from 'rxjs/Observable';
import {UnitService} from '../../unit/unit.service';
import {Unit} from '../../unit/unit.model';
import {EstimationService} from '../estimation.service';
import {isNullOrUndefined} from 'util';
import {OrderService} from '../../order/order.service';
import {Order} from '../../order/order.model';
import {OrderSimpleDTO} from '../../order/order-simpleDTO.model';
import {TnAlert} from '../../../tn-components/tn-alert';

@Component({
    selector: 'new-estimation',
    templateUrl: './new-estimation.component.html',
    styles: [],

})
export class NewEstimationComponent implements OnInit, OnDestroy {
    private commercialPartsTotalCost: number = 0;
    private operationsTotalCost: number = 0;

    estimation: Estimation;
    order: OrderSimpleDTO;
    isSaving: boolean;
    isExporting: boolean;
    subscription: Subscription;
    closeResult: string;
    clickedRow: number;
    model = 1;
    machines: Machine[] = [];
    private searchingUnit = false;
    hideSearchingWhenUnsubscribed = new Observable(() => () => this.searchingUnit = false);
    searchFeild: boolean;


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
        this.order = new OrderSimpleDTO();

    }

    ngOnInit() {
        this.isSaving = false;


        this.subscription = this.route.params.subscribe((params) => {
            console.log(params);
            if (params['id']) {
                console.log('params exiest');
                this.load(params['id']);

            }

        });
        this.machineService.query()
            .subscribe((res: ResponseWrapper) => {
                this.machines = res.json;

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
            precision: 2
        };
        this.calculateCommercialPartsTotalCost();
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMachineById(index: number, item: Machine) {
        return index;
    }

    addOperation() {
        this.estimation.operations.push({
            id: null

        });
    }

    addCommercialPart() {
        this.estimation.commercialParts.push({
            id: null

        });
    }

    onDeleteRow(index: number) {
        console.log(index);
        this.estimation.operations.splice(index, 1);
    }

    onDeleteCommercialPart(index: number) {
        console.log(index);
        this.estimation.commercialParts.splice(index, 1);
    }

    onSaveBtnClick() {

        this.save();
    }

    previousState() {
        window.history.back();
    }

    onFileChange() {
        // console.log(this.optionsModel);
        // this.save();
    }

    onFileArrayChange(event: Attachment[]) {
        // this.attachments = event;
        console.log('event from parent object: ', event);
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
        this.isSaving = false;
        this.previousState();
        //      this.router.navigate()
        // this.router.navigate(['estimation']);
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
        this.estimationService.find(id).subscribe((estimation) => {
            this.estimation = estimation;
            this.calculateOperationsTotalCost();
            this.calculateCommercialPartsTotalCost();

            this.orderService.findOrderSimpleDto(estimation.orderId).subscribe((order => {
                this.order = order;
            }));


        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    openModal(content) {
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

    calculateOperationsTotalCost() {
        this.operationsTotalCost = 0;

        for (const operation of this.estimation.operations) {
            if (operation.estimatedTime != null && operation.machine.workingHourPrice != null) {
                this.operationsTotalCost = this.operationsTotalCost + operation.estimatedTime * operation.machine.workingHourPrice;

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

    compareMachine(m1: Machine, m2: Machine): boolean {
        if (!isNullOrUndefined(m1) && !isNullOrUndefined(m2)) {
            return m1.id === m2.id;

        }
    }

    calculateTotal(): number {
        return this.commercialPartsTotalCost + this.operationsTotalCost + this.estimation.materialPrice
    }

    calculateDiscount(): number {
        return this.calculateTotal() * this.estimation.discount / 100
    }

    getTechnologyCardPdf() {
        this.estimationService.download(this.estimation);
    }

    messages: any[] = ['Message 5'];

    messageMapping: { [k: string]: string } = {
        '=0': 'No messages.',
        '=1': '# tydzień',
        '=2': '# tygodnie',
        '=3': '# tygodnie',
        '=4': '# tygodnie',
        'other': '# tygodni'
    };


    exportToTechnologyCard() {
        this.isExporting = true;
        console.log('test export technology card');
        console.log('est: ', this.estimation);
        this.estimationService.exportToTechnologyCard(this.estimation).subscribe(res => {
            const modalRef = this.modalService.open(TnAlert);
            modalRef.componentInstance.header = 'Uwaga';
            modalRef.componentInstance.content = 'Dane zostały wyeksportowane do karty technologicznej nr: '+res.json().id;
                this.isExporting = false;
            }, (error: any) => {
                this.isExporting = false;

            const modalRef = this.modalService.open(TnAlert);
            modalRef.componentInstance.header = 'Uwaga';
            modalRef.componentInstance.content = 'Błąd podczas eksportu! '
            }
        );
        //  console.log(response());
    }
}
