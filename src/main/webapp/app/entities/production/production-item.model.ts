


export class ProductionItem  {


    constructor(public estimationId?: number,
                public orderNumber?: string,
                public itemNumber?: string,
                public itemName?: string,
                public amount?: number,
                public orderType?: any,
                public productionProgress?: number,
                public actualProductionPlace?: string,
                public nextOperationPlace?: string,

    ) {
    }

}
