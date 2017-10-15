import {
    Component, ComponentFactoryResolver, ComponentRef, Directive, ElementRef, EventEmitter, forwardRef, Injectable, Input, NgZone, OnChanges, OnDestroy, Output, Renderer2,
    SimpleChanges, TemplateRef,
    ViewContainerRef
} from '@angular/core';
import {NgbCalendar, NgbDateParserFormatter, NgbDatepicker, NgbDatepickerI18n, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {PlacementArray, positionElements} from '@ng-bootstrap/ng-bootstrap/util/positioning';
import {NgbDatepickerNavigateEvent} from '@ng-bootstrap/ng-bootstrap/datepicker/datepicker';
import {AbstractControl, ControlValueAccessor, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validator} from '@angular/forms';
import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {NgbDatepickerService} from '@ng-bootstrap/ng-bootstrap/datepicker/datepicker-service';
import {DayTemplateContext} from '@ng-bootstrap/ng-bootstrap/datepicker/datepicker-day-template-context';
import {createInjector} from '@angular/core/src/view/refs';
import {ViewData} from '@angular/core/src/view';

const I18N_VALUES = {
    'pl': {
        weekdays: ['Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa', 'Di'],
        months: ['Sty', 'Lut', 'Mar', 'Kwi', 'Maj', 'Czer', 'Lip', 'Sie', 'Wrz', 'Paź', 'Lis', 'Gru'],
    }
    // other languages you would support
};

// Define a service holding the language. You probably already have one if your app is i18ned. Or you could also
// use the Angular LOCALE_ID value
@Injectable()
export class I18n {
    language = 'pl';
}

// Define custom service providing the months and weekdays translations
@Injectable()
export class CustomDatepickerI18n extends NgbDatepickerI18n {

    constructor(private _i18n: I18n) {
        super();
    }

    getWeekdayShortName(weekday: number): string {
        return I18N_VALUES[this._i18n.language].weekdays[weekday - 1];
    }

    getMonthShortName(month: number): string {
        return I18N_VALUES[this._i18n.language].months[month - 1];
    }

    getMonthFullName(month: number): string {
        return this.getMonthShortName(month);
    }
}

// @Component({
//     selector: 'ngbDatePickerPl',
//     templateUrl: './datepicker-i18n.html',
//     providers: [I18n, {provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n}], // define custom NgbDatepickerI18n provider,
//     exportAs: 'ngbDatepickerPl'
// })
// export class NgbDatePickerPlComponent {
//     model;
// }

class NgbDatePickerPlComp extends NgbDatepicker {
    constructor( _keyMapService, _service, _calendar,  config, _cd, _elementRef) {
        const i18n=new I18n();


        const customDtPickerI18n = new CustomDatepickerI18n(i18n);
        super(_keyMapService, _service, _calendar, customDtPickerI18n, config, _cd, _elementRef);
        console.log(' build NgbDatePickerPlComp',this);
    }
}

const NGB_DATEPICKER_VALUE_ACCESSOR = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => NgbDatePickerPl),
    multi: true
};

const NGB_DATEPICKER_VALIDATOR = {
    provide: NG_VALIDATORS,
    useExisting: forwardRef(() => NgbDatePickerPl),
    multi: true
};

/**
 * A directive that makes it possible to have datepickers on input fields.
 * Manages integration with the input field itself (data entry) and ngModel (validation etc.).
 */
