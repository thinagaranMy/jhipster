import { BaseEntity, User } from './../../shared';

export class UserRole implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public description?: string,
        public user_userRoles?: User[],
    ) {
    }
}
