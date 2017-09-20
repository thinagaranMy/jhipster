import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Routes } from './routes.model';
import { RoutesService } from './routes.service';

@Component({
    selector: 'jhi-routes-detail',
    templateUrl: './routes-detail.component.html'
})
export class RoutesDetailComponent implements OnInit, OnDestroy {

    routes: Routes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private routesService: RoutesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRoutes();
    }

    load(id) {
        this.routesService.find(id).subscribe((routes) => {
            this.routes = routes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRoutes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'routesListModification',
            (response) => this.load(this.routes.id)
        );
    }
}
