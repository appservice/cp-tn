import { BaseEntity } from './../../shared';
import {Drawing} from '../drawing/drawing.model';
import {Operation} from '../operation/operation.model';

export class TechnologyCard implements BaseEntity {
    constructor(
        public id?: number,
        public material?: string,
        public mass?: number,
        public description?: string,
        public createdAt?: any,
        public amount?: number,
        public drawing?: Drawing,
        public drawingNumber?: string,
        public createdByName?: string,
        public operations?: Operation[],
        public sequenceNumber ?: number,
    ) {
    }
}
