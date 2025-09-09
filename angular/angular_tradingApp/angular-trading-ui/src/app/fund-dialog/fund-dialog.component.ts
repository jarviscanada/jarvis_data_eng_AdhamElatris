import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-fund-dialog',
  templateUrl: './fund-dialog.component.html'
})
export class FundDialogComponent {
  amount: number = 0;

  constructor(
    public dialogRef: MatDialogRef<FundDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { action: string }
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSubmit(): void {
    this.dialogRef.close(this.amount);
  }
}
