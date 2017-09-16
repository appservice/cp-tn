import { BaseEntity } from './../../shared';


export class OrderSimpleDTO implements BaseEntity {
    constructor(
        public id?: number,
        public internalNumber?: string,
        public referenceNumber?: string,
        // public sapNumber?: string,
        // public orderType?: OrderType,
        // public name?: string,
        // public description?: string,
        // public closeDate?: any,
        // public orderStatus?: OrderStatus,
        // public estimations?: Estimation[],
        public clientName?: string,
        public createdBy?: string,
        public createdAt?: string,
    ) {
    }
}
