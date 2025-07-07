import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TraderListService } from '../trader-list.service';
import { Trader } from '../trader';
import { MatDialog } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FundDialogComponent } from '../fund-dialog/fund-dialog.component';

@Component({
  selector: 'app-trader-account',
  templateUrl: './trader-account.component.html',
  styleUrls: ['./trader-account.component.css']
})
export class TraderAccountComponent implements OnInit {
  trader: Trader | undefined;

  constructor(
    private route: ActivatedRoute,
    private traderService: TraderListService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const traderId = +this.route.snapshot.paramMap.get('id')!;
    this.traderService.getDataSource().subscribe(list => {
      this.trader = list.find(t => t.id === traderId);
    });
  }

  openDepositDialog(): void {
    const dialogRef = this.dialog.open(FundDialogComponent, {
      width: '300px',
      data: { action: 'Deposit' }
    });

    dialogRef.afterClosed().subscribe(amount => {
      if (amount && this.trader) {
        this.trader.amount += amount;
      }
    });
  }

  openWithdrawDialog(): void {
    const dialogRef = this.dialog.open(FundDialogComponent, {
      width: '300px',
      data: { action: 'Withdraw' }
    });

    dialogRef.afterClosed().subscribe(amount => {
      if (amount && this.trader) {
        this.trader.amount = Math.max(0, this.trader.amount - amount);
      }
    });
  }
}
