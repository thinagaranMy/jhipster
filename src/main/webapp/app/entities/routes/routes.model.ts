import { BaseEntity } from './../../shared';

export class Routes implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public validTo?: any,
        public validFrom?: any,
        public transitStations?: BaseEntity[],
    ) {
    }
}
