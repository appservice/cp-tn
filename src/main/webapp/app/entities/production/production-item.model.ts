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


                ) {
    }

}
