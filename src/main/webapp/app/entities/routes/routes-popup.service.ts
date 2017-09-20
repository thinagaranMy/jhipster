import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Routes } from './routes.model';
import { RoutesService } from './routes.service';

@Injectable()
export class RoutesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private routesService: RoutesService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.routesService.find(id).subscribe((routes) => {
                    if (routes.validTo) {
                        routes.validTo = {
                            year: routes.validTo.getFullYear(),
                            month: routes.validTo.getMonth() + 1,
                            day: routes.validTo.getDate()
                        };
                    }
                    if (routes.validFrom) {
                        routes.validFrom = {
                            year: routes.validFrom.getFullYear(),
                            month: routes.validFrom.getMonth() + 1,
                            day: routes.validFrom.getDate()
                        };
                    }
                    this.ngbModalRef = this.routesModalRef(component, routes);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.routesModalRef(component, new Routes());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    routesModalRef(component: Component, routes: Routes): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.routes = routes;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
