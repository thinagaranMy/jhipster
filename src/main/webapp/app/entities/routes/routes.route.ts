import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RoutesComponent } from './routes.component';
import { RoutesDetailComponent } from './routes-detail.component';
import { RoutesPopupComponent } from './routes-dialog.component';
import { RoutesDeletePopupComponent } from './routes-delete-dialog.component';

@Injectable()
export class RoutesResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const routesRoute: Routes = [
    {
        path: 'routes',
        component: RoutesComponent,
        resolve: {
            'pagingParams': RoutesResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Routes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'routes/:id',
        component: RoutesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Routes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const routesPopupRoute: Routes = [
    {
        path: 'routes-new',
        component: RoutesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Routes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'routes/:id/edit',
        component: RoutesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Routes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'routes/:id/delete',
        component: RoutesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Routes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
