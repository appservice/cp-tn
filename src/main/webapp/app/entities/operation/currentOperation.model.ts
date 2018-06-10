import {Operator} from '../operator';


export class CurrentOperation {
    constructor(
        public operationEventId?: number,
        public description?: string,
        public machineName?: string,
        public drawingNo?: string,
        public detailName?: string,
        public customerName?: string,
        public orderNumber ?: string,
        public orderId?: number,
        public staredAt?: any,
        public estimatedWorkTime?: number,
        public operator ?: Operator,
        public estimationId ?: number
    ) {
    }
}
