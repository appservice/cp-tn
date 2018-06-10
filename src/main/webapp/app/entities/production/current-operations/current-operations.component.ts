import { Component, OnInit } from '@angular/core';
import {OperationService} from '../../operation';
import {CurrentOperation} from '../../operation/currentOperation.model';
import {ResponseWrapper} from '../../../shared';

@Component({
  selector: 'tn-current-operations',
  templateUrl: './current-operations.component.html',
  styles: []
})
export class CurrentOperationsComponent implements OnInit {

    currentOperations: CurrentOperation[];

  constructor(private operationService: OperationService) { }

  ngOnInit() {
      this.operationService.getCurrentOperations().subscribe((response: ResponseWrapper) => {
              this.currentOperations = response.json;
          },
          (error) =>
              console.log('error: ', error));

  }


  public  refresh(){
      this.operationService.getCurrentOperations().subscribe((response: ResponseWrapper) => {
              this.currentOperations = response.json;
          },
          (error) =>
              console.log('error: ', error));
  }
}
