import { BaseEntity } from './../../shared';

export class MpkBudgetMapper implements BaseEntity {
    constructor(
        public id?: number,
        public mpk?: string,
        public budget?: string,
        public description?: string,
        public clientId?: number,
    ) {
    }
}
