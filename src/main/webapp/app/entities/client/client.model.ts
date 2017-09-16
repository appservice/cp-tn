import { BaseEntity } from './../../shared';

export class Client implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shortcut?: string,
        public orders?: BaseEntity[],
    ) {
    }
}
