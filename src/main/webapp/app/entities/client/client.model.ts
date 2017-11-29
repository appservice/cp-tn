import { BaseEntity } from './../../shared';

export class Client implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortcut?: string,
        public address ?: string,
        public nip ?: string,
        public orders?: BaseEntity[],
        public annualOrderNumber ?: string,
        public printSinglePdfSummaryPerOrderItem ?: boolean,
    ) {
    }
}
