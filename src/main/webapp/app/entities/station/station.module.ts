import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BmsappSharedModule } from '../../shared';
import {
    StationService,
    StationPopupService,
    StationComponent,
    StationDetailComponent,
    StationDialogComponent,
    StationPopupComponent,
    StationDeletePopupComponent,
    StationDeleteDialogComponent,
    stationRoute,
    stationPopupRoute,
    StationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...stationRoute,
    ...stationPopupRoute,
];

@NgModule({
    imports: [
        BmsappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StationComponent,
        StationDetailComponent,
        StationDialogComponent,
        StationDeleteDialogComponent,
        StationPopupComponent,
        StationDeletePopupComponent,
    ],
    entryComponents: [
        StationComponent,
        StationDialogComponent,
        StationPopupComponent,
        StationDeleteDialogComponent,
        StationDeletePopupComponent,
    ],
    providers: [
        StationService,
        StationPopupService,
        StationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BmsappStationModule {}
