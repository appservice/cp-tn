import {BaseEntity} from './../../shared';
import {Estimation} from '../estimation/estimation.model';
import {UserShortDTO} from '../user/user-short-dto.model';

export const enum OrderType {
    ESTIMATION = 'ESTIMATION',
    EMERGENCY = 'EMERGENCY',
    PRODUCTION = 'PRODUCTION'
}

export const enum OrderStatus {
    'WORKING_COPY',
    'SENT_TO_ESTIMATION', 'IN_ESTIMATION', 'SENT_OFFER_TO_CLIENT', 'CREATED_PURCHASE_ORDER',
    'CREATING_SAP_ORDER', 'TECHNOLOGY_VERIFICATION',
    'IN_PRODUCTION', 'FINISHED',
    'TECHNOLOGY_CREATION'
}

export class Order implements BaseEntity {
    constructor(public id?: number,
                public internalNumber?: string,
                public referenceNumber?: string,
                public deliveryAddress?: string,
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
                public createdBy ?: UserShortDTO,
                public createdByName?: string,
                public estimationMakerName?: string,
                public remark ?: string,
                public inquiryId?: number,
                public estimationFinishDate?: any,
                public mpk?: string,
                public offerRemarks?: string,
                public canEdit ?: boolean,
                public canEditAsAdmin ?: boolean,
                public estimationMaker ?: UserShortDTO,) {
    }
}
