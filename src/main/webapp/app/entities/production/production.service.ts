import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, ResponseContentType, Headers, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {JhiDateUtils} from 'ng-jhipster';

import {ResponseWrapper, createRequestOption} from '../../shared';
import {ProductionItem} from './production-item.model';
import * as FileSaver from 'file-saver';


@Injectable()
export class ProductionService {

    private resourceUrl = 'api/production';
    private resourceSearchUrl = 'api/_search/orders';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }


    getItemsActualInProduction(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/items-actual-in-production', options)
            .map((res: Response) => this.convertResponse(res));
    }


    getItemsFinished(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/items-finished', options)
            .map((res: Response) => this.convertResponse(res));
    }


    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(cooperation: ProductionItem): ProductionItem {
        const copy: ProductionItem = Object.assign({}, cooperation);
        return copy;
    }


    moveProductionItemToArchive(producitonId: number, receiver ?: string): Observable<Response> {
        const params: URLSearchParams = new URLSearchParams();
        params.set('receiver', receiver);
        return this.http.put(this.resourceUrl + '/' + producitonId + '/moveToArchive', null, {params: params});
    }


    getProductionAsExcel(urlSearchParams?: URLSearchParams): Observable<any> {
        // const copy = this.convert(order);


        let headers = new Headers({'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});
        if (urlSearchParams) {
            options.params = urlSearchParams;

        }

        return this.http.get(`api/production/to-excel`, options)
            .map((res: Response) => res.blob())
            .do((data: any) => {
                this.saveDownload(data, 'Aktulanie w produkcji.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
            }, (error: any) => {
                console.log(error);
            });

    }

    getFinishedProductionAsExcel(urlSearchParams?: URLSearchParams): Observable<any> {
        // const copy = this.convert(order);


        let headers = new Headers({'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});
        if (urlSearchParams) {
            options.params = urlSearchParams;

        }

        return this.http.get(`api/production/finished/to-excel`, options)
            .map((res: Response) => res.blob())
            .do((data: any) => {
                this.saveDownload(data, 'Detale ukończone.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
            }, (error: any) => {
                console.log(error);
            });

    }

    saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data, fileName, disableAutoBOM);
    }

    returnToTechnologyVerification(orderId: number) :Observable<ResponseWrapper>{
       return this.http.get(this.resourceUrl+'/return-to-technology-verification/'+orderId);
    }
}
