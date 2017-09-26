import { BaseEntity } from './../../shared';

export class Trip implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public passengerCount?: number,
        public scheduledTime?: any,
        public departureTime?: any,
        public arrivalTime?: any,
        public activeTrip?: boolean,
        public routesId?: number,
        public tripMasterId?: number,
    ) {
        this.activeTrip = false;
    }
}
