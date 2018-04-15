import { BaseEntity } from './../../shared';

export class Machine implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortcut?: string,
        public workingHourPrice?: number,
        public validFrom?: any,
        public defaultTechnologyDesc ?: string,
        // public operationsId?: number,
    ) {
    }
}
