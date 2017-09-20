import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BmsappSharedModule } from '../../shared';
import {
    RoutesService,
    RoutesPopupService,
    RoutesComponent,
    RoutesDetailComponent,
    RoutesDialogComponent,
    RoutesPopupComponent,
    RoutesDeletePopupComponent,
    RoutesDeleteDialogComponent,
    routesRoute,
    routesPopupRoute,
    RoutesResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...routesRoute,
    ...routesPopupRoute,
];

@NgModule({
    imports: [
        BmsappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RoutesComponent,
        RoutesDetailComponent,
        RoutesDialogComponent,
        RoutesDeleteDialogComponent,
        RoutesPopupComponent,
        RoutesDeletePopupComponent,
    ],
    entryComponents: [
        RoutesComponent,
        RoutesDialogComponent,
        RoutesPopupComponent,
        RoutesDeleteDialogComponent,
        RoutesDeletePopupComponent,
    ],
    providers: [
        RoutesService,
        RoutesPopupService,
        RoutesResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BmsappRoutesModule {}
