import {OrderStatus} from './order.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

export class OrderFilter {

    constructor(public referenceNumber?: string,
                public internalNumber?: string,
                public orderStatus?: string,
                public clientName?: string,
                public  validFrom?: NgbDateStruct,
                public validTo?: NgbDateStruct,) {


    }

    getValidFromString(): string {
        if (this.validFrom) {
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return new Date(this.validFrom.year, this.validFrom.month-1, this.validFrom.day).toISOString();

        }
    }

    getValidToString(): string {
        if (this.validTo) {
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return new Date(this.validTo.year, this.validTo.month-1, this.validTo.day+1).toISOString();

        }
    }
}
