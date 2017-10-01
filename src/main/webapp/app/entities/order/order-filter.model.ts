
import {OrderStatus} from './order.model';

export class OrderFilter{

    constructor(
        public referenceNumber?: string,
        public internalNumber?: string,
        public orderStatus?: OrderStatus,
        public clientName?: string,

    ) {
    }
}
