// import { BaseEntity } from './../../shared';
import {Machine} from '../machine/machine.model';
import {Operator} from '../operator/operator.model';

export class OperationReportDTO {
    constructor(public id?: number,
                // public description?: string,
                // public remark?: string,
                public estimatedTime?: number,
                // public realTime?: number,
                // public createdBy?: string,
                public machineName?: string,
                public sequenceNumber ?: number,
                public operationStatus ?: string,
                // public machineCost?: number,
                public operationEventList ?: OperationEventDTO [],
                public estimationId?: number,) {
    }
}

export class OperationEventDTO {
    constructor(public id?: number,
                public createdAt ?: any,
                public operationEventType ?: string,
                public operator ?: Operator,
                public  operationId ?: number,) {
    }
}
