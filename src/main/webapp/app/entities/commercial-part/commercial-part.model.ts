import { BaseEntity } from './../../shared';

export class CommercialPart implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public amount?: number,
        public price?: number,
        public unitId?: number,
        public estimationId?: number,
    ) {
    }
}
