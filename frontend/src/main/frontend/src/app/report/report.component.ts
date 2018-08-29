import { Component, OnInit, Input, Output,ViewChild } from '@angular/core';
import {ReportService} from './report.service';
import { BaseChartDirective } from 'ng2-charts';
import Chart from 'chart.js';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {

  private reportHidden: boolean = true;
  @ViewChild(BaseChartDirective)
  public chart: BaseChartDirective;

  constructor(private reportService : ReportService) {
  }
  ngOnInit() {
  }

  public lineChartData:Array<any> = [
   {},
 ];
 public lineChartLabels:Array<any> = [];
 public lineChartOptions:any = {
   responsive: true
 };
 public lineChartColors:Array<any> = [
   { // grey
     backgroundColor: 'rgba(148,159,177,0.2)',
     borderColor: 'rgba(148,159,177,1)',
     pointBackgroundColor: 'rgba(148,159,177,1)',
     pointBorderColor: '#fff',
     pointHoverBackgroundColor: '#fff',
     pointHoverBorderColor: 'rgba(148,159,177,0.8)'
   },
 ];
 public lineChartLegend:boolean = true;
 public lineChartType:string = 'line';

 initReport() :void {
   this.reportHidden = false;
   this.reportService.get().subscribe(
     data => {
       this.lineChartLabels = [];
       let score = [];
       let hardScore = [];
       let softScore = [];
       let generation = [];
       for (let population of data.reportPopulation) {
            generation.push(population.populationGeneration);
            score.push(population.bestFitnessScore);
            hardScore.push(population.bestHardFitnessScore);
            softScore.push(population.bestSoftFitnessScore);
        }
        // this.lineChartLabels = generation;
        this.lineChartData = [{data: score , label: 'Score'}, {data: hardScore , label: 'hardScore'}, {data: softScore , label: 'softScore'}];
        this.chart.labels = generation;
        this.chart.chart.data.labels = generation;
        this.chart.chart.data.datasets = this.lineChartData;

        this.chart.chart.update();

       console.log(data);
     },
     err => {console.log("Error occured when get elements.")}
   );
 }

 // events
 public chartClicked(e:any):void {
   console.log(e);
 }

 public chartHovered(e:any):void {
   console.log(e);
 }

}
