import {BaseEntity} from './../../shared';
import {Estimation} from '../estimation/estimation.model';

export const enum OrderType {
    'ESTIMATION',
    'SERVICE',
    'PRODUCTION'
}

export const enum OrderStatus {
    'WORKING_COPY',
    'SENT_TO_ESTIMATION', 'IN_ESTIMATION', 'IN_PRODUCTION', 'FINISHED'
}

export class Order implements BaseEntity {
    constructor(public id?: number,
                public internalNumber?: string,
                public referenceNumber?: string,
                public sapNumber?: string,
                public orderType?: OrderType,
                public name?: string,
                public description?: string,
                public closeDate?: any,
                public orderStatus?: any,
                public estimations?: Estimation[],
                public clientId?: number,
                public clientShortcut?: string,
                public createdAt?: string,
                public createdByName?: string,
                public estimationMakerName?: string,
                public remark ?: string,
                public inquiryId?: number,
                public estimationFinishDate?: any,
                ) {
    }
}
