import {BaseEntity} from './../../shared';
import {Drawing} from '../drawing/drawing.model';
import {CommercialPart} from './new-estimation/commercial-part.model';
import {Operation} from '../operation/operation.model';
import {EstimationRemark} from '../estimation-remark/estimation-remark.model';
import {Cooperation} from '../cooperation/cooperation.model';

export class Estimation implements BaseEntity {
    constructor(public id?: number,
                public internalNumber?: string,
                public itemNumber?: string,
                public itemName?: string,
                public material?: string,
                public materialPrice?: number,
                public amount?: number,
                public mass?: number,
                public description?: string,
                public estimatedCost?: number,
                public finalCost?: number,
                public drawing?: Drawing,
                public operations?: Operation[],
                public neededRealizationDate?: any,
                public orderId?: number,
                public orderInternalNumber?: string,
                public commercialParts?: CommercialPart[],
                public optionsModel?: number[],
                public discount?: number,
                public createdAt?: any,
                public createdBy?: string,
                public checked ?: boolean,
                public estimatedRealizationDate ?: any,
                public estimationRemarks?: EstimationRemark[],
                public sapNumber ?: string,
                public remark?: string,
                public cooperationList?: Cooperation[],
                public mpk?: string,
                public notRealizable?: boolean,
                ) {
    }
}
