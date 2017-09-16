import { BaseEntity } from './../../shared';
import {Attachemnt} from '../attachment/attachment.model';

export class Drawing implements BaseEntity {
    constructor(
        public id?: number,
        public number?: string,
        public name?: string,
        public attachments?: Attachemnt[],
        // public dataContentType?: string,
        // public data?: any,
        public estimationId?: number,
    ) {
    }
}
