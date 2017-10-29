import { BaseEntity } from './../../shared';

export class Cooperation implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public counterparty?: string,
        public amount?: number,
        public price?: number,
        public estimationId?: number,
    ) {
    }
}
