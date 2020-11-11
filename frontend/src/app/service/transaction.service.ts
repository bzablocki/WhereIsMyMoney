import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';

@Injectable()
export class TransactionService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getTransactionsFromPdf() {
    return this.apiService.get(this.config.getTransactionsFromPdfUrl);
  }


  getTransactionsFromDB() {
    return this.apiService.get(this.config.getTransactionsFromDBUrl);
  }

  getDeleteTransactions() {
    return this.apiService.get(this.config.deleteTransactionsUrl);
  }

}
