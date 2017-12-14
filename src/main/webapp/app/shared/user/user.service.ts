import {Injectable} from '@angular/core';
import {BaseRequestOptions, Http, Response, URLSearchParams} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {SERVER_API_URL} from '../../app.constants';
import {User} from './user.model';
import {ResponseWrapper} from '../model/response-wrapper.model';
import {createRequestOption} from '../model/request-util';

@Injectable()
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private resourceSearchBySentenceUrl = 'api/users/by-sentence';


    constructor(private http: Http) {
    }

    create(user: User): Observable<ResponseWrapper> {
        return this.http.post(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    update(user: User): Observable<ResponseWrapper> {
        return this.http.put(this.resourceUrl, user)
            .map((res: Response) => this.convertResponse(res));
    }

    find(login: string): Observable<User> {
        return this.http.get(`${this.resourceUrl}/${login}`).map((res: Response) => res.json());
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(login: string): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${login}`);
    }

    authorities(): Observable<string[]> {
        return this.http.get(SERVER_API_URL + 'api/users/authorities').map((res: Response) => {
            const json = res.json();
            return <string[]> json;
        });
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }


    searchBySentence(sentence: string): Observable<ResponseWrapper> {
        const options: BaseRequestOptions = new BaseRequestOptions();
        const params: URLSearchParams = new URLSearchParams();
        params.set('sentence', sentence);
        options.params = params;

        // const options = createRequestOption(req);
        return this.http.get(this.resourceSearchBySentenceUrl, options)
            .map((res: any) => this.convertResponse(res).json);
    }

    getUserWhichCanCreateTechnologyCard():Observable<ResponseWrapper>{
        return this.http.get(this.resourceUrl+'/can-create-technology-card/').
            map((res: any)=>this.convertResponse(res).json);
    }
}
