import {Component, Input} from '@angular/core';
import {EstimationRemark} from '../../entities/estimation-remark/estimation-remark.model';

@Component({
    selector: 'tn-estimation-remark',
    templateUrl: './tn-estimation-remark-component.html'
})
export class TnEstimationRemarkComponent {
    @Input() estimationRemarks:EstimationRemark[];
}
