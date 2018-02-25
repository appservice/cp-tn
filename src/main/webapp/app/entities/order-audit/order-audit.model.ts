import {BaseEntity} from '../../shared';


export class OrderAudit implements BaseEntity {
    constructor(public id?: number,
                public operation ?: string,
                public createdBy ?: any,
                public createdAt ?: any,
                public order ?: any) {
    }


}

