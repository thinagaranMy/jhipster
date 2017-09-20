import { BaseEntity } from './../../shared';

export class Station implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public validTo?: any,
        public validFrom?: any,
        public transitRoutes?: BaseEntity[],
    ) {
    }
}
