import {Product} from "../Product/product";
import {User} from "../user";
import {ProductInOrder} from "./productInOrder";

export class Order {

  constructor(
    public id: number = null,
    public orderDetails: ProductInOrder[] = null,
    public user: User = null,
    public orderStatus: string= null,
  ) {}

}
