import {Injectable} from '@angular/core';
import {IMyDateModel, IMyInputFieldChanged, INgxMyDpOptions} from 'ngx-mydatepicker';

@Injectable()
export class Globals {
    validDate: boolean;

    constructor() {
    }

    onInputFieldChanged(event: IMyInputFieldChanged) {
       // console.log('onInputFieldChanged(): Value: ', event.value, ' - dateFormat: ', event.dateFormat, ' - valid: ', event.valid);
        if (event.value != null && event.value) {
            this.validDate = event.valid;
        }

    }


    public myOptions: INgxMyDpOptions = {
        // other options...
        showWeekNumbers: true,
        dateFormat: 'dd-mm-yyyy',
        todayBtnTxt: 'Dzisiaj',
        dayLabels: {su: "Nie", mo: "Pon", tu: "Wto", we: "Sro", th: "Czw", fr: "Pią", sa: "Sob"},
        monthLabels: {1: "Sty", 2: "Lut", 3: "Mar", 4: "Kwi", 5: "Maj", 6: "Cze", 7: "Lip", 8: "Sier", 9: "Wrz", 10: "Paź", 11: "Lis", 12: "Gru"},
    };

    // optional date changed callback
    onDateChanged(event: IMyDateModel): void {
        this.validDate = true;

    }
}
