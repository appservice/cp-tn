export class User {
    public id?: any;
    public login?: string;
    public firstName?: string;
    public lastName?: string;
    public email?: string;
    public activated?: Boolean;
    public langKey?: string;
    public authorities?: any[];
    public createdBy?: string;
    public createdDate?: Date;
    public lastModifiedBy?: string;
    public lastModifiedDate?: Date;
    public password?: string;
    public clientId?: number;
    public clientName?: string;
    public phone?: string;


    constructor(id?: any,
                login?: string,
                firstName?: string,
                lastName?: string,
                email?: string,
                activated?: Boolean,
                langKey?: string,
                authorities?: any[],
                createdBy?: string,
                createdDate?: Date,
                lastModifiedBy?: string,
                lastModifiedDate?: Date,
                password?: string,
                clientId?: number,
                clientName?: string,
                phone?: string) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.clientId = clientId ? clientId : null;
        this.clientName = clientName ? clientName : null;
        this.phone = phone ? phone : null;
    }
}
