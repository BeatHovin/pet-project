import {Product} from "../Product/product";
import {User} from "../user";

export class ProductInOrder {

  constructor(
    public product: Product = null,
    public quantity: number = null
  ) {}

}
