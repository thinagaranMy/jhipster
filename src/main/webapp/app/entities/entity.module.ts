import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BmsappUserRoleModule } from './user-role/user-role.module';
import { BmsappStationModule } from './station/station.module';
import { BmsappRoutesModule } from './routes/routes.module';
import { BmsappTripModule } from './trip/trip.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        BmsappUserRoleModule,
        BmsappStationModule,
        BmsappRoutesModule,
        BmsappTripModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BmsappEntityModule {}
