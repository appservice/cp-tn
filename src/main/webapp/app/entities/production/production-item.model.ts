import {UserShortDTO} from '../user';

export class ProductionItem {


    constructor(public estimationId?: number,
                public clientName ?: string,
                public orderNumber?: string,
                public itemNumber?: string,
                public itemName?: string,
                public amount?: number,
                public orderType?: any,
                public productionProgress?: number,
                public actualProductionPlace?: string,
                public nextOperationPlace?: string,
                public orderId ?: number,
                public readyForDispatch ?: boolean,
                public showProductionOrderLink ?: boolean,
                public showOperationsDetail ?: boolean,
                public estimatedRealizationDate ?: any,
                public receiver ?: UserShortDTO,
                public deliveredAt ?: any,
                public productionStartDateTime ?: any,
                public createdAt ?: any,
                public sapNumber ?:string,
                public realizationDateExpired ?: boolean,
                ) {
    }

}
