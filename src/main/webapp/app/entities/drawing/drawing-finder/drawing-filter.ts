export class DrawingFilter {

    constructor(public name?: string,
                public  number?: string,
                public createdAtFrom?: any,
                public createdAtTo?: any,) {


    }

    getValidFromString(): string {
        if (this.createdAtFrom) {
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return new Date(this.createdAtFrom.year, this.createdAtFrom.month - 1, this.createdAtFrom.day).toISOString();

        }
    }

    getValidToString(): string {
        if (this.createdAtTo) {
            // return this.validFrom.year+"-"+this.validFrom.month+"-"+this.validFrom.day;
            return new Date(this.createdAtTo.year, this.createdAtTo.month - 1, this.createdAtTo.day + 1).toISOString();

        }
    }
}
