import {Product} from "../Product/product";
import {User} from "../user";

export class OrderDTO {

  constructor(
    public products: Product[] = [],
    public email: String = null,
  ) {}

}
