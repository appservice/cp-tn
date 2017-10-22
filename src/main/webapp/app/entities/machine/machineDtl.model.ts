import { BaseEntity } from './../../shared';

export class MachineDtl implements BaseEntity {
    constructor(
        public id?: number,
        public workingHourPrice?: number,
        public validFrom?: any,
        public validTo?: any,

    ) {
    }
}
