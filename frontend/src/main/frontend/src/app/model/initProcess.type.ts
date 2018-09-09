import { FormGroup} from '@angular/forms';

export class InitProcess {

  numberPerDay: number;
  daysPerWeek: number;
  weeksPerSemester: number;
  mutationValue: number;
  populationSize: number;
  startedDate: string;

  constructor(form : FormGroup){
    this.numberPerDay = form.get('numberPerDay').value;
    this.daysPerWeek = form.get('daysPerWeek').value;
    this.weeksPerSemester = form.get('weeksPerSemester').value;
    this.mutationValue = form.get('mutationValue').value;
    this.populationSize = form.get('populationSize').value;
    this.startedDate = form.get('startedDate').value;
  }

}
