import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Routes } from './routes.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RoutesService {

    private resourceUrl = 'api/routes';
    private resourceSearchUrl = 'api/_search/routes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(routes: Routes): Observable<Routes> {
        const copy = this.convert(routes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(routes: Routes): Observable<Routes> {
        const copy = this.convert(routes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Routes> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.validTo = this.dateUtils
            .convertLocalDateFromServer(entity.validTo);
        entity.validFrom = this.dateUtils
            .convertLocalDateFromServer(entity.validFrom);
    }

    private convert(routes: Routes): Routes {
        const copy: Routes = Object.assign({}, routes);
        copy.validTo = this.dateUtils
            .convertLocalDateToServer(routes.validTo);
        copy.validFrom = this.dateUtils
            .convertLocalDateToServer(routes.validFrom);
        return copy;
    }
}
