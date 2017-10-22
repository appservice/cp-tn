import {OrderStatus} from './order.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {IMyDateModel} from 'mydatepicker';

export class OrderFilter {

    constructor(public referenceNumber?: string,
                public internalNumber?: string,
                public orderStatus?: string,
                public clientName?: string,
                public  validFrom?: IMyDateModel,
                public  validTo?: IMyDateModel//NgbDateStruct,
                ) {


    }

    getValidFromString(): string {
        if (this.validFrom) {
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return this.validFrom.jsdate.toISOString();//new Date(this.validFrom.year, this.validFrom.month - 1, this.validFrom.day).toISOString();

        }
    }

    getValidToString(): string {
        if (this.validTo) {
            console.log(this.validTo);
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return new Date(this.validTo.date.year, this.validTo.date.month - 1, this.validTo.date.day + 1).toISOString();

        }
    }
}