@Directive({
    selector: 'input[ngbDatepickerPl]',
    exportAs: 'ngbDatepickerPl',
    host: {
        '(input)': 'manualDateChange($event.target.value)',
        '(change)': 'manualDateChange($event.target.value, true)',
        '(keyup.esc)': 'close()',
        '(blur)': 'onBlur()'
    },
    providers: [NGB_DATEPICKER_VALUE_ACCESSOR, NGB_DATEPICKER_VALIDATOR, NgbDatepickerService, I18n, {provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n},]
})
export class NgbDatePickerPl implements OnChanges,
    OnDestroy, ControlValueAccessor, Validator {
    private _cRef: ComponentRef<NgbDatePickerPlComp> = null;
    private _model: NgbDate;
    private _zoneSubscription: any;

    /**
     * Reference for the custom template for the day display
     */
    @Input() dayTemplate: TemplateRef<DayTemplateContext>;

    /**
     * Number of months to display
     */
    @Input() displayMonths: number;

    /**
     * First day of the week. With default calendar we use ISO 8601: 1=Mon ... 7=Sun
     */
    @Input() firstDayOfWeek: number;

    /**
     * Callback to mark a given date as disabled.
     * 'Current' contains the month that will be displayed in the view
     */
    @Input() markDisabled: (date: NgbDateStruct, current: { year: number, month: number }) => boolean;

    /**
     * Min date for the navigation. If not provided will be 10 years before today or `startDate`
     */
    @Input() minDate: NgbDateStruct;

    /**
     * Max date for the navigation. If not provided will be 10 years from today or `startDate`
     */
    @Input() maxDate: NgbDateStruct;

    /**
     * Navigation type: `select` (default with select boxes for month and year), `arrows`
     * (without select boxes, only navigation arrows) or `none` (no navigation at all)
     */
    @Input() navigation: 'select' | 'arrows' | 'none';

    /**
     * The way to display days that don't belong to current month: `visible` (default),
     * `hidden` (not displayed) or `collapsed` (not displayed with empty space collapsed)
     */
    @Input() outsideDays: 'visible' | 'collapsed' | 'hidden';

    /**
     * Placement of a datepicker popup accepts:
     *    "top", "top-left", "top-right", "bottom", "bottom-left", "bottom-right",
     *    "left", "left-top", "left-bottom", "right", "right-top", "right-bottom"
     * and array of above values.
     */
    @Input() placement: PlacementArray = 'bottom-left';

    /**
     * Whether to display days of the week
     */
    @Input() showWeekdays: boolean;

    /**
     * Whether to display week numbers
     */
    @Input() showWeekNumbers: boolean;

    /**
     * Date to open calendar with.
     * With default calendar we use ISO 8601: 'month' is 1=Jan ... 12=Dec.
     * If nothing or invalid date provided, calendar` will open with current month.
     * Use 'navigateTo(date)' as an alternative
     */
    @Input() startDate: { year: number, month: number };

    /**
     * A selector specifying the element the datepicker popup should be appended to.
     * Currently only supports "body".
     */
    @Input() container: string;

    /**
     * An event fired when navigation happens and currently displayed month changes.
     * See NgbDatepickerNavigateEvent for the payload info.
     */
    @Output() navigate = new EventEmitter<NgbDatepickerNavigateEvent>();

    private _onChange = (_: any) => {
    };
    private _onTouched = () => {
    };
    private _validatorChange = () => {
    };


    constructor(private _parserFormatter: NgbDateParserFormatter, private _elRef: ElementRef, private _vcRef: ViewContainerRef,
                private _renderer: Renderer2, private _cfr: ComponentFactoryResolver, ngZone: NgZone,
                private ngbDatePickerI18n: NgbDatepickerI18n,
                private _service: NgbDatepickerService, private _calendar: NgbCalendar) {
        this._zoneSubscription = ngZone.onStable.subscribe(() => {
            if (this._cRef) {
                positionElements(
                    this._elRef.nativeElement, this._cRef.location.nativeElement, this.placement, this.container === 'body');
            }
        });
    }

    registerOnChange(fn: (value: any) => any): void {
        this._onChange = fn;
    }

    registerOnTouched(fn: () => any): void {
        this._onTouched = fn;
    }

    registerOnValidatorChange(fn: () => void): void {
        this._validatorChange = fn;
    };

    setDisabledState(isDisabled: boolean): void {
        this._renderer.setProperty(this._elRef.nativeElement, 'disabled', isDisabled);
        if (this.isOpen()) {
            this._cRef.instance.setDisabledState(isDisabled);
        }
    }

    validate(c: AbstractControl): { [key: string]: any } {
        const value = c.value;

        if (value === null || value === undefined) {
            return null;
        }

        if (!this._calendar.isValid(value)) {
            return {'ngbDate': {invalid: c.value}};
        }

        if (this.minDate && NgbDate.from(value).before(NgbDate.from(this.minDate))) {
            return {'ngbDate': {requiredBefore: this.minDate}};
        }

        if (this.maxDate && NgbDate.from(value).after(NgbDate.from(this.maxDate))) {
            return {'ngbDate': {requiredAfter: this.maxDate}};
        }
    }

    writeValue(value) {
        const ngbDate = value ? new NgbDate(value.year, value.month, value.day) : null;
        this._model = this._calendar.isValid(value) ? ngbDate : null;
        this._writeModelValue(this._model);
    }

    manualDateChange(value: string, updateView = false) {
        this._model = this._service.toValidDate(this._parserFormatter.parse(value), null);
        this._onChange(this._model ? this._model.toStruct() : (value === '' ? null : value));
        if (updateView && this._model) {
            this._writeModelValue(this._model);
        }
    }

    isOpen() {
        return !!this._cRef;
    }

    /**
     * Opens the datepicker with the selected date indicated by the ngModel value.
     */
    open() {
        if (!this.isOpen()) {
            const cf = this._cfr.resolveComponentFactory(NgbDatepicker);
            // console.log(this._cfr);

            this._cRef = this._vcRef.createComponent(cf);
            // this._cRef.instance.i18n = this.ngbDatePickerI18n;

            console.log(this._cRef.instance.i18n.getMonthShortName(2));
            console.log(this._cRef.instance.i18n.getMonthShortName(2));


            this._applyPopupStyling(this._cRef.location.nativeElement);
            this._cRef.instance.writeValue(this._model);
            this._applyDatepickerInputs(this._cRef.instance);
            this._subscribeForDatepickerOutputs(this._cRef.instance);
            this._cRef.instance.ngOnInit();

            // date selection event handling
            this._cRef.instance.registerOnChange((selectedDate) => {
                this.writeValue(selectedDate);
                this._onChange(selectedDate);
                this.close();
            });

            // focus handling
            this._cRef.instance.focus();

            if (this.container === 'body') {
                window.document.querySelector(this.container).appendChild(this._cRef.location.nativeElement);
            }
        }
    }

    /**
     * Closes the datepicker popup.
     */
    close() {
        if (this.isOpen()) {
            this._vcRef.remove(this._vcRef.indexOf(this._cRef.hostView));
            this._cRef = null;
        }
    }

    /**
     * Toggles the datepicker popup (opens when closed and closes when opened).
     */
    toggle() {
        if (this.isOpen()) {
            this.close();
        } else {
            this.open();
        }
    }

    /**
     * Navigates current view to provided date.
     * With default calendar we use ISO 8601: 'month' is 1=Jan ... 12=Dec.
     * If nothing or invalid date provided calendar will open current month.
     * Use 'startDate' input as an alternative
     */
    navigateTo(date?: { year: number, month: number }) {
        if (this.isOpen()) {
            this._cRef.instance.navigateTo(date);
        }
    }

    onBlur() {
        this._onTouched();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['minDate'] || changes['maxDate']) {
            this._validatorChange();
        }
    }

    ngOnDestroy() {
        this.close();
        this._zoneSubscription.unsubscribe();
    }

    private _applyDatepickerInputs(datepickerInstance: NgbDatepicker): void {
        ['dayTemplate', 'displayMonths', 'firstDayOfWeek', 'markDisabled', 'minDate', 'maxDate', 'navigation',
            'outsideDays', 'showNavigation', 'showWeekdays', 'showWeekNumbers']
            .forEach((optionName: string) => {
                if (this[optionName] !== undefined) {
                    datepickerInstance[optionName] = this[optionName];
                }
            });
        datepickerInstance.startDate = this.startDate || this._model;
    }

    private _applyPopupStyling(nativeElement: any) {
        this._renderer.addClass(nativeElement, 'dropdown-menu');
        this._renderer.setStyle(nativeElement, 'padding', '0');
    }

    private _subscribeForDatepickerOutputs(datepickerInstance: NgbDatepicker) {
        datepickerInstance.navigate.subscribe(date => this.navigate.emit(date));
    }

    private _writeModelValue(model: NgbDate) {
        this._renderer.setProperty(this._elRef.nativeElement, 'value', this._parserFormatter.format(model));
        if (this.isOpen()) {
            this._cRef.instance.writeValue(model);
            this._onTouched();
        }
    }
}
