import { BaseEntity } from './../../shared';
import {Machine} from '../machine/machine.model';

export class Operation implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public remark?: string,
        public estimatedTime?: number,
        public realTime?: number,
        public createdBy?: string,
        public machine?: Machine,
        // public machineCost?: number,
        public estimationId?: number,
    ) {
    }
}
