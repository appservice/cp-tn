import {Component, OnDestroy, OnInit} from '@angular/core';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {TechnologyCard} from '../technology-card.model';
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
import {TechnologyCardService} from '../technology-card.service';
import {isNullOrUndefined} from 'util';
import {OrderService} from '../../order/order.service';
import {OrderSimpleDTO} from '../../order/order-simpleDTO.model';
import {DrawingFinderComponent} from '../../drawing/drawing-finder/drawing-finder.component';

@Component({
    selector: 'new-etechnology-card',
    templateUrl: './new-technology-card.component.html',
    styles: [],

})
export class NewTechnologyCardComponent implements OnInit, OnDestroy {

    private sumOfWorkingHours: number = 0;


    technologyCard: TechnologyCard;
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
    title: string;
    readOnly: boolean;


    currencyMaskOpt: CurrencyMaskConfig;

    constructor(private alertService: JhiAlertService,
                private machineService: MachineService,
                private eventManager: JhiEventManager,
                private router: Router,
                private route: ActivatedRoute,
                private modalService: NgbModal,
                private unitService: UnitService,
                private technologyCardService: TechnologyCardService,
                private activatedRoute: ActivatedRoute,) {
        this.technologyCard = new TechnologyCard();
        this.technologyCard.operations = [];

    }

    ngOnInit() {
        this.isSaving = false;


        this.subscription = this.route.params.subscribe((params) => {
            if (params['id']) {
                this.title = "Edytuj kartę technologizną"
                this.load(params['id']);

            } else {
                this.title = "Dodaj kartę technologizną"

            }
            if (params['readOnly']) {
                this.readOnly = params['readOnly'];

            }

        });

        this.activatedRoute.data.subscribe((data) => {
            this.readOnly = data['readOnly'];
        });
        this.machineService.getAllNotPageable()
            .subscribe((res: ResponseWrapper) => {
                this.machines = res.json;

            }, (res: ResponseWrapper) => this.onError(res.json));

        this.technologyCard.drawing = {id: null, attachments: []};
        // this.estimation.amount = 3;

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
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackMachineById(index: number, item: Machine) {
        return index;
    }

    addOperation() {
        this.technologyCard.operations.push({
            id: null,
            sequenceNumber: (this.technologyCard.operations.length + 1) * 5

        });
    }


    onDeleteRow(index: number) {
        console.log(index);
        this.technologyCard.operations.splice(index, 1);
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
        if (this.technologyCard.id !== undefined) {
            this.subscribeToSaveResponse(
                this.technologyCardService.update(this.technologyCard));
        } else {
            this.subscribeToSaveResponse(
                this.technologyCardService.create(this.technologyCard));
        }
    }

    private subscribeToSaveResponse(result: Observable<TechnologyCard>) {
        result.subscribe((res: TechnologyCard) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TechnologyCard) {
        this.eventManager.broadcast({name: 'technologyCardModification', content: 'OK'});
        this.isSaving = false;
        this.previousState();

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
        this.technologyCardService.find(id).subscribe((technologyCard) => {
            this.technologyCard = technologyCard;
            this.calculateTotalWorkingHours();


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


    compareMachine(m1: Machine, m2: Machine): boolean {
        if (!isNullOrUndefined(m1) && !isNullOrUndefined(m2)) {
            return m1.id === m2.id;

        }
    }


    getTechnologyCardPdf() {
        // this.estimationService.download(this.technologyCard);
    }


    openDrawingCardModal() {

        const modalRef = this.modalService.open(DrawingFinderComponent, {size: 'lg'});

        console.log(modalRef.result);
        modalRef.result.then(result => {
            console.log(result);
            this.technologyCard.drawing = result;
            // this.insertOperationFromTechnologyCard(result)
        }, (reason: any) => {
            console.log(reason)
        });
    }


    calculateTotalWorkingHours() {
        this.sumOfWorkingHours = 0;

        for (const operation of this.technologyCard.operations) {
            if (operation.estimatedTime != null) {
                this.sumOfWorkingHours = this.sumOfWorkingHours + operation.estimatedTime;

            }
        }
    }

}
