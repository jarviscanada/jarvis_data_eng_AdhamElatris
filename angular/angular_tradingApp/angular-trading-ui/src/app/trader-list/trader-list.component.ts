import { Component, OnInit } from '@angular/core';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';
import { MatDialog } from '@angular/material/dialog';
import { TraderFormComponent } from '../trader-form/trader-form.component';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css']
})
export class TraderListComponent implements OnInit {
  traderList: Trader[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'dob', 'country', 'actions'];

  constructor(
    private traderListService: TraderListService,
    private dialog: MatDialog  
  ) {}

  ngOnInit(): void {
    this.traderListService.getDataSource().subscribe(data => this.traderList = data);
  }

  deleteTrader(trader: Trader): void {
    this.traderListService.deleteTrader(trader.key);
    this.traderList = this.traderList.filter(t => t.key !== trader.key);
  }

  openAddTraderDialog(): void {
    const dialogRef = this.dialog.open(TraderFormComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(newTraderData => {
      if (newTraderData) {
        const newTrader: Trader = {
          key: Date.now().toString(),
          id: Date.now(),
          firstName: newTraderData.firstName,
          lastName: newTraderData.lastName,
          email: newTraderData.email,
          dob: newTraderData.dateOfBirth,
          country: newTraderData.country,
          amount: 0,
          
        };

        this.traderListService.addTrader(newTrader);
        this.traderList = [...this.traderList];
      }
    });
  }
}
