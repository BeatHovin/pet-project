import {Injectable} from '@angular/core'
import {HttpClient,} from '@angular/common/http'
import {environment} from 'src/environments/environment';
import {Observable} from "rxjs";
import {Product} from "../model/Product/product";
import {NgForm} from "@angular/forms";
import {ProductType} from "../model/Product/productType";
import {User} from "../model/user";
import {OrderDTO} from "../model/dto/orderDTO";
import {Order} from "../model/Order/order";

@Injectable({providedIn: 'root'})
export class OrderService {
  private host = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public getOrdersByStatus(status: string): Observable<Order[]>{
    return this.http.get<Order[]>(`${this.host}/order/orderListByStatus/${status}`);
  }

  public acceptOrder(orderId: number) {
    return this.http.get(`${this.host}/order/acceptOrder/${orderId}`);
  }

  public denyOrder(orderId: number) {
    return this.http.get(`${this.host}/order/denyOrder/${orderId}`);
  }

  public processOrder(orderId: number) {
    return this.http.get(`${this.host}/order/processOrder/${orderId}`);
  }


  public postOrder(orderDTO: OrderDTO) {
    return this.http.post<Product[]>(`${this.host}/order/postOrder`, orderDTO)
  }

  public getOrdersByEmail(email: string) {
    return this.http.get<Order[]>(`${this.host}/order/getOrderListByEmail/${email}`)
  }

  public getOrders() {
    return this.http.get(`${this.host}/order/getOrderList`)
  }

  public createOrderDTO(currentEmail: string, productsInOrder: Product[]): FormData {
    const formData = new FormData();
    formData.append('products', JSON.stringify(productsInOrder));
    formData.append('email', currentEmail);
    return formData;
  }
}
