import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ConfigService, FooService, UserService} from '../service';
import {TransactionElem} from '../component/table-expandable';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  fooResponse = {};
  whoamIResponse = {};
  allUserResponse = {};
  allTransactionsResponseDummy = {};
  allTransactionsResponse = []

  constructor(
    private config: ConfigService,
    private fooService: FooService,
    private userService: UserService
  ) {
  }

  ngOnInit() {
    this.userService.initUser()
    this.getTransactions();
  }

  getTransactions() {
    this.fooService.getTransactionsFromPdf()
      .subscribe(res => {
        this.transactionResponseObj(res);
      }, err => {
        this.transactionResponseObj(err);
      });

  }

  makeRequest(path) {
    if (path === this.config.getTransactionsFromPdfUrl) {
      this.fooService.getTransactionsFromPdf()
        .subscribe(res => {
          this.forgeResponseObj(this.allTransactionsResponseDummy, res, path);
        }, err => {
          this.forgeResponseObj(this.allTransactionsResponseDummy, err, path);
        });
    } else if (path === this.config.fooUrl) {
      this.fooService.getFoo()
        .subscribe(res => {
          this.forgeResponseObj(this.fooResponse, res, path);
        }, err => {
          this.forgeResponseObj(this.fooResponse, err, path);
        });
    } else if (path === this.config.whoamiUrl) {
      this.userService.getMyInfo()
        .subscribe(res => {
          this.forgeResponseObj(this.whoamIResponse, res, path);
        }, err => {
          this.forgeResponseObj(this.whoamIResponse, err, path);
        });
    } else {
      this.userService.getAll()
        .subscribe(res => {
          this.forgeResponseObj(this.allUserResponse, res, path);
        }, err => {
          this.forgeResponseObj(this.allUserResponse, err, path);
        });
    }
  }

  transactionResponseObj(res) {
    // const elem: TransactionElem = {
    //   name: 'My Element',
    //   description: 'This is my testing element',
    //   position: 1,
    //   symbol: 'ME',
    //   weight: 1.23
    // };
    if (res.ok === false) {
      this.allTransactionsResponse = []
    } else {
      console.log(res);
      this.allTransactionsResponse = res;
    }
  }

  forgeResponseObj(obj, res, path) {
    obj.path = path;
    obj.method = 'GET';
    if (res.ok === false) {
      // err
      obj.status = res.status;
      try {
        obj.body = JSON.stringify(JSON.parse(res._body), null, 2);
      } catch (err) {
        console.log(res);
        obj.body = res.error.message;
      }
    } else {
      // 200
      obj.status = 200;
      obj.body = JSON.stringify(res, null, 2);
    }
  }


  // ngOnChanges(changes: SimpleChanges): void {
  //
  //   console.log(changes)
  // }


}
