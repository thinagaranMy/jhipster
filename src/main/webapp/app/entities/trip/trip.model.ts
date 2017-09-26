import { BaseEntity } from './../../shared';

export class Trip implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public passengerCount?: number,
        public departTime?: any,
        public scheduledTime?: any,
        public arrivalTime?: any,
        public routesId?: number,
        public tripMasterId?: number,
    ) {
    }
}
