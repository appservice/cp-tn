///<reference path="../../../../../../node_modules/@angular/core/src/metadata/lifecycle_hooks.d.ts"/>
import {Component, OnDestroy, OnInit} from '@angular/core';
import {OrderAuditService} from './order-audit.service';
import {ResponseWrapper} from '../../shared';
import {Subscription} from 'rxjs/Subscription';
import {ActivatedRoute} from '@angular/router';
import {Audit} from '../../admin';
import {OrderAudit} from './order-audit.model';

@Component({
  selector: 'tn-order-audit',
  templateUrl: './order-audit.component.html',
  styles: []
})
export class OrderAuditComponent implements OnInit, OnDestroy {
    subscription: Subscription;
    orderAudits: OrderAudit[]=[];
    orderNumber: string;

  constructor(private orderAuditService: OrderAuditService,
              private route: ActivatedRoute,
  ) { }

  ngOnInit() {



      this.subscription = this.route.params.subscribe((params) => {
          console.log(params);
          if (params['orderId']) {
              this.load(params['orderId']);

          }

          if (params['orderNumber']) {
              this.orderNumber = params['orderNumber'];
          }
      });


  }

  load(orderId:number){
      this.orderAuditService.getAllAudits(orderId).subscribe(
          (response: ResponseWrapper)=>{
              console.log(response);
              this.orderAudits=response.json;
          }
      )

  }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }

    previousState() {
        window.history.back();
    }


}
