import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ConfigService} from './config.service';

@Injectable()
export class FooService {

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getFoo() {
    return this.apiService.get(this.config.fooUrl);
  }

  getTransactionsFromPdf() {
    return this.apiService.get(this.config.getTransactionsFromPdfUrl);
  }

  initCategories() {
    return this.apiService.get(this.config.getInitCategoriesUrl);
  }

  deleteAllCategories() {
    return this.apiService.get(this.config.getDeleteAllCategoriesUrl);
  }
  refreshTransactionToCategoryMapping() {
    return this.apiService.get(this.config.refreshTransactionToCategoryMappingUrl);
  }


}
