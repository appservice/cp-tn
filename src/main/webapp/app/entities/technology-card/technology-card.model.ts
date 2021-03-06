import { BaseEntity } from './../../shared';
import {Drawing} from '../drawing/drawing.model';
import {Operation} from '../operation/operation.model';
import {CommercialPart} from '../commercial-part';
import {Cooperation} from '../cooperation';

export class TechnologyCard implements BaseEntity {
    constructor(
        public id?: number,
        public material?: string,
        public materialType?: string,
        public mass?: number,
        public description?: string,
        public createdAt?: any,
        public amount?: number,
        public drawing?: Drawing,
        public drawingNumber?: string,
        public drawingId?: number,
        public drawingName?: string,
        public createdByName?: string,
        public operations?: Operation[],
        public sequenceNumber ?: number,
        public materialPrice ?: number,
        public commercialParts ?: CommercialPart [],
        public cooperationList ?: Cooperation [],
    ) {
    }
}
