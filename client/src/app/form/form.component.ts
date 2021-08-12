import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent {
  
  constructor ( private http: HttpClient){
    this.getDbList();
  }

  dbs: string[] = [];
  tables: string[] = [];
  columns: string[] = [];
  MAX_COLUMNS: number = 3;
  checkBoxValue: boolean[] = new Array(this.MAX_COLUMNS).fill(false);

  saveUrl: string = "http://localhost:8080/api/save";
  getDbsUrl: string = "http://localhost:8080/api/getdbs";
  getTablesUrl: string = "http://localhost:8080/api/gettables/";
  getColumnsUrl: string = "http://localhost:8080/api/getcolumns";
  getStatusUrl: string = "http://localhost:8080/api/status/";

  reset(){
    this.selectedColumns = [];
    this.selectedTable = "";
    this.selectedFilters = "";
    this.selectedLimit = "";
    this.selectedDb = "";
    this.checkBoxValue.fill(false);
  }
  
  resetDb(){
    this.selectedColumns = [];
    this.selectedTable = "";
    this.selectedFilters = "";
    this.selectedLimit = "";
    this.checkBoxValue.fill(false);
  }

  selectedDb : string = "";
  onSelectDb(value: string): void {
    this.reset();
    this.selectedDb = value;
    this.getTablesList();
    console.log(this.selectedDb);
  }

  selectedTable : string = "";
  onSelectTable(value: string): void {
    this.resetDb();
    this.selectedTable = value;
    this.getColumnsList();
    console.log(this.selectedTable);
  }


  selectedColumns : string[] = [];
  // checkBoxValue: boolean[] = new Array(this.MAX_COLUMNS).fill(false);
  onSelectColumns(value: string, index: number): void {
    console.log(this.checkBoxValue);
    const val_ind = this.selectedColumns.indexOf(value,0);
    if (val_ind>-1 && this.checkBoxValue[index]==true){
      this.selectedColumns.splice(val_ind,1);
    }
    else if (val_ind<0 && this.checkBoxValue[index]==false){
      this.selectedColumns.push(value);
    }
    console.log(this.selectedColumns);
  }

  selectedFilters : string = "";
  onSelectFilters(value : string): void {
    this.selectedFilters = value;
    console.log(this.selectedFilters);
  }

  selectedLimit : string = "";
  onSelectLimit(value : string): void {
    this.selectedLimit = value;
    console.log(this.selectedLimit);
  }

  getDbList(): void {
    this.http.get<string[]>(this.getDbsUrl).subscribe(
        (data) => {
           // console.log(data, "data");
            this.dbs = data
        },
        (error) => console.log(error),
    );
  }

  getTablesList(): void {
    this.http.get<string[]>(this.getTablesUrl+this.selectedDb).subscribe(
      (data) => {
        //console.log(data);
        this.tables = data
      },
      (error) => console.log(error),
    )
  }

  getColumnsList(): void {
    let json = {
      "db": this.selectedDb,
      "table": this.selectedTable
    };

    let headers = new HttpHeaders();
    headers = headers.set('Access-Control-Allow-Origin', 'http://localhost:4200')
    .set('Access-Control-Allow-Methods', "GET, POST, DELETE, PUT")
    .set('Content-Type', 'application/json');

    this.http.post<string[]>(this.getColumnsUrl, json, {headers: headers}).subscribe(
      (response) => {
        //console.log(response);
        this.columns = response;
        this.MAX_COLUMNS = response.length;
        console.log(this.MAX_COLUMNS);
        this.checkBoxValue = new Array(this.MAX_COLUMNS).fill(false);
      },
      (error) => console.log(error),
    )
  }

  selectedUUID: string = "";
  onSelectUuid(value : string): void {
    this.selectedUUID = value;
    console.log(this.selectedUUID);
  }

  response: any = "";
  refreshStatus(): void {
    if (this.selectedUUID===""){
	alert("Please enter a UUID");
    }else {
	this.http.get<string>(this.getStatusUrl+this.selectedUUID).subscribe(
        	(data) => {
        	console.log(data, "ref_data");
        	this.response= data;
      		},
      		(error) => console.log(error),
    	)
    }
    
  }
  
  filters : string[] = [];
  submitForm(): void {
    
    // console.log(this.selectedDb);
    // console.log(this.selectedTable);
    // console.log(this.selectedColumns);
    // console.log(this.selectedFilters);
    // console.log(this.selectedLimit);
    if (this.selectedFilters===""){
	this.filters = [];
    }else {
	this.filters = this.selectedFilters.split(",");
    }
    

    // console.log(this.filters);

    let json = {
      "table" : this.selectedTable,
      "db": this.selectedDb,
      "columns": this.selectedColumns,
      "filters": this.filters,
      "limit": this.selectedLimit
    };
    
    console.log(json);
    
    
    let headers = new HttpHeaders();
    headers = headers.set('Access-Control-Allow-Origin', 'http://localhost:4200')
    .set('Access-Control-Allow-Methods', "GET, POST, DELETE, PUT")
    .set('Content-Type', 'application/json');
    
    this.http.post(this.saveUrl, json, {headers: headers, responseType: 'text'}).subscribe(
      (response) => {
        //console.log(response);
        this.response = JSON.parse(response);
      },
      (error) => {
        console.log(error);
        this.response = "Error";
      }
      )    
  }
    
}
