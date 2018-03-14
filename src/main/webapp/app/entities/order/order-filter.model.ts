import {OrderStatus} from './order.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {IMyDateModel} from 'mydatepicker';

export class OrderFilter {

    constructor(public referenceNumber?: string,
                public internalNumber?: string,
                public orderStatus?: string,
                public clientName?: string,
                // public  validFrom?:  IMyDateModel,//NgbDateStruct,//
                public  validFrom?:  any,//IMyDateModel,//NgbDateStruct,//
                public  validTo?: IMyDateModel,//NgbDateStruct,
                public title ?: string,// IMyDateModel//NgbDateStruct,
                public creatorName ?: string,// IMyDateModel//NgbDateStruct,
    ) {


    }

    getValidFromString(): string {
        if (this.validFrom) {
            return this.validFrom.jsdate.toISOString();
          //  return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
          //  return new Date(this.validFrom.year, this.validFrom.month - 1, this.validFrom.day).toISOString();//this.validFrom.jsdate.toISOString();//

        }
    }

    getValidToString(): string {
        if (this.validTo) {
            console.log(this.validTo);
            return this.validTo.jsdate.toISOString();

            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
        //    return new Date(this.validTo.year, this.validTo.month - 1, this.validTo.day + 1).toISOString();

        }
    }
}
