import { BaseEntity } from './../../shared';

export class Attachemnt /*implements BaseEntity*/ {
    constructor(
        public id?: number,
        public path?: string,
        public name?: string,
        public dataContentType?: string,
        public uploadDate?:any,
        public drawingId?: number,
    ) {
    }
}
