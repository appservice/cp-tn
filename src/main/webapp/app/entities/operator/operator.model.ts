import { BaseEntity } from './../../shared';

export class Operator implements BaseEntity {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public cardNumber?: string,
        public jobTitle?: string,
        public active?: boolean,
    ) {
        this.active = false;
    }
}
