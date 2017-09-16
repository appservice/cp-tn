import { BaseEntity } from './../../shared';

export class Unit implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
