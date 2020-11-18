import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private apiUrl = '/api';
  private userUrl = this.apiUrl + '/user';

  private _refreshTokenUrl = this.apiUrl + '/refresh';

  get refreshTokenUrl(): string {
    return this._refreshTokenUrl;
  }

  private _loginUrl = this.apiUrl + '/login';

  get loginUrl(): string {
    return this._loginUrl;
  }

  private _logoutUrl = this.apiUrl + '/logout';

  get logoutUrl(): string {
    return this._logoutUrl;
  }

  private _changePasswordUrl = this.apiUrl + '/changePassword';

  get changePasswordUrl(): string {
    return this._changePasswordUrl;
  }

  private _whoamiUrl = this.apiUrl + '/whoami';

  get whoamiUrl(): string {
    return this._whoamiUrl;
  }

  private _usersUrl = this.userUrl + '/all';

  get usersUrl(): string {
    return this._usersUrl;
  }

  private _resetCredentialsUrl = this.userUrl + '/reset-credentials';

  get resetCredentialsUrl(): string {
    return this._resetCredentialsUrl;
  }

  private _fooUrl = this.apiUrl + '/foo';

  get fooUrl(): string {
    return this._fooUrl;
  }

  private _signupUrl = this.apiUrl + '/signup';

  get signupUrl(): string {
    return this._signupUrl;
  }

  private _getTransactionsFromPdfUrl = this.apiUrl + '/getTransactionsFromPdf';

  get getTransactionsFromPdfUrl(): string {
    return this._getTransactionsFromPdfUrl;
  }

  private _getDeleteAllCategoriesUrl = this.apiUrl + '/getDeleteAllCategories';

  get getDeleteAllCategoriesUrl(): string {
    return this._getDeleteAllCategoriesUrl;
  }
  private _refreshTransactionToCategoryMappingUrl = this.apiUrl + '/refreshTransactionToCategoryMapping';

  get refreshTransactionToCategoryMappingUrl(): string {
    return this._refreshTransactionToCategoryMappingUrl;
  }

  private _getInitCategoriesUrl = this.apiUrl + '/getInitCategories';

  get getInitCategoriesUrl(): string {
    return this._getInitCategoriesUrl;
  }

  private _getTransactionsFromDBUrl = this.apiUrl + '/getTransactionsFromDB';

  get getTransactionsFromDBUrl(): string {
    return this._getTransactionsFromDBUrl;
  }
  private _deleteTransactionsUrl = this.apiUrl + '/deleteTransactions';

  get deleteTransactionsUrl(): string {
    return this._deleteTransactionsUrl;
  }

  private _uploadPdfUrl = this.apiUrl + '/api/upload-pdf';
  get uploadPdfUrl(): string {
    return this._uploadPdfUrl;
  }


}
