
import {BaseEntity} from '../../../shared/model/base-entity';
import {Unit} from '../../unit/unit.model';

export class CommercialPart implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public amount?: number,
        public description?: string,
        public price?: number,
        public unit?: Unit,
        public estimationId?: number,
        public total?: number,
    ) {
    }
}
