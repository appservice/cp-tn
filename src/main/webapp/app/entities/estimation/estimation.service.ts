import {Injectable} from '@angular/core';
import {Http, RequestOptions, Response, ResponseContentType, Headers, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Estimation} from './estimation.model';
import {ResponseWrapper, createRequestOption} from '../../shared';
import * as FileSaver from 'file-saver';
import {JhiDateUtils} from 'ng-jhipster';
import {letProto} from 'rxjs/operator/let';


@Injectable()
export class EstimationService {

    private resourceUrl = 'api/estimations';
    private resourceSearchUrl = 'api/_search/estimations';

    constructor(private http: Http, private dateUtils: JhiDateUtils) {
    }

    create(estimation: Estimation): Observable<Estimation> {
        const copy = this.convert(estimation);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(estimation: Estimation): Observable<Estimation> {
        const copy = this.convert(estimation);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Estimation> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {

            let jsonResponse = res.json();
            if (jsonResponse.estimatedRealizationDate != null) {
                const tempDate = this.dateUtils.convertLocalDateFromServer(jsonResponse.estimatedRealizationDate);

                jsonResponse.estimatedRealizationDate = {
                    year: tempDate.getFullYear(),
                    month: tempDate.getMonth() + 1,
                    day: tempDate.getDate()
                };

            }

            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl + '/to-finish', options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(estimation: Estimation): Estimation {
        const copy: Estimation = Object.assign({}, estimation);
        if (copy.estimatedRealizationDate != null) {

            copy.estimatedRealizationDate = this.dateUtils.convertLocalDateToServer(copy.estimatedRealizationDate);

        }
        return copy;
    }

    download(estimation: Estimation): void {
        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({responseType: ResponseContentType.Blob, headers});

        this.http.get(`${this.resourceUrl}/${estimation.id}/technology-card`, options)
            .map((res: Response) => res.blob())
            .subscribe((data: any) => {
                this.saveDownload(data, estimation.drawing.number, 'application/pdf');

            });
    }

    saveDownload(responseData: any, fileName: string, contentType: string) {
        const data: Blob = new Blob([responseData], {type: contentType});

        const disableAutoBOM = true;

        FileSaver.saveAs(data, 'Karta_obiegowa_' + fileName + '.pdf', disableAutoBOM);
    }


    exportToTechnologyCard(estimation: Estimation) {
        console.log('class from service');
        const copy = this.convert(estimation);
        console.log('copy', copy);
        return this.http.post('api/technology-cards/created-from-estimation', copy);
    }


    findInquiryByCriteria(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/inquiry-item-finder', options)
            .map((res: Response) => this.convertResponse(res));
    }

    findPurchaseORderByCriteria(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/purchase-order-item-finder', options)
            .map((res: Response) => this.convertResponse(res));
    }


    findEmergencyOrderByCriteria(req?: any, urlSearchParams?: URLSearchParams): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        if (urlSearchParams) {
            options.params.appendAll(urlSearchParams);
        }
        return this.http.get(this.resourceUrl + '/emergency-order-item-finder', options)
            .map((res: Response) => this.convertResponse(res));
    }

    publishPrice(estimationId: number, isPublished: boolean): Observable<Response> {
        return this.http.put(`${this.resourceUrl}/${estimationId}/publishPrice`, {'published': isPublished});
    }
}
