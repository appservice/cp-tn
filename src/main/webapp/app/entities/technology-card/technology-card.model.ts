import { BaseEntity } from './../../shared';

export class TechnologyCard implements BaseEntity {
    constructor(
        public id?: number,
        public material?: string,
        public mass?: number,
        public description?: string,
        public createdAt?: any,
        public amount?: number,
        public drawingId?: number,
        public operations?: BaseEntity[],
    ) {
    }
}
