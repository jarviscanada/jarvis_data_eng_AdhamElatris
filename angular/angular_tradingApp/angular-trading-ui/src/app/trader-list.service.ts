import { Injectable } from '@angular/core';
import { Trader } from './trader';
import { of, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TraderListService {

  traderList: Trader[] = [
    {
      key: '1', id: 1, firstName: 'Mike', lastName: 'Spencer', dob: new Date().toLocaleDateString(),
      country: 'Canada', email: 'mike@test.com', amount: 0,
      actions: `<button (click)="deleteTrader">Delete Trader</button>`
    },
    {
      key: '2', id: 2, firstName: 'Hellen', lastName: 'Miller', dob: new Date().toLocaleDateString(),
      country: 'Austria', email: 'hellen@test.com', amount: 0,
      actions: `<button (click)="deleteTrader">Delete Trader</button>`
    }
  ];

  constructor() { }

  getDataSource(): Observable<Trader[]> {
    // Return your Trader list data as an observable here
    return of(this.traderList);
  }

  getColumns(): string[] {
    return ['First Name', 'Last Name', 'Email', 'DateOfBirth', 'Country', 'Actions'];
  }

  deleteTrader(key: string): void {
  this.traderList = this.traderList.filter(trader => trader.key !== key);
}

}
