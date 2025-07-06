import { Component, OnInit } from '@angular/core';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';

@Component({
  selector: 'app-trader-list',
  templateUrl: './trader-list.component.html',
  styleUrls: ['./trader-list.component.css']
})
export class TraderListComponent implements OnInit {
  traderList: Trader[] = [];
  displayedColumns: string[] = ['firstName', 'lastName', 'email', 'dob', 'country', 'actions'];

  constructor(private traderListService: TraderListService) {}

  ngOnInit(): void {
    this.traderListService.getDataSource().subscribe(data => this.traderList = data);
  }

  deleteTrader(trader: Trader): void {
    this.traderListService.deleteTrader(trader.key);
    this.traderList = this.traderList.filter(t => t.key !== trader.key);
  }

  openAddTraderDialog(): void {
    // Implement modal dialog to add trader here (Angular Material Dialog)
  }
}
