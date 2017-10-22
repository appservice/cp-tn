import {Component, forwardRef, OnDestroy, OnInit} from '@angular/core';
import {ControlValueAccessor, FormControl, NG_VALIDATORS, NG_VALUE_ACCESSOR} from '@angular/forms';
import {IMyDateModel, IMyInputFieldChanged, INgxMyDpOptions, } from 'ngx-mydatepicker';

@Component({
  selector: 'tn-date-picker',
  templateUrl: './tn-date-picker.component.html',
  styles: [],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => TnDatePickerComponent),
            multi: true
        },
        {
            provide: NG_VALIDATORS,
            useValue: (c: FormControl) => {
                let err = {
                    rangeError: {
                        given: c.value,
                        max: 10,
                        min: 0
                    }
                };

                return (c.value > 10 || c.value < 0) ? err : null;
            },
            multi: true
        }
    ]

})
export class TnDatePickerComponent implements OnInit, ControlValueAccessor, OnDestroy  {



    validDate:boolean;
    dateValue:any;
    dp:any;
    propagateChange:any = () => {};


    writeValue(value: any): void {
        console.log(value);
        if(value)
        this.dateValue=value;
    }

    registerOnChange(fn: any): void {
        console.log('ts'+fn);
        this.propagateChange=fn;
        // this.dateValue=fn;
        // // throw new Error("Method not implemented.");
    }

    registerOnTouched(fn: any): void {
    //    throw new Error("Method not implemented.");
    }

    setDisabledState?(isDisabled: boolean): void {

        throw new Error("Method not implemented.");
    }

    constructor() { }

  ngOnInit() {
        this.validDate=true;
  }



        onInputFieldChanged(event: IMyInputFieldChanged) {
            console.log('onInputFieldChanged(): Value: ', event.value, ' - dateFormat: ', event.dateFormat, ' - valid: ', event.valid);
         //   console.log(event);
            if(event.value!=null && event.value){
                this.validDate=event.valid;
            }

        }
        myOptions: INgxMyDpOptions = {
            // other options...
            showWeekNumbers:true,
            dateFormat: 'dd-mm-yyyy',
            todayBtnTxt: 'Dzisiaj',
            dayLabels: {su: "Nie", mo: "Pon", tu: "Wto", we: "Sro", th: "Czw", fr: "Pią", sa: "Sob"},
            monthLabels: {1: "Sty", 2: "Lut", 3: "Mar", 4: "Kwi", 5: "Maj", 6: "Cze", 7: "Lip", 8: "Sier", 9: "Wrz", 10: "Paź", 11: "Lis", 12: "Gru"},
        };

        // optional date changed callback
        onDateChanged(event: IMyDateModel): void {
     //       console.log(event);
            this.validDate = true;
            this.dateValue=event;
            this.registerOnChange(event);

        }
    ngOnDestroy(): void {
        console.log("destroy");
        console.log(this.dp);
        if(this.dp){
            console.log(this.dp);
           this.dp.closeCalendar();

        }

    }
}
