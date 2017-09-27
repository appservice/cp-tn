import {BaseEntity} from './../../shared';


export class EstimationRemark implements BaseEntity {
    constructor(public id?: number,
                public remark ?: string,
                public createdAt?: any,
                public createdByName?: string,
                public createdById?: number,
                public estimationId ?: number,) {
    }
}
